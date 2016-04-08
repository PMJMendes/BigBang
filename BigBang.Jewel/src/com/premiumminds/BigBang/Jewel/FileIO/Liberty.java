package com.premiumminds.BigBang.Jewel.FileIO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.UUID;

import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.FileFieldData;
import Jewel.Engine.SysObjects.FileSectionData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.SysObjects.FileIOBase;

/**
 * Class responsible for importing Generali's receipts
 */
public class Liberty extends FileIOBase {

	public static final UUID RDef_Imports = UUID
			.fromString("8D11763D-4B0B-44EF-8779-A0A701270881");
	public static final UUID FormatID_Liberty = UUID
			.fromString("14ADAE21-C09F-443B-BC2D-A5E200C7E2E0");
	public static final UUID ObjID_ImportStatus = UUID
			.fromString("EE1BF5D6-4116-410F-9A9B-A0A700F4F569");

	public static class Fields {
		public static final int POLICY = 4;
		public static final int RECEIPT = 7;
		public static final int STARTDATE = 8;
		public static final int ENDDATE = 9;
		public static final int TOTALPREMIUMSIGN = 13;
		public static final int TOTALPREMIUM = 14;
		public static final int SALESPREMIUMSIGN = 16;
		public static final int SALESPREMIUM = 17;
		public static final int COMMISSIONSIGN = 19;
		public static final int COMMISSION = 20;
		public static final int CHANNEL = 24;
		public static final int STATE = 25;
		public static final int RECEIPTTYPE = 28;
		public static final int COLLECTIONMETHOD = 30;
	}

	public static class StatusCodes {
		public static final UUID Code_0_Ok = UUID
				.fromString("611B2744-6880-4204-BED3-A0A700F5FC10");
	}

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
			HashSet<String> parsedReceipts, SQLServer pdb) throws BigBangJewelException {
		
		String lineToParse;
		String policyNumber;
		Policy receiptPolicy;
		String receiptNumber;
		Timestamp startDate;
		Timestamp endDate;
		BigDecimal totalPremium; // Built with the total premium + total premium sign fields
		BigDecimal salesPremium; // Built with the sales premium + sales premium sign fields
		BigDecimal commission; // Built with the commission + commission sign fields
		String channel;
		String state;
		UUID receiptTypeGuid; // TODO tabela
		String collectionMethod;
		
	}
	
}
