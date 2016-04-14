package com.premiumminds.BigBang.Jewel.FileIO;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileFieldData;
import Jewel.Engine.SysObjects.FileSectionData;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IStep;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.PaymentData;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateReceipt;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.Payment;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.TriggerForceShortCircuit;
import com.premiumminds.BigBang.Jewel.SysObjects.FileIOBase;

/**
 * Class responsible for importing Generali's receipts
 */
public class Liberty extends FileIOBase {

	public static final UUID ObjID_TypeTranslator = UUID.fromString("C42EEF38-C2B4-45BE-AE86-A5E600BAD859");
	public static final UUID RDef_Imports = UUID.fromString("8D11763D-4B0B-44EF-8779-A0A701270881");
	public static final UUID FormatID_Liberty = UUID.fromString("14ADAE21-C09F-443B-BC2D-A5E200C7E2E0");
	public static final UUID ObjID_ImportStatus = UUID.fromString("EE1BF5D6-4116-410F-9A9B-A0A700F4F569");
	public static final UUID FKPaymentType = UUID.fromString("A44F6D02-83A2-4D96-BF5F-A02200EB6857"); // paid to the insurance company

	public static class Fields {
		public static final int AGENT = 0;
		public static final int POLICY = 3;
		public static final int RECEIPT = 6;
		public static final int STARTDATE = 7;
		public static final int ENDDATE = 8;
		public static final int TOTALPREMIUMSIGN = 12;
		public static final int TOTALPREMIUM = 13;
		public static final int SALESPREMIUMSIGN = 15;
		public static final int SALESPREMIUM = 16;
		public static final int COMMISSIONSIGN = 18;
		public static final int COMMISSION = 19;
		public static final int STATE = 24;
		public static final int RECEIPTTYPE = 27;
		public static final int COLLECTIONMETHOD = 29;
	}

	public static class StatusCodes {
		public static final UUID Code_0_Ok 				= UUID.fromString("611B2744-6880-4204-BED3-A0A700F5FC10");
		public static final UUID Code_1_NoPolicy        = UUID.fromString("B54E8992-42B1-4C00-8831-A0A700F5FC10");
		public static final UUID Code_2_RepeatedReceipt = UUID.fromString("20C24636-8F0D-4B28-B117-A0A700F5FC10");
		public static final UUID Code_3_ExistingReceipt = UUID.fromString("81FCAFAE-31F4-481C-9431-A0A700F5FC10");
		public static final UUID Code_4_UnknownType     = UUID.fromString("FD82165C-492B-460A-8CE0-A0A700F5FC10");
		public static final UUID Code_5_InternalError   = UUID.fromString("C8E3D6D7-D3DE-45A7-AB40-A0A700F9FADE");
		public static final UUID Code_6_TotalPremError  = UUID.fromString("7F96CF04-665A-46BF-B7B7-F823D64FF2D8");
		public static final UUID Code_7_SalesPremError  = UUID.fromString("267F8473-FAEC-483F-8624-FEF3680CEC8C");
		public static final UUID Code_8_WrongAgent		= UUID.fromString("23D38AAF-742A-4CD4-8951-3398FD37031E");
		public static final UUID Code_9_PaymentCreated	= UUID.fromString("9F9F36CF-4A94-421E-84ED-4E1811053160");
		public static final UUID Code_10_PaymentNotPossible  = UUID.fromString("D1EBB3D3-6A6F-446A-99BB-A127012367F1");
	}

	public static class AllowedAgentsCodes {
		public static String Credite 		= "15296";
		public static String Moscavide 		= "20711";
		public static String Network 		= "22253";
	}

	// Array with the GUIDs from the two possible companies (Generali and Generali(AM))
	private UUID[] possibleCompanies = null;

	public UUID GetStatusTable() throws BigBangJewelException {
		return ObjID_ImportStatus;
	}

	/**
	 * The method responsible for parsing the content of the file from Liberty
	 */
	public void Parse() throws BigBangJewelException {

		MasterDB ldb;
		HashSet<String> parsedReceipts;
		FileSectionData[] receiptsArray;
		FileFieldData[] lineData;
		int i;

		// Gets a reference to the DB and creates a connection
		try {
			ldb = new MasterDB();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
		try {
			createSession(ldb);
		} catch (BigBangJewelException e) {
			try {
				ldb.Disconnect();
			} catch (SQLException e1) {

			}
			throw e;
		}

		parsedReceipts = new HashSet<String>();

		// Gets the receipts' array from the file data, and iterates it, parsing
		// each receipt individually
		receiptsArray = mobjData.getData()[0];

		for (i = 0; i < receiptsArray.length; i++) {
			try {
				ldb.BeginTrans();
			} catch (Throwable e) {
				try {
					ldb.Disconnect();
				} catch (SQLException e1) {

				}
				throw new BigBangJewelException(e.getMessage(), e);
			}

			try {
				lineData = receiptsArray[i].getData();
				ParseReceipt(i, lineData, parsedReceipts, ldb);
			} catch (Throwable e) {

			}

			// Commits the new created receipt
			try {
				ldb.Commit();
			} catch (Throwable e) {
				try {
					ldb.Disconnect();
				} catch (SQLException e1) {

				}
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

		// Creates a new entry in the database, with the information about the
		// processing
		try {
			createReport(ldb, "Importação Liberty", RDef_Imports,
					FormatID_Liberty.toString() + "|" + midSession.toString());
		} catch (BigBangJewelException e) {
			try {
				ldb.Disconnect();
			} catch (SQLException e1) {

			}
			throw e;
		}

		try {
			ldb.Disconnect();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	/**
	 * The method responsible for parsing a given receipt (entry from the File)
	 */
	private void ParseReceipt(int fileReceiptIndex, FileFieldData[] lineData,
			HashSet<String> parsedReceipts, SQLServer pdb)
					throws BigBangJewelException {

		String lineToParse;
		String agentCode; 
		String policyNumber;
		Policy receiptPolicy;
		String receiptNumber;
		Timestamp startDate;
		Timestamp endDate;
		BigDecimal totalPremium; // Built with the total premium + total premium sign fields
		BigDecimal salesPremium; // Built with the sales premium + sales premium sign fields
		BigDecimal commission; // Built with the commission + commission sign fields
		String state;
		UUID receiptTypeGuid;
		String collectionMethod;
		CreateReceipt createdReceipt;
		boolean possibleInsert = true;

		// Policy and receipt number, used to report import problems/success
		lineToParse = lineData[Fields.POLICY].getData() + lineData[Fields.RECEIPT].getData();

		try {

			state = lineData[Fields.STATE].getData().trim();
			collectionMethod = lineData[Fields.COLLECTIONMETHOD].getData().trim();

			// It only tries to insert receipts in two states
			if (" ".equals(state) || "C".equals(state)) {

				// Gets the agent code, and if it doesn't correspond to Crédite or to the Sales Network, logs an error
				agentCode = lineData[Fields.AGENT].getData().trim().replaceFirst("^0+(?!$)", "");
				if (!AllowedAgentsCodes.Credite.equals(agentCode) && !AllowedAgentsCodes.Moscavide.equals(agentCode) &&
						!AllowedAgentsCodes.Network.equals(agentCode)) {
					createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_8_WrongAgent, null);
					return;
				}

				// It only tries to insert the receipts whose agent corresponds to the one associated with the current 
				// user's namespace (chosen at login)
				if (isAgentLogged(agentCode)) {

					// Gets the policy to which the receipt is associated, and if it does not exist, logs an error
					policyNumber = lineData[Fields.POLICY].getData().trim().replaceFirst("^0+(?!$)", "");
					receiptPolicy = FindPolicy(policyNumber);
					if (receiptPolicy == null) {
						createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_1_NoPolicy, null);
						return;
					}

					// Gets the receipt number, and if it was previously processed, logs an error
					receiptNumber = lineData[Fields.RECEIPT].getData().trim().replaceFirst("^0+(?!$)", "");
					if (parsedReceipts.contains(receiptNumber)) {
						createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_2_RepeatedReceipt, null);
						return;
					}

					// Checks if the receipt exists in the database, and if it exists, logs an error
					// An already charged receipt may exist in the database
					if (FindReceipt(receiptNumber, receiptPolicy.GetProcessID())) {
						if ("C".equals(state)) {
							possibleInsert = false;
						} else {
							createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_3_ExistingReceipt, null);
							return;
						}
					}
					
					createdReceipt = new CreateReceipt(receiptPolicy.GetProcessID());

					// A receipt corresponding to a payment does not logs an error if already exists, but should
					// not be inserted
					if (possibleInsert) {
						// Gets the receipt type from the input file, and checks if it corresponds to a parametrized type in 
						// BigBang. If it doesn't, logs an error
						receiptTypeGuid = ProcessReceiptType(lineData[Fields.RECEIPTTYPE].getData().trim());
						if (receiptTypeGuid == null) {
							createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_4_UnknownType, null);
							return;
						}

						// Checks the total premium, and if it equal to 0, logs an error.
						totalPremium = BuildDecimal(lineData[Fields.TOTALPREMIUMSIGN].getData(), lineData[Fields.TOTALPREMIUM].getData());
						if (totalPremium.compareTo(BigDecimal.ZERO) == 0) {
							createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_6_TotalPremError, null);
							return;
						}

						// Checks the sales premium, and if it equal to 0, logs an error.
						salesPremium = BuildDecimal(lineData[Fields.SALESPREMIUMSIGN].getData(), lineData[Fields.SALESPREMIUM].getData());	
						if (salesPremium.compareTo(BigDecimal.ZERO) == 0) {
							createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_7_SalesPremError, null);
							return;
						}

						// Adds the receipt to the parsed ones
						parsedReceipts.add(receiptNumber);

						// Gets the values needed to create a receipt, and creates it
						startDate = BuildDate(lineData[Fields.STARTDATE].getData().trim());
						endDate = BuildDate(lineData[Fields.ENDDATE].getData().trim());
						commission = BuildDecimal(lineData[Fields.COMMISSIONSIGN].getData(), lineData[Fields.COMMISSION].getData());

						// Sets the new recipe with the (possible) values and stores it
						createdReceipt.mobjData = new ReceiptData();

						createdReceipt.mobjData.mstrNumber = receiptNumber;
						createdReceipt.mobjData.midType = receiptTypeGuid;
						createdReceipt.mobjData.mdblTotal = totalPremium;
						createdReceipt.mobjData.mdblCommercial = salesPremium;
						createdReceipt.mobjData.mdblCommissions = commission;
						createdReceipt.mobjData.mdblRetrocessions = null;
						createdReceipt.mobjData.mdblFAT = null;
						createdReceipt.mobjData.mdblBonusMalus = null;
						createdReceipt.mobjData.mbIsMalus = null;
						createdReceipt.mobjData.mdtIssue = null;
						createdReceipt.mobjData.mdtMaturity = startDate;
						createdReceipt.mobjData.mdtEnd = endDate;
						createdReceipt.mobjData.mdtDue = null;
						createdReceipt.mobjData.midMediator = null;
						createdReceipt.mobjData.mstrNotes = null;
						createdReceipt.mobjData.mstrDescription = null;

						createdReceipt.mobjImage = null;
						createdReceipt.mobjContactOps = null;
						createdReceipt.mobjDocOps = null;

						createdReceipt.Execute(pdb);

					}

					// If the receipt can be paid, it does...
					if (isPaidReceipt(state, collectionMethod)) {
						if (!payReceipt(Receipt.GetInstance(Engine.getCurrentNameSpace(), createdReceipt.mobjData.mid), pdb)) {
							createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_10_PaymentNotPossible, null);
							return;
						} else {
							createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_9_PaymentCreated, null);
						}
					}
					
					createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_0_Ok, createdReceipt.mobjData.mid);
					
				}
			}

		} catch (Throwable e) {
			createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_5_InternalError, null);
			return;
		}
	}

	/** 
	 * This method returns true if the logged agent corresponds to the one associated 
	 * with the current user's namespace (chosen at login)
	 */
	private boolean isAgentLogged(String agentCode) {

		if ((AllowedAgentsCodes.Credite.equals(agentCode) || 
				AllowedAgentsCodes.Moscavide.equals(agentCode)) &&
				Constants.NSID_CredEGS.equals(Engine.getCurrentNameSpace())) {
			return true;
		}

		if (AllowedAgentsCodes.Network.equals(agentCode) &&
				Constants.NSID_RedeCom.equals(Engine.getCurrentNameSpace())) {
			return true;
		}

		return false;
	}

	/** 
	 * This method initializes the possible companies' array, and tries to 
	 * find the policy with a given number to each of those companies
	 * (returning as soon as it is found)
	 */
	private Policy FindPolicy(String policyNumber) throws BigBangJewelException {


		if (possibleCompanies == null) {
			InitCompanies();
		}

		for (int i = 0; i < possibleCompanies.length; i++) {
			Policy policy = FindPolicy(policyNumber, possibleCompanies[i], true);
			if (policy != null) {
				return policy;
			}
		}

		return null;
	}

	/**
	 * This method tries to get the policy from the DB, corresponding to the policy number associated
	 * with a receipt read from the file
	 */
	private Policy FindPolicy(String policyNumber, UUID companyId, boolean shouldRetry)
			throws BigBangJewelException {

		String policyNumberAux;
		Policy fetchedPolicy;
		int policiesFound;
		MasterDB ldb;
		IEntity policyEntity;
		ResultSet fetchedPolicies;

		// In case it is the first call (meaning it is not recursive), it removes the white spaces from the policy number
		// It also includes the character '!' which later will be changed to the character 'N', indicating it is in UNICODE,
		// when querying the DB
		policyNumberAux = "!" + ( shouldRetry ? policyNumber.replaceFirst("^0+(?!$)", "") : policyNumber );

		fetchedPolicy = null;
		policiesFound = 0;

		// Gets the entity responsible for fetching and manipulating policies
		try {
			policyEntity = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			ldb = new MasterDB();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Gets the policies from that company, with a given number (it should return 1)
		try {
			fetchedPolicies = policyEntity.SelectByMembers(ldb, new int[] {0, 2}, 
					new java.lang.Object[] {policyNumberAux, companyId}, new int[0]);
		} catch (Throwable e) {
			try { 
				ldb.Disconnect(); 
			} catch (Throwable e1) {

			}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Iterates the fetched policies and increases the counter
		try {
			while (fetchedPolicies.next()) {
				fetchedPolicy = Policy.GetInstance(Engine.getCurrentNameSpace(), fetchedPolicies);
				policiesFound++;
			}
		}
		catch (Throwable e) {
			try { fetchedPolicies.close(); } catch (Throwable e2) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// "Cleanup"
		try {
			fetchedPolicies.close();
		}
		catch (Throwable e) {
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}
		try {
			ldb.Disconnect();
		}
		catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// If it wasn't possible to get the policy, and it was the first try, 
		// it tries again
		if (shouldRetry && (policiesFound == 0)) {
			fetchedPolicy = FindPolicy(policyNumber, companyId, false);
		}

		// If there is more than one policy found (which is an error, for there should only 
		// be a policy or a given number)
		if (policiesFound > 1) {
			return null;
		}

		return fetchedPolicy;
	}

	/**
	 * This method tries to get the receipt with a given number from the DB
	 */
	private boolean FindReceipt(String receiptNumber, UUID policyId) 
			throws BigBangJewelException {

		Receipt fetchedReceipt;
		boolean wasReceiptFound;
		IEntity receiptEntity;
		MasterDB ldb;
		ResultSet fetchedReceipts;

		fetchedReceipt = null;
		wasReceiptFound = false;

		// Gets the entity responsible for fetching and manipulating receipts
		try {
			receiptEntity = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Receipt));
			ldb = new MasterDB();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Gets the receipts with a given number
		try {
			fetchedReceipts = receiptEntity.SelectByMembers(ldb, new int[] {0}, 
					new java.lang.Object[] {"!" + receiptNumber}, new int[] {2});
		} catch (Throwable e)	{
			try { 
				ldb.Disconnect(); 
			} catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Iterates the fetched receipts and increases the counter
		try {
			while (fetchedReceipts.next()) {
				fetchedReceipt = Receipt.GetInstance(Engine.getCurrentNameSpace(), fetchedReceipts);
				if (policyId.equals(fetchedReceipt.getProcess().GetParent().getKey())) {
					wasReceiptFound = true;
					break;
				}
			}
		}
		catch (Throwable e) {
			try { 
				fetchedReceipts.close(); 
			} catch (Throwable e2) {}
			try { 
				ldb.Disconnect(); 
			} catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// "Cleanup"
		try {
			fetchedReceipts.close();
		} catch (Throwable e) {
			try {
				ldb.Disconnect();
			} catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}
		try {
			ldb.Disconnect();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return wasReceiptFound;
	}

	/**
	 * This method tries to get the receipt's type's GUID from the DB
	 */
	private UUID ProcessReceiptType(String receiptType) throws BigBangJewelException {

		String receiptTypeAux;
		UUID receiptGuid;
		IEntity receiptTypeEntity;
		MasterDB ldb;
		ResultSet fetchedTypes;

		receiptTypeAux = "!" + receiptType.trim();
		receiptGuid = null;

		// Gets the entity responsible for fetching and manipulating receipt types
		try {
			receiptTypeEntity = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjID_TypeTranslator));
			ldb = new MasterDB();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Gets the types with a given id
		try {
			fetchedTypes = receiptTypeEntity.SelectByMembers(ldb, new int[] {0}, 
					new java.lang.Object[] {receiptTypeAux}, new int[0]);
		} catch (Throwable e) {
			try { 
				ldb.Disconnect(); 
			} catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// Gets the fetched type
		try {
			if (fetchedTypes.next()) {
				receiptGuid = (UUID)Engine.GetWorkInstance(receiptTypeEntity.getKey(), fetchedTypes).getAt(1);
			}
		} catch (Throwable e) {
			try { 
				fetchedTypes.close(); 
			} catch (Throwable e2) {}
			try { 
				ldb.Disconnect(); 
			} catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		// "Cleanup"
		try {
			fetchedTypes.close();
		} catch (Throwable e) {
			try { 
				ldb.Disconnect(); 
			} catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}
		try {
			ldb.Disconnect();
		}
		catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return receiptGuid;
	}

	/** 
	 * This method transforms the strings with a sign and a value into a BigDecimal
	 * It also divides the value by 100
	 */
	private BigDecimal BuildDecimal(String sign, String value) {
		String textVal = value.replaceFirst("^0+(?!$)", "");
		textVal=(sign + textVal).trim();
		BigDecimal response = new BigDecimal(textVal);
		return response.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
	}

	/** 
	 * This method transforms the string with a date from the file into a Timestamp
	 */
	private Timestamp BuildDate(String dateString) {
		StringBuilder stringBuilder = new StringBuilder(dateString);
		dateString = stringBuilder.insert(dateString.length()-2, "-").toString();
		dateString = stringBuilder.insert(dateString.length()-5, "-").toString();
		return Timestamp.valueOf(dateString + " 00:00:00.0");
	}

	/**
	 * This method checks if it is possible to set the receipt as paid
	 */
	private boolean isPaidReceipt(String state, String collectionMethod) {
		HashSet<String> possibleMethods = new HashSet<String>(Arrays.asList("02", "03", "04", "08"));

		if(state != null && state.equals("C") && possibleMethods.contains(collectionMethod)) {
			return true;
		}
		return false;
	}

	/** 
	 * This method tries to pay the receipt automatically 
	 */
	private boolean payReceipt(Receipt receipt, SQLServer pdb) 
			throws BigBangJewelException {
		
		IProcess receiptProcess;
		Payment payment;

		// Creates a payment to the receipt and marks the receipt as paid
		try {
			receiptProcess = receipt.getProcess();

			if ( receiptProcess.GetLiveLog(Constants.OPID_Receipt_Payment, pdb) != null )
				return true;
			
			// Checks if the receipt is not paid already, and if it is in a state
			// that allows it to be paid
			if ( !AdvanceReceipt(receipt, pdb) )
				return false;

			payment = new Payment(receipt.getKey());
			payment.marrData = new PaymentData[] {new PaymentData()};
			payment.marrData[0].midPaymentType = FKPaymentType;
			payment.Execute(pdb);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return true;
	}

	/** 
	 * This method initializes the possible companies' array
	 */
	private void InitCompanies() throws BigBangJewelException {

		String companiesShortName[] = new String[] { "LIB", "LIBAM" };

		possibleCompanies = new UUID[companiesShortName.length];

		for (int i = 0; i < companiesShortName.length; i++) {
			possibleCompanies[i] = Company.FindCompany(Engine.getCurrentNameSpace(), companiesShortName[i]);
			if (possibleCompanies[i] == null) {
				throw new BigBangJewelException("Inesperado: Companhia de Seguros Generali não encontrada.");
			}
		}
	}

	/** 
	 * As seen on com.premiumminds.BigBang.Jewel.FileIO.Axa.java
	 * This method checks if a receipt may have an associated payment
	 */
	private boolean AdvanceReceipt(Receipt pobjReceipt, SQLServer pdb)
			throws BigBangJewelException {
		IProcess lrefRecProc;
		IStep lobjStep;
		TriggerForceShortCircuit lopEFSC;

		try {
			// Gets the receipt associated process
			lrefRecProc = pobjReceipt.getProcess();

			// Checks if the process allows the payment operation
			lobjStep = lrefRecProc.GetValidOperation(Constants.OPID_Receipt_Payment);
			if ( lobjStep == null ) {
				// if it was not possible, changes the process "state" and tries it again
				lobjStep = lrefRecProc.GetValidOperation(Constants.OPID_Receipt_TriggerForceShortCircuit);
				if ( lobjStep == null ) {
					return false;
				}
				lopEFSC = new TriggerForceShortCircuit(lrefRecProc.getKey());
				lopEFSC.Execute(pdb);
				lobjStep = lrefRecProc.GetValidOperation(Constants.OPID_Receipt_Payment);
			}
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return (lobjStep != null);
	}
}
