package com.premiumminds.BigBang.Jewel.FileIO;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileFieldData;
import Jewel.Engine.SysObjects.FileSectionData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Operations.Policy.CreateReceipt;
import com.premiumminds.BigBang.Jewel.SysObjects.FileIOBase;

/**
 * Class responsible for importing Tranquilidade's receipts 
 */
public class Tranquilidade extends FileIOBase {
	
	public static final UUID ObjID_TypeTranslator = UUID.fromString("D5CB3950-A195-4FD5-8629-A59A00F34322");
	public static final UUID RDef_Imports = UUID.fromString("8D11763D-4B0B-44EF-8779-A0A701270881");
	public static final UUID FormatID_Tranquilidade = UUID.fromString("A2501623-ECF1-4486-90E9-A59A00CBC830");
	public static final UUID ObjID_ImportStatus   = UUID.fromString("EE1BF5D6-4116-410F-9A9B-A0A700F4F569");

	public static class Fields {
	    public static final int POLICY       		=  2;
	    public static final int RECEIPT      		=  3;
	    public static final int CREATIONDATE 		=  4;
	    public static final int STARTDATE    		=  5;
	    public static final int ENDDATE      		=  6;
	    public static final int LIMITDATE    		=  7;
	    public static final int TOTALPREMIUM 		=  10;
	    public static final int COLLECTORCOMMISSION	=  11;
	    public static final int MEDIATORCOMMISSION	=  13;
	    public static final int RECEIPTTYPE			=  16;
	    public static final int RECEIPTSTATE		=  17;
	    public static final int SALESPREMIUM		=  20;
	}
	
	public static class StatusCodes {
	    public static final UUID Code_0_Ok              = UUID.fromString("611B2744-6880-4204-BED3-A0A700F5FC10");
	    public static final UUID Code_1_NoPolicy        = UUID.fromString("B54E8992-42B1-4C00-8831-A0A700F5FC10");
	    public static final UUID Code_2_RepeatedReceipt = UUID.fromString("20C24636-8F0D-4B28-B117-A0A700F5FC10");
	    public static final UUID Code_3_ExistingReceipt = UUID.fromString("81FCAFAE-31F4-481C-9431-A0A700F5FC10");
	    public static final UUID Code_4_UnknownType     = UUID.fromString("FD82165C-492B-460A-8CE0-A0A700F5FC10");
	    public static final UUID Code_5_BadFileLine     = UUID.fromString("AD012EF2-DAC6-4F80-A74A-A0A700F5FC10");
	    public static final UUID Code_6_InternalError   = UUID.fromString("C8E3D6D7-D3DE-45A7-AB40-A0A700F9FADE");
	    public static final UUID Code_7_TotalPremError  = UUID.fromString("7F96CF04-665A-46BF-B7B7-F823D64FF2D8");
	    public static final UUID Code_8_SalesPremError  = UUID.fromString("267F8473-FAEC-483F-8624-FEF3680CEC8C");
	}
	
	// Array with the GUIDs from the two possible companies (Tranquilidade and Tranquilidade (AM))
	private UUID[] possibleCompanies = null;
	
	public UUID GetStatusTable() throws BigBangJewelException {
		return ObjID_ImportStatus;
	}
	
	/** 
	 * The method responsible for parsing the content of the file from Tranquilidade
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
		
		// Gets the receipts' array from the file data, and iterates it, parsing each receipt individually
		receiptsArray = mobjData.getData()[1];

		for ( i = 0; i < receiptsArray.length; i++ ) {
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
		
		// Creates a new entry in the database, with the information about the processing
		try {
			createReport(ldb, "Importação Tranquilidade", 
					RDef_Imports, FormatID_Tranquilidade.toString() + "|" + midSession.toString());
		} catch (BigBangJewelException e) {
			try { 
				ldb.Disconnect(); 
			} catch (SQLException e1) {
				
			}
			throw e;
		}
		
		try {
			ldb.Disconnect();
		}
		catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
	
	/** 
	 * The method responsible for parsing a given receipt (entry from the File)
	 */
	private void ParseReceipt(int fileReceiptIndex, FileFieldData[] lineData, 
			HashSet<String> parsedReceipts, SQLServer pdb) throws BigBangJewelException {
		
		String lineToParse;
		Policy receiptPolicy;
		String receiptNumber;
		UUID receiptTypeGuid;
		Timestamp creationDate;
		Timestamp startDate;
		Timestamp endDate;
		Timestamp limitDate;
		BigDecimal totalPremium;
		BigDecimal commission; //COMMISSION = Collector Commission + Mediator Commission
		BigDecimal salesPremium;
		CreateReceipt createdReceipt;
		
		lineToParse = lineData[Fields.POLICY].getData() + lineData[Fields.RECEIPT].getData();
		
		try {
			
			// Checks if the receipt state is defined as OK, and if not, logs an error
			if (!"OK".equalsIgnoreCase(lineData[Fields.RECEIPTSTATE].getData())) {
				createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_5_BadFileLine, null);
				return; 
			}
			
			// Gets the policy to which the receipt is associated, and if it does not exist, logs an error
			receiptPolicy = FindPolicy(lineData[Fields.POLICY].getData().trim());
			if ( receiptPolicy == null ) {
				createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_1_NoPolicy, null);
				return;
			}
			
			// Gets the receipt number, and if it was previously processed, logs an error
			receiptNumber = lineData[Fields.RECEIPT].getData().trim();
			if (parsedReceipts.contains(receiptNumber)) {
				createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_2_RepeatedReceipt, null);
				return;
			}
			
			// Checks if the receipt exists in the database, and if it exists, logs an error
			if (FindReceipt(receiptNumber, receiptPolicy.GetProcessID())) {
				createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_3_ExistingReceipt, null);
				return;
			}
			
			// Gets the receipt type from the input file, and checks if it corresponds to a parametrized type in 
			// BigBang. If it doesn't, logs an error
			receiptTypeGuid = ProcessReceiptType(lineData[Fields.RECEIPTTYPE].getData());
			if (receiptTypeGuid == null) {
				createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_4_UnknownType, null);
				return;
			}
			
			// Checks the total premium, and if it equal to 0, logs an error.
			totalPremium = BuildDecimal(lineData[Fields.TOTALPREMIUM].getData());
			if (totalPremium.compareTo(BigDecimal.ZERO) == 0) {
				createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_7_TotalPremError, null);
				return;
			}
						
			// Checks the sales premium, and if it equal to 0, logs an error.
			salesPremium = BuildDecimal(lineData[Fields.SALESPREMIUM].getData());	
			if (salesPremium.compareTo(BigDecimal.ZERO) == 0) {
				createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_8_SalesPremError, null);
				return;
			}
			
			// Adds the receipt to the parsed ones
			parsedReceipts.add(receiptNumber);
			
			// Gets the values needed to create a receipt, and creates it
			creationDate = BuildDate(lineData[Fields.CREATIONDATE].getData());
			startDate = BuildDate(lineData[Fields.STARTDATE].getData());
			endDate = BuildDate(lineData[Fields.ENDDATE].getData());
			limitDate = BuildDate(lineData[Fields.LIMITDATE].getData());
			commission = BuildDecimal(lineData[Fields.COLLECTORCOMMISSION].getData())
					.add(BuildDecimal(lineData[Fields.MEDIATORCOMMISSION].getData()));	
			createdReceipt = new CreateReceipt(receiptPolicy.GetProcessID());
			
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
			createdReceipt.mobjData.mdtIssue = creationDate;
			createdReceipt.mobjData.mdtMaturity = startDate;
			createdReceipt.mobjData.mdtEnd = endDate;
			createdReceipt.mobjData.mdtDue = limitDate;
			createdReceipt.mobjData.midMediator = null;
			createdReceipt.mobjData.mstrNotes = null;
			createdReceipt.mobjData.mstrDescription = null;
	
			createdReceipt.mobjImage = null;
			createdReceipt.mobjContactOps = null;
			createdReceipt.mobjDocOps = null;
	
			createdReceipt.Execute(pdb);
			
		} catch (Throwable e) {
			createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_6_InternalError, null);
			return;
		}
		
		createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_0_Ok, createdReceipt.mobjData.mid);
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
	 * This method initializes the possible companies' array
	 */
	private void InitCompanies() throws BigBangJewelException {
		
		String companiesShortName[] = new String[] { "TSE", "TSEAM" };

		possibleCompanies = new UUID[companiesShortName.length];

		for (int i = 0; i < companiesShortName.length; i++) {
			possibleCompanies[i] = Company.FindCompany(Engine.getCurrentNameSpace(), companiesShortName[i]);
			if (possibleCompanies[i] == null) {
				throw new BigBangJewelException("Inesperado: Cia.Seg. Tranquilidade não encontrada.");
			}
		}
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
	private boolean FindReceipt(String receiptNumber, UUID receiptGuid) 
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
	        	if (receiptGuid.equals(fetchedReceipt.getProcess().GetParent().getKey())) {
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
	 * This method transforms the string with a date from the file into a Timestamp
	 */
	private Timestamp BuildDate(String pstrDate) {
		return Timestamp.valueOf(pstrDate + " 00:00:00.0");
	}

	
	/** 
	 * This method transforms the string with a value into a BigDecimal
	 */
	private BigDecimal BuildDecimal(String pstrNumber) {
		return new BigDecimal(pstrNumber.replace(',', '.'));
	}
	
}
