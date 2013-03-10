package com.premiumminds.BigBang.Jewel.Objects;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.JewelEngineException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.AccountingData;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators;
import com.premiumminds.BigBang.Jewel.Reports.MediatorAccountingReport;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionMapBase;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionSetBase;

public class MediatorAccountingMap
	extends TransactionMapBase
{
	public static class I
		extends TransactionMapBase.I
	{
		public static final int ENTRYNUMBER  = 3;
		public static final int ENTRYYEAR    = 4;
	}

    public static MediatorAccountingMap GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (MediatorAccountingMap)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MediatorAccountingMap), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static MediatorAccountingMap GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (MediatorAccountingMap)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MediatorAccountingMap),
					prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    private transient DocOps mobjDoc;
    private transient BigDecimal mdblTotal;
    private transient BigDecimal mdblRetention;
    private transient Timestamp mdtToday;

	public void Initialize()
		throws JewelEngineException
	{
		super.Initialize();
	}

	public UUID getParentType()
	{
		return Constants.ObjID_MediatorAccountingSet;
	}

	public UUID getSubObjectType()
	{
		return Constants.ObjID_MediatorAccountingDetail;
	}

	public void Settle(SQLServer pdb, UUID pidPrintSet)
		throws BigBangJewelException
	{
		ManageMediators lopMM;

		super.Settle(pdb, pidPrintSet);

		lopMM = new ManageMediators(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
		lopMM.mobjDocOps = generateDocOp(pdb);
		lopMM.marrAccounting = getAccountingData(pdb);

		try
		{
			lopMM.Execute(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public DocOps generateDocOp(SQLServer pdb)
		throws BigBangJewelException
	{
		MediatorAccountingReport lrepMA;
		FileXfer lobjFile;
		DocumentData lobjDoc;
		DocOps lobjResult;

		if ( mobjDoc != null )
			return mobjDoc;

		lrepMA = new MediatorAccountingReport();
		lrepMA.midMap = getKey();
		lobjFile = lrepMA.Generate();

		mdblTotal = lrepMA.mdblNet;
		mdtToday = lrepMA.mdtToday;
		mdblRetention = lrepMA.mdblRetention;

		lobjDoc = new DocumentData();
		lobjDoc.mstrName = "Retrocessão";
		lobjDoc.midOwnerType = Constants.ObjID_Mediator;
		lobjDoc.midOwnerId = (UUID)getAt(TransactionMapBase.I.OWNER);
		lobjDoc.midDocType = Constants.DocID_MediatorPayment;
		lobjDoc.mstrText = null;
		lobjDoc.mobjFile = lobjFile.GetVarData();
		lobjDoc.marrInfo = new DocInfoData[6];
		lobjDoc.marrInfo[0] = new DocInfoData();
		lobjDoc.marrInfo[0].mstrType = "Número de Recibos";
		lobjDoc.marrInfo[0].mstrValue = Integer.toString(lrepMA.mlngCount);
		lobjDoc.marrInfo[1] = new DocInfoData();
		lobjDoc.marrInfo[1].mstrType = "Total de Prémios";
		lobjDoc.marrInfo[1].mstrValue = String.format("%,.2f", lrepMA.mdblTotalPremiums);
		lobjDoc.marrInfo[2] = new DocInfoData();
		lobjDoc.marrInfo[2].mstrType = "Total de Comissões";
		lobjDoc.marrInfo[2].mstrValue = String.format("%,.2f", lrepMA.mdblTotalComms);
		lobjDoc.marrInfo[3] = new DocInfoData();
		lobjDoc.marrInfo[3].mstrType = "Total de Retrocessões";
		lobjDoc.marrInfo[3].mstrValue = String.format("%,.2f", lrepMA.mdblTotalRetros);
		lobjDoc.marrInfo[4] = new DocInfoData();
		lobjDoc.marrInfo[4].mstrType = "Retenção na Fonte";
		lobjDoc.marrInfo[4].mstrValue = String.format("%,.2f", lrepMA.mdblRetention);
		lobjDoc.marrInfo[5] = new DocInfoData();
		lobjDoc.marrInfo[5].mstrType = "Total Líquido";
		lobjDoc.marrInfo[5].mstrValue = String.format("%,.2f", lrepMA.mdblNet);

		lobjResult = new DocOps();
		lobjResult.marrCreate = new DocumentData[]{lobjDoc};

		mobjDoc = lobjResult;

		return lobjResult;
	}

	@SuppressWarnings("deprecation")
	public AccountingData[] getAccountingData(SQLServer pdb)
		throws BigBangJewelException
	{
		String lstrAccount;
		AccountingData[] larrResult;

		if ( mdblTotal.signum() <= 0 )
			return null;

		lstrAccount = Mediator.GetInstance(Engine.getCurrentNameSpace(),
				(UUID)getAt(TransactionMapBase.I.OWNER)).getEffectiveAccount();
		if ( lstrAccount == null )
			return null;

		initAccounting(pdb, mdtToday.getYear() + 1900);

		if ( (mdblRetention != null) && (mdblRetention.signum() != 0) )
		{
			larrResult = new AccountingData[4];
			
			larrResult[2] = new AccountingData();
			larrResult[2].mlngNumber = (Integer)getAt(I.ENTRYNUMBER);
			larrResult[2].mdtDate = mdtToday;
			larrResult[2].mdblAccount = new BigDecimal(lstrAccount);
			larrResult[2].mdblValue = mdblRetention.abs();
			larrResult[2].mstrSign = (mdblRetention.signum() > 0 ? "D" : "C");
			larrResult[2].mlngBook = 6;
			larrResult[2].mstrSupportDoc = (String)mrefSet.getAt(TransactionSetBase.I.NUMBER);
			larrResult[2].mstrDesc = "Prestação de Conta Mediadora";
			larrResult[2].midDocType = Constants.ObjID_MediatorAccountingMap;
			larrResult[2].mlngYear = (Integer)getAt(I.ENTRYYEAR);
			larrResult[2].midFile = null;

			larrResult[3] = new AccountingData();
			larrResult[3].mlngNumber = (Integer)getAt(I.ENTRYNUMBER);
			larrResult[3].mdtDate = mdtToday;
			larrResult[3].mdblAccount = new BigDecimal("2422");
			larrResult[3].mdblValue = mdblTotal.abs();
			larrResult[3].mstrSign = (mdblRetention.signum() > 0 ? "C" : "D");
			larrResult[3].mlngBook = 6;
			larrResult[3].mstrSupportDoc = (String)mrefSet.getAt(TransactionSetBase.I.NUMBER);
			larrResult[3].mstrDesc = "Prestação de Conta Mediadora";
			larrResult[3].midDocType = Constants.ObjID_MediatorAccountingMap;
			larrResult[3].mlngYear = (Integer)getAt(I.ENTRYYEAR);
			larrResult[3].midFile = null;
		}
		else
			larrResult = new AccountingData[2];

		larrResult[0] = new AccountingData();
		larrResult[0].mlngNumber = (Integer)getAt(I.ENTRYNUMBER);
		larrResult[0].mdtDate = mdtToday;
		larrResult[0].mdblAccount = new BigDecimal(lstrAccount);
		larrResult[0].mdblValue = mdblTotal.abs();
		larrResult[0].mstrSign = (mdblTotal.signum() > 0 ? "D" : "C");
		larrResult[0].mlngBook = 6;
		larrResult[0].mstrSupportDoc = (String)mrefSet.getAt(TransactionSetBase.I.NUMBER);
		larrResult[0].mstrDesc = "Prestação de Conta Mediadora";
		larrResult[0].midDocType = Constants.ObjID_MediatorAccountingMap;
		larrResult[0].mlngYear = (Integer)getAt(I.ENTRYYEAR);
		larrResult[0].midFile = null;

		larrResult[1] = new AccountingData();
		larrResult[1].mlngNumber = (Integer)getAt(I.ENTRYNUMBER);
		larrResult[1].mdtDate = mdtToday;
		larrResult[1].mdblAccount = new BigDecimal("1203");
		larrResult[1].mdblValue = mdblTotal.abs();
		larrResult[1].mstrSign = (mdblTotal.signum() > 0 ? "C" : "D");
		larrResult[1].mlngBook = 6;
		larrResult[1].mstrSupportDoc = (String)mrefSet.getAt(TransactionSetBase.I.NUMBER);
		larrResult[1].mstrDesc = "Prestação de Conta Mediadora";
		larrResult[1].midDocType = Constants.ObjID_MediatorAccountingMap;
		larrResult[1].mlngYear = (Integer)getAt(I.ENTRYYEAR);
		larrResult[1].midFile = null;

		return larrResult;
	}

	public void initAccounting(SQLServer pdb, int plngYear)
		throws BigBangJewelException
	{
		int llngNumber;

		if ( getAt(I.ENTRYNUMBER) != null )
			return;

		llngNumber = GetNewAccountingNumber(pdb, plngYear);

		try
		{
			setAt(I.ENTRYNUMBER, llngNumber);
			setAt(I.ENTRYYEAR, plngYear);
			SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public TR[] buildTable(int plngNumber)
		throws BigBangJewelException
	{
		TR[] larrAux;
		TR[] larrRows;
		int l, i;

		larrAux = super.buildTable(plngNumber);
		l = larrAux.length;

		larrRows = new TR[larrAux.length + 2];
		larrRows[0] = larrAux[0];

		larrRows[1] = ReportBuilder.constructDualRow("Agente",
				Mediator.GetInstance(getNameSpace(), (UUID)getAt(TransactionMapBase.I.OWNER)).getLabel(), TypeDefGUIDs.T_String, false);

		for ( i = 1; i < larrAux.length; i++ )
			larrRows[i + 1] = larrAux[i];

		larrAux = buildExtraRows();

		for ( i = l + 1; i < larrRows.length; i++ )
			larrRows[i] = larrAux[i - l - 1];

		return larrRows;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrAux;
		TD[] larrCells;
		int i;

		larrAux = super.buildInnerHeaderRow();
		i = larrAux.length;

		larrCells = Arrays.copyOf(larrAux, i + 2);

		larrCells[i] = ReportBuilder.buildHeaderCell("Comissão");
		ReportBuilder.styleCell(larrCells[i], false, true);

		larrCells[i + 1] = ReportBuilder.buildHeaderCell("Retrocessão");
		ReportBuilder.styleCell(larrCells[i + 1], false, true);

		return larrCells;
	}

	private TR[] buildExtraRows()
		throws BigBangJewelException
	{
		BigDecimal ldblTotal;
		BigDecimal ldblRetro;
		int i;

		ldblTotal = BigDecimal.ZERO;

		getDetails();
		for ( i = 0; i < marrDetails.length; i++ )
		{
			ldblRetro = ((MediatorAccountingDetail)marrDetails[i]).getRetrocession();
			if ( ldblRetro != null )
				ldblTotal = ldblTotal.add(ldblRetro);
		}

		TR[] larrRows;

		larrRows = new TR[1];

		larrRows[0] = ReportBuilder.constructDualRow("Total de Retrocessões", ldblTotal, TypeDefGUIDs.T_Decimal, false);

		return larrRows;
	}

	private int GetNewAccountingNumber(SQLServer pdb, int plngYear)
		throws BigBangJewelException
	{
		IEntity lrefMaps;
        ResultSet lrsReceipts;
        int llngResult;

		try
		{
			lrefMaps = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_MediatorAccountingMap)); 

			lrsReceipts = lrefMaps.SelectByMembers(pdb, new int[] {I.ENTRYYEAR}, new java.lang.Object[] {plngYear},
					new int[] {-I.ENTRYNUMBER});
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		llngResult = 1;
		try
		{
			if ( lrsReceipts.next() )
			{
				if( lrsReceipts.getObject(2 + I.ENTRYNUMBER) != null )
					llngResult = lrsReceipts.getInt(2 + I.ENTRYNUMBER) + 1;
			}
		}
		catch (Throwable e)
		{
			try { lrsReceipts.close(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsReceipts.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return llngResult;
	}
}
