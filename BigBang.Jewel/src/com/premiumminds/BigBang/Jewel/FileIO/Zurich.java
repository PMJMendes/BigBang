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

public class Zurich
	extends FileIOBase
{
	public static final UUID ObjID_TypeTranslator = UUID.fromString("6380DEED-69F2-454D-B89C-A2CE00C79A7C");
	public static final UUID RDef_Imports = UUID.fromString("8D11763D-4B0B-44EF-8779-A0A701270881");
	public static final UUID FormatID_Zurich = UUID.fromString("552982E3-C2C2-4554-8CD5-A2CE00C03F78");
	public static final UUID ObjID_ImportStatus   = UUID.fromString("EE1BF5D6-4116-410F-9A9B-A0A700F4F569");

	public static class Fields
	{
		public static final int NAME                 =  0;
		public static final int ADDRESS              =  1;
		public static final int RECEIPTTYPE          =  2;
		public static final int LINE                 =  3;
		public static final int COLLECTINGAGENT      =  4;
		public static final int ISSUINGAGENT         =  5;
		public static final int POLICY               =  6;
		public static final int RECEIPT              =  7;
		public static final int STARTDATE            =  8;
		public static final int ENDDATE              =  9;
		public static final int ISSUEDATE            = 10;
		public static final int MATURITYDATE         = 11;
		public static final int CAPITAL              = 12;
		public static final int OBJECT               = 13;
		public static final int COMMERCIALPREMIUM    = 14;
		public static final int TOTALPREMIUM         = 15;
		public static final int COLLECTIONCOMMISSION = 16;
		public static final int ISSUINGCOMMISSION    = 17;
	}

	public static class StatusCodes
	{
	    public static final UUID Code_0_Ok              = UUID.fromString("611B2744-6880-4204-BED3-A0A700F5FC10");
	    public static final UUID Code_1_NumberNotParsed = UUID.fromString("071250BB-89DF-43CF-9760-A0A700F5FC10");
	    public static final UUID Code_2_RepeatedReceipt = UUID.fromString("20C24636-8F0D-4B28-B117-A0A700F5FC10");
	    public static final UUID Code_3_NoPolicy        = UUID.fromString("B54E8992-42B1-4C00-8831-A0A700F5FC10");
	    public static final UUID Code_4_ExistingReceipt = UUID.fromString("81FCAFAE-31F4-481C-9431-A0A700F5FC10");
	    public static final UUID Code_5_UnknownType     = UUID.fromString("FD82165C-492B-460A-8CE0-A0A700F5FC10");
	    public static final UUID Code_6_BadFileLine     = UUID.fromString("AD012EF2-DAC6-4F80-A74A-A0A700F5FC10");
	    public static final UUID Code_7_InternalError   = UUID.fromString("C8E3D6D7-D3DE-45A7-AB40-A0A700F9FADE");
	}

	public UUID GetStatusTable()
		throws BigBangJewelException
	{
		return ObjID_ImportStatus;
	}

	public void Parse()
		throws BigBangJewelException
	{
		MasterDB ldb;
		HashSet<String> larrSet;
		FileSectionData[] larrReceipts;
		FileFieldData[] larrData;
		int i;

		try
		{
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			createSession(ldb);
		}
		catch (BigBangJewelException e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw e;
		}

		larrSet = new HashSet<String>();
		larrReceipts = mobjData.getData()[0];

		for ( i = 0; i < larrReceipts.length; i++ )
		{
			try
			{
				ldb.BeginTrans();
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (SQLException e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}

			try
			{
				larrData = larrReceipts[i].getData();
				ParseReceipt(i, larrData, larrSet, ldb);
			}
			catch (Throwable e)
			{
			}

			try
			{
				ldb.Commit();
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (SQLException e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

		try
		{
			createReport(ldb, "Importação Zurich", RDef_Imports, FormatID_Zurich.toString() + "|" + midSession.toString());
		}
		catch (BigBangJewelException e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw e;
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private void ParseReceipt(int plngLine, FileFieldData[] parrData, HashSet<String> parrSet, SQLServer pdb)
		throws BigBangJewelException
	{
		String lstrLineText;
		Policy lobjPolicy;
		String lstrReceipt;
		UUID lidType;
		Timestamp ldtMaturity;
		Timestamp ldtEnd;
		Timestamp ldtLimit;
		Timestamp ldtIssue;
		BigDecimal ldblCommercial;
		BigDecimal ldblValue;
		BigDecimal ldblCommission;
		CreateReceipt lopCR;

		lstrLineText = parrData[Fields.RECEIPT].getData();

		try
		{
			lobjPolicy = FindPolicy(parrData[Fields.POLICY].getData(), true);
			if ( lobjPolicy == null )
			{
				createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_3_NoPolicy, null);
				return;
			}

			lstrReceipt = parrData[Fields.RECEIPT].getData();
			if ( parrSet.contains(lstrReceipt) )
			{
				createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_2_RepeatedReceipt, null);
				return;
			}
			if ( FindReceipt(lstrReceipt, lobjPolicy.GetProcessID()) )
			{
				createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_4_ExistingReceipt, null);
				return;
			}

			lidType = ProcessReceiptType(parrData[Fields.RECEIPTTYPE].getData());
			if ( lidType == null )
			{
				createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_5_UnknownType, null);
				return;
			}

			parrSet.add(lstrReceipt);

			ldtMaturity = BuildDate(parrData[Fields.STARTDATE].getData());
			ldtEnd = BuildDate(parrData[Fields.ENDDATE].getData());
			ldtLimit = BuildDate(parrData[Fields.MATURITYDATE].getData());
			ldtIssue = BuildDate(parrData[Fields.ISSUEDATE].getData());
			ldblCommercial = BuildDecimal(parrData[Fields.COMMERCIALPREMIUM].getData());
			ldblValue = BuildDecimal(parrData[Fields.TOTALPREMIUM].getData());
			ldblCommission = BuildDecimal(parrData[Fields.COLLECTIONCOMMISSION].getData()).
					add(BuildDecimal(parrData[Fields.ISSUINGCOMMISSION].getData()));

			lopCR = new CreateReceipt(lobjPolicy.GetProcessID());

			lopCR.mobjData = new ReceiptData();
			lopCR.mobjData.mstrNumber = lstrReceipt;
			lopCR.mobjData.midType = lidType;
			lopCR.mobjData.mdblTotal = ldblValue;
			lopCR.mobjData.mdblCommercial = ldblCommercial;
			lopCR.mobjData.mdblCommissions = ldblCommission;
			lopCR.mobjData.mdblRetrocessions = null;
			lopCR.mobjData.mdblFAT = null;
			lopCR.mobjData.mdblBonusMalus = null;
			lopCR.mobjData.mbIsMalus = null;
			lopCR.mobjData.mdtIssue = ldtIssue;
			lopCR.mobjData.mdtMaturity = ldtMaturity;
			lopCR.mobjData.mdtEnd = ldtEnd;
			lopCR.mobjData.mdtDue = ldtLimit;
			lopCR.mobjData.midMediator = null;
			lopCR.mobjData.mstrNotes = null;
			lopCR.mobjData.mstrDescription = null;

			lopCR.mobjImage = null;
			lopCR.mobjContactOps = null;
			lopCR.mobjDocOps = null;

			lopCR.Execute(pdb);
		}
		catch (Throwable e)
		{
			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_7_InternalError, null);
			return;
		}

		createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_0_Ok, lopCR.mobjData.mid);
	}

	private Policy FindPolicy(String pstrNumber, boolean pbRetry)
		throws BigBangJewelException
	{
		UUID midCompany;
		String lstrNumberAux;
		IEntity lrefPolicies;
        MasterDB ldb;
        ResultSet lrsPolicies;
		Policy lobjResult;
		int llngFound;

		midCompany = Company.FindCompany(Engine.getCurrentNameSpace(), "ZUR");
		if ( midCompany == null )
			throw new BigBangJewelException("Inesperado: Zurich não encontrada.");

		lstrNumberAux = "!" + ( pbRetry ? pstrNumber.replaceFirst("^0+(?!$)", "") : pstrNumber );
		lobjResult = null;
		llngFound = 0;

		try
		{
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

        try
        {
        	lrsPolicies = lrefPolicies.SelectByMembers(ldb, new int[] {0, 2}, new java.lang.Object[] {lstrNumberAux, midCompany}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
	        while (lrsPolicies.next())
	        {
	        	lobjResult = Policy.GetInstance(Engine.getCurrentNameSpace(), lrsPolicies);
	        	llngFound++;
	        }
        }
        catch (Throwable e)
        {
			try { lrsPolicies.close(); } catch (Throwable e2) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangJewelException(e.getMessage(), e);
        }

        try
        {
        	lrsPolicies.close();
        }
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( pbRetry && (llngFound == 0)  )
			lobjResult = FindPolicy(pstrNumber, false);

		if ( llngFound > 1 )
			return null;

		return lobjResult;
	}

	private boolean FindReceipt(String pstrNumber, UUID pidParentProc)
		throws BigBangJewelException
	{
		IEntity lrefReceipts;
        MasterDB ldb;
        ResultSet lrsReceipts;
		Receipt lobjResult;
		boolean lbFound;

		lobjResult = null;
		lbFound = false;

		try
		{
			lrefReceipts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Receipt));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

        try
        {
        	lrsReceipts = lrefReceipts.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {"!" + pstrNumber}, new int[] {2});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
	        while (lrsReceipts.next())
	        {
	        	lobjResult = Receipt.GetInstance(Engine.getCurrentNameSpace(), lrsReceipts);
	        	if ( pidParentProc.equals(lobjResult.getProcess().GetParent().getKey()) )
	        	{
		        	lbFound = true;
		        	break;
	        	}
	        }
        }
        catch (Throwable e)
        {
			try { lrsReceipts.close(); } catch (Throwable e2) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangJewelException(e.getMessage(), e);
        }

        try
        {
        	lrsReceipts.close();
        }
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lbFound;
	}

	private UUID ProcessReceiptType(String pstrType)
		throws BigBangJewelException
	{
		String lstrTypeAux;
		IEntity lrefTypes;
        MasterDB ldb;
        ResultSet lrsTypes;
		UUID lidResult;

		lstrTypeAux = "!" + pstrType.trim();
		lidResult = null;

		try
		{
			lrefTypes = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), ObjID_TypeTranslator));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

        try
        {
        	lrsTypes = lrefTypes.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {lstrTypeAux}, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
	        if (lrsTypes.next())
	        {
	        	lidResult = (UUID)Engine.GetWorkInstance(lrefTypes.getKey(), lrsTypes).getAt(1);
	        }
        }
        catch (Throwable e)
        {
			try { lrsTypes.close(); } catch (Throwable e2) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangJewelException(e.getMessage(), e);
        }

        try
        {
        	lrsTypes.close();
        }
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lidResult;
	}

	private Timestamp BuildDate(String pstrDate)
	{
		return Timestamp.valueOf(pstrDate.substring(0, 4) + "-" + pstrDate.substring(4, 6) + "-" + pstrDate.substring(6, 8) +
				" 00:00:00.0");
	}

	private BigDecimal BuildDecimal(String pstrNumber)
	{
		return new BigDecimal(pstrNumber.substring(0, pstrNumber.length() - 3) + "." + pstrNumber.substring(pstrNumber.length() - 2));
	}
}
