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
 * Class responsible for importing Generali's receipts 
 */
public class Generali extends FileIOBase {
	
	public static final UUID ObjID_TypeTranslator = UUID.fromString("C02BA415-DF20-46B0-B4F7-A59C0123DA6C");
	public static final UUID RDef_Imports = UUID.fromString("8D11763D-4B0B-44EF-8779-A0A701270881");
	public static final UUID FormatID_Generali = UUID.fromString("1C288653-113E-4499-822B-A59B0116EE1B");
	public static final UUID ObjID_ImportStatus   = UUID.fromString("EE1BF5D6-4116-410F-9A9B-A0A700F4F569");

	public static class Fields {
		public static final int POLICY       		=  9;
		public static final int RECEIPT      		=  10;
		public static final int CREATIONDATE 		=  11;
		public static final int STARTDATE    		=  16;
	    public static final int ENDDATE      		=  17;
	    public static final int DUEDATE      		=  18;
	    public static final int RECEIPTTYPE			=  19;
	    public static final int TOTALPREMIUM 		=  25;
	    public static final int SALESPREMIUM		=  26;
	    public static final int FAT					=  29;
	    public static final int SALESCOMMISSION		=  35;
	    public static final int COLLECTORCOMMISSION	=  36;
	}
	
	public static class StatusCodes {
	    public static final UUID Code_0_Ok              = UUID.fromString("611B2744-6880-4204-BED3-A0A700F5FC10");
	    public static final UUID Code_1_NoPolicy        = UUID.fromString("B54E8992-42B1-4C00-8831-A0A700F5FC10");
	    public static final UUID Code_2_RepeatedReceipt = UUID.fromString("20C24636-8F0D-4B28-B117-A0A700F5FC10");
	    public static final UUID Code_3_ExistingReceipt = UUID.fromString("81FCAFAE-31F4-481C-9431-A0A700F5FC10");
	    public static final UUID Code_4_UnknownType     = UUID.fromString("FD82165C-492B-460A-8CE0-A0A700F5FC10");
	    public static final UUID Code_5_InternalError   = UUID.fromString("C8E3D6D7-D3DE-45A7-AB40-A0A700F9FADE");
	    public static final UUID Code_6_TotalPremError  = UUID.fromString("7F96CF04-665A-46BF-B7B7-F823D64FF2D8");
	    public static final UUID Code_7_SalesPremError  = UUID.fromString("267F8473-FAEC-483F-8624-FEF3680CEC8C");
	}
	
	public UUID GetStatusTable() throws BigBangJewelException {
		return ObjID_ImportStatus;
	}
	
	/** 
	 * The method responsible for parsing the content of the file from Generali
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
		receiptsArray = mobjData.getData()[0];
		
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
			createReport(ldb, "Importação Generali", 
					RDef_Imports, FormatID_Generali.toString() + "|" + midSession.toString());
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
			HashSet<String> parsedReceipts, SQLServer pdb) throws BigBangJewelException {
		
		String lineToParse;
		String policyNumber;
		Policy receiptPolicy;
		String receiptNumber;
		UUID receiptTypeGuid;
		Timestamp creationDate;
		Timestamp startDate;
		Timestamp endDate;
		Timestamp limitDate;
		BigDecimal totalPremium;
		BigDecimal commission; //COMMISSION = Collector Commission + Sales Commission
		BigDecimal salesPremium;
		BigDecimal fat;
		CreateReceipt createdReceipt;
		
		lineToParse = lineData[Fields.POLICY].getData() + lineData[Fields.RECEIPT].getData();
		
		try {
			
			// Gets the policy to which the receipt is associated, and if it does not exist, logs an error
			policyNumber = lineData[Fields.POLICY].getData().trim()
					.replaceFirst("\\d{4}-", "").replaceAll("-.*", "");
			receiptPolicy = FindPolicy(policyNumber, true);
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
			
			// Gets the receipt type from the input file, and checks if it corresponds to a parameterized type in 
			// BigBang. If it doesn't, logs an error
			receiptTypeGuid = ProcessReceiptType(lineData[Fields.RECEIPTTYPE].getData());
			if (receiptTypeGuid == null) {
				createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_4_UnknownType, null);
				return;
			}
			
			// Checks the total premium, and if it equal to 0, logs an error.
			totalPremium = BuildDecimal(lineData[Fields.TOTALPREMIUM].getData());
			if (totalPremium.compareTo(BigDecimal.ZERO) == 0) {
				createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_6_TotalPremError, null);
				return;
			}
			
			// Checks the sales premium, and if it equal to 0, logs an error.
			salesPremium = BuildDecimal(lineData[Fields.SALESPREMIUM].getData());	
			if (salesPremium.compareTo(BigDecimal.ZERO) == 0) {
				createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_7_SalesPremError, null);
				return;
			}
			
			// Adds the receipt to the parsed ones
			parsedReceipts.add(receiptNumber);
						
			// Gets the values needed to create a receipt, and creates it
			creationDate = BuildDate(lineData[Fields.CREATIONDATE].getData());
			startDate = BuildDate(lineData[Fields.STARTDATE].getData());
			endDate = BuildDate(lineData[Fields.ENDDATE].getData());
			commission = BuildDecimal(lineData[Fields.COLLECTORCOMMISSION].getData())
					.add(BuildDecimal(lineData[Fields.SALESCOMMISSION].getData()));
			createdReceipt = new CreateReceipt(receiptPolicy.GetProcessID());
			limitDate = BuildDate(lineData[Fields.DUEDATE].getData());
			fat = BuildDecimal(lineData[Fields.FAT].getData());
			
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
			createdReceipt.mobjData.mdblFAT = fat;
			
			createdReceipt.mobjImage = null;
			createdReceipt.mobjContactOps = null;
			createdReceipt.mobjDocOps = null;
			
			createdReceipt.Execute(pdb);
						
		} catch (Throwable e) {
			createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_5_InternalError, null);
			return;
		}				
		
		createDetail(pdb, lineToParse, fileReceiptIndex, StatusCodes.Code_0_Ok, createdReceipt.mobjData.mid);
	}	
	
	/**
	 * This method tries to get the policy from the DB, corresponding to the policy number associated
	 * with a receipt read from the file
	 */
	private Policy FindPolicy(String policyNumber, boolean shouldRetry)
		throws BigBangJewelException {
		
		UUID companyGuid;
		String policyNumberAux;
		Policy fetchedPolicy;
		int policiesFound;
		MasterDB ldb;
		IEntity policyEntity;
		ResultSet fetchedPolicies;
		
		// Gets the GUID representing the company in the DB, and if it doesn't exists, throws an exception 
		companyGuid = Company.FindCompany(Engine.getCurrentNameSpace(), "GEN");
		if (companyGuid == null) {
			throw new BigBangJewelException("Inesperado: Companhia de Seguros Generali não encontrada.");
		}
		
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
					new java.lang.Object[] {policyNumberAux, companyGuid}, new int[0]);
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
		} catch (Throwable e) {
			try { fetchedPolicies.close(); } catch (Throwable e2) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
		    	throw new BigBangJewelException(e.getMessage(), e);
		}
				
		// "Cleanup"
		try {
			fetchedPolicies.close();
		} catch (Throwable e) {
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}
		try {
			ldb.Disconnect();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
				
		// If it wasn't possible to get the policy, and it was the first try, 
		// it tries again
		if (shouldRetry && (policiesFound == 0)) {
			fetchedPolicy = FindPolicy(policyNumber, false);
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
		String[] dateParts = pstrDate.split("-");
		return Timestamp.valueOf(dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0] + " 00:00:00.0");
	}

	
	/** 
	 * This method transforms the string with a value into a BigDecimal
	 */
	private BigDecimal BuildDecimal(String pstrNumber) {
		return new BigDecimal(pstrNumber.replace(',', '.'));
	}
	
}
