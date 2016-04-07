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
import Jewel.Engine.SysObjects.ObjectBase;
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
import com.premiumminds.BigBang.Jewel.Operations.Receipt.TriggerForceShortCircuit;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.NotPayedIndication;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.Payment;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.ReturnToInsurer;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.SetReturnToInsurer;
import com.premiumminds.BigBang.Jewel.SysObjects.FileIOBase;

public class Axa
	extends FileIOBase
{
	public static final UUID ObjID_TypeTranslator = UUID.fromString("90643E1D-3CE8-428A-9107-A12700FC73C4");
	public static final UUID RDef_Imports = UUID.fromString("8D11763D-4B0B-44EF-8779-A0A701270881");
	public static final UUID FormatID_Axa = UUID.fromString("408B7ADD-18DB-4D31-A11C-A12700E3001F");
	public static final UUID ObjID_ImportStatus   = UUID.fromString("EE1BF5D6-4116-410F-9A9B-A0A700F4F569");
	public static final UUID FKReturnMotive = UUID.fromString("BD7E61EB-BA8E-4D60-94D5-A10300B4EE4A");
	public static final UUID FKPaymentType = UUID.fromString("A44F6D02-83A2-4D96-BF5F-A02200EB6857");

	public static class Fields
	{
	    public static final int POLICY       =  0;
	    public static final int RECEIPT      =  1;
	    public static final int TYPE         =  2;
	    public static final int VALUE        =  3;
	    public static final int COMMISSION   =  4;
	    public static final int FILEDATE     =  5;
	    public static final int MATURITYDATE =  6;
	    public static final int ENDDATE      =  7;
	    public static final int STATE        =  8;
	    public static final int PAYMENTDATE  =  9;
	    public static final int LIMITDATE    = 10;
	    public static final int ISSUEDATE    = 11;
	}

	public static class StatusCodes
	{
	    public static final UUID Code_0_Ok                  = UUID.fromString("611B2744-6880-4204-BED3-A0A700F5FC10");
	    public static final UUID Code_1_NumberNotParsed     = UUID.fromString("071250BB-89DF-43CF-9760-A0A700F5FC10");
	    public static final UUID Code_2_RepeatedReceipt     = UUID.fromString("20C24636-8F0D-4B28-B117-A0A700F5FC10");
	    public static final UUID Code_3_NoPolicy            = UUID.fromString("B54E8992-42B1-4C00-8831-A0A700F5FC10");
	    public static final UUID Code_4_ExistingReceipt     = UUID.fromString("81FCAFAE-31F4-481C-9431-A0A700F5FC10");
	    public static final UUID Code_5_UnknownType         = UUID.fromString("FD82165C-492B-460A-8CE0-A0A700F5FC10");
	    public static final UUID Code_6_BadFileLine         = UUID.fromString("AD012EF2-DAC6-4F80-A74A-A0A700F5FC10");
	    public static final UUID Code_7_InternalError       = UUID.fromString("C8E3D6D7-D3DE-45A7-AB40-A0A700F9FADE");
	    public static final UUID Code_8_ReturnNotPossible   = UUID.fromString("B150E81D-8DCE-47D9-8B48-A12701235AF2");
	    public static final UUID Code_9_PaymentNotPossible  = UUID.fromString("D1EBB3D3-6A6F-446A-99BB-A127012367F1");
	    public static final UUID Code_10_AdvanceNotPossible = UUID.fromString("BBCCBC9B-BB3F-4FBD-990C-A127012CF8EC");
	}
	
	UUID[] marrCompanies;

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
			createReport(ldb, "Importação AXA", RDef_Imports, FormatID_Axa.toString() + "|" + midSession.toString());
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
		ObjectBase lobjType;
		Policy lobjPolicy;
		String lstrReceipt;
		Receipt lobjReceipt;

		lstrLineText = parrData[Fields.POLICY].getData().trim() + " - " + parrData[Fields.RECEIPT].getData().trim();

		try
		{
			lobjType = ProcessReceiptType(parrData[Fields.TYPE].getData(), parrData[Fields.STATE].getData());
			if ( lobjType == null )
			{
				createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_5_UnknownType, null);
				return;
			}

			lobjPolicy = FindPolicy(parrData[Fields.POLICY].getData().trim());
			if ( lobjPolicy == null )
			{
				createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_3_NoPolicy, null);
				return;
			}

			lstrReceipt = parrData[Fields.RECEIPT].getData().trim();
			if ( parrSet.contains(lstrReceipt) )
			{
				createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_2_RepeatedReceipt, null);
				return;
			}
			parrSet.add(lstrReceipt);

			lobjReceipt = FindReceipt(lstrReceipt, lobjPolicy.GetProcessID());
			if ( lobjReceipt == null )
				lobjReceipt = CreateReceipt(lobjPolicy, (UUID)lobjType.getAt(2), parrData, pdb);

			if ( Constants.OPID_Receipt_ReturnToInsurer.equals((UUID)lobjType.getAt(3)) )
			{
				if ( !ReturnReceipt(lobjReceipt, pdb) )
				{
					createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_8_ReturnNotPossible, null);
					return;
				}
			}

			if ( Constants.OPID_Receipt_Payment.equals((UUID)lobjType.getAt(3)) )
			{
				if ( !PayReceipt(lobjReceipt, pdb) )
				{
					createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_9_PaymentNotPossible, null);
					return;
				}
			}

			if ( Constants.OPID_Receipt_CreatePaymentNotice.equals((UUID)lobjType.getAt(3)) )
			{
				if ( !AdvanceReceipt(lobjReceipt, pdb) )
				{
					createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_10_AdvanceNotPossible, null);
					return;
				}
			}

			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_0_Ok, lobjReceipt.getKey());
		}
		catch (Throwable e)
		{
			createDetail(pdb, lstrLineText, plngLine, StatusCodes.Code_7_InternalError, null);
			return;
		}
	}

	private Policy FindPolicy(String pstrNumber) throws BigBangJewelException {
		if (marrCompanies == null) {
			InitCompanies();
		}
		
		for (int i = 0; i < marrCompanies.length; i++) {
			Policy policy = FindPolicy(pstrNumber, marrCompanies[i], true);
			if (policy != null) {
				return policy;
			}
		}
		
		return null;
	}
	
	private void InitCompanies() throws BigBangJewelException {
		String larrCompNames[] = new String[] { "AXA", "AXAM" };
		
		marrCompanies = new UUID[larrCompNames.length];
	
		for (int i = 0; i < larrCompNames.length; i++) {
			marrCompanies[i] = Company.FindCompany(Engine.getCurrentNameSpace(), larrCompNames[i]);
			if (marrCompanies[i] == null) {
				throw new BigBangJewelException("Inesperado: AXA Seguros Portugal não encontrada.");
			}
		}
	}
	
	private Policy FindPolicy(String pstrNumber, UUID midCompany, boolean pbRetry)
		throws BigBangJewelException
	{
		String lstrNumberAux;
		IEntity lrefPolicies;
        MasterDB ldb;
        ResultSet lrsPolicies;
		Policy lobjResult;
		int llngFound;

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
			lobjResult = FindPolicy(pstrNumber, midCompany, false);

		if ( llngFound > 1 )
			return null;

		return lobjResult;
	}

	private Receipt FindReceipt(String pstrNumber, UUID pidParentProc)
		throws BigBangJewelException
	{
		IEntity lrefReceipts;
        MasterDB ldb;
        ResultSet lrsReceipts;
		Receipt lobjResult;

		lobjResult = null;

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
		        	break;
	    		lobjResult = null;
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

		return lobjResult;
	}

	private ObjectBase ProcessReceiptType(String pstrType, String pstrState)
		throws BigBangJewelException
	{
		IEntity lrefTypes;
        MasterDB ldb;
        ResultSet lrsTypes;
        ObjectBase lobjResult;

		lobjResult = null;

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
        	lrsTypes = lrefTypes.SelectByMembers(ldb, new int[] {0, 1}, new java.lang.Object[] {"!" + pstrType.trim(), "!" + pstrState.trim()},
        			new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
	        if (lrsTypes.next())
	        	lobjResult = Engine.GetWorkInstance(lrefTypes.getKey(), lrsTypes);
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

		return lobjResult;
	}

	private Timestamp BuildDate(String pstrDate)
	{
		return Timestamp.valueOf("20" + pstrDate.trim().replace('.', '-') + " 00:00:00.0");
	}

	private BigDecimal BuildDecimal(String pstrNumber)
	{
		return new BigDecimal(pstrNumber.trim().replace(',', '.'));
	}

	private Receipt CreateReceipt(Policy pobjPolicy, UUID pidType, FileFieldData[] parrData, SQLServer pdb)
		throws BigBangJewelException
	{
		Timestamp ldtMaturity;
		Timestamp ldtEnd;
		Timestamp ldtLimit;
		Timestamp ldtIssue;
		BigDecimal ldblValue;
		BigDecimal ldblCommission;
		CreateReceipt lopCR;

		ldtMaturity = BuildDate(parrData[Fields.MATURITYDATE].getData());
		ldtEnd = BuildDate(parrData[Fields.ENDDATE].getData());
		ldtLimit = BuildDate(parrData[Fields.LIMITDATE].getData());
		ldtIssue = BuildDate(parrData[Fields.ISSUEDATE].getData());
		ldblValue = BuildDecimal(parrData[Fields.VALUE].getData());
		ldblCommission = BuildDecimal(parrData[Fields.COMMISSION].getData());

		lopCR = new CreateReceipt(pobjPolicy.GetProcessID());

		lopCR.mobjData = new ReceiptData();
		lopCR.mobjData.mstrNumber = parrData[Fields.RECEIPT].getData().trim();
		lopCR.mobjData.midType = pidType;
		lopCR.mobjData.mdblTotal = ldblValue;
		lopCR.mobjData.mdblCommercial = null;
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

		try
		{
			lopCR.Execute(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return Receipt.GetInstance(Engine.getCurrentNameSpace(), lopCR.mobjData.mid);
	}

	private boolean ReturnReceipt(Receipt pobjReceipt, SQLServer pdb)
		throws BigBangJewelException
	{
		IProcess lrefRecProc;
		IStep lobjStep;
		NotPayedIndication lopNPI;
		SetReturnToInsurer lopSRTI;
		ReturnToInsurer lopRTI;

		try
		{
			lrefRecProc = pobjReceipt.getProcess();

			if ( lrefRecProc.GetLiveLog(Constants.OPID_Receipt_ReturnToInsurer, pdb) != null )
				return true;

			lobjStep = lrefRecProc.GetValidOperation(Constants.OPID_Receipt_ReturnToInsurer);
			if ( lobjStep == null )
			{
				lobjStep = lrefRecProc.GetValidOperation(Constants.OPID_Receipt_SetReturnToInsurer);
				if ( lobjStep == null )
				{
					lobjStep = lrefRecProc.GetValidOperation(Constants.OPID_Receipt_NotPayedIndication);
					if ( lobjStep == null )
						return false;
					lopNPI = new NotPayedIndication(lrefRecProc.getKey());
					lopNPI.Execute(pdb);
				}
				else
				{
					lopSRTI = new SetReturnToInsurer(lrefRecProc.getKey());
					lopSRTI.midMotive = FKReturnMotive;
					lopSRTI.Execute(pdb);
				}
				lobjStep = lrefRecProc.GetValidOperation(Constants.OPID_Receipt_ReturnToInsurer);
				if ( lobjStep == null )
					return false;
			}

			lopRTI = new ReturnToInsurer(lrefRecProc.getKey());
			lopRTI.marrReceiptIDs = new UUID[] {pobjReceipt.getKey()};
			lopRTI.mbUseSets = false;
			lopRTI.Execute(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return true;
	}

	private boolean PayReceipt(Receipt pobjReceipt, SQLServer pdb)
		throws BigBangJewelException
	{
		IProcess lrefRecProc;
		Payment lopP;

		try
		{
			lrefRecProc = pobjReceipt.getProcess();

			if ( lrefRecProc.GetLiveLog(Constants.OPID_Receipt_Payment, pdb) != null )
				return true;

			if ( !AdvanceReceipt(pobjReceipt, pdb) )
				return false;

			lopP = new Payment(lrefRecProc.getKey());
			lopP.marrData = new PaymentData[] {new PaymentData()};
			lopP.marrData[0].midPaymentType = FKPaymentType;
			lopP.Execute(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return true;
	}

	private boolean AdvanceReceipt(Receipt pobjReceipt, SQLServer pdb)
		throws BigBangJewelException
	{
		IProcess lrefRecProc;
		IStep lobjStep;
		TriggerForceShortCircuit lopEFSC;

		try
		{
			lrefRecProc = pobjReceipt.getProcess();

			lobjStep = lrefRecProc.GetValidOperation(Constants.OPID_Receipt_Payment);
			if ( lobjStep == null )
			{
				lobjStep = lrefRecProc.GetValidOperation(Constants.OPID_Receipt_TriggerForceShortCircuit);
				if ( lobjStep == null )
					return false;
				lopEFSC = new TriggerForceShortCircuit(lrefRecProc.getKey());
				lopEFSC.Execute(pdb);
				lobjStep = lrefRecProc.GetValidOperation(Constants.OPID_Receipt_Payment);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return (lobjStep != null);
	}
}
