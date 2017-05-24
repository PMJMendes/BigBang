package com.premiumminds.BigBang.Jewel.Objects;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.premiumminds.BigBang.Jewel.Data.DocDataLight;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers;
import com.premiumminds.BigBang.Jewel.Reports.InsurerAccountingReport;
import com.premiumminds.BigBang.Jewel.SysObjects.PHCConnector;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionDetailBase;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionMapBase;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionSetBase;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

public class InsurerAccountingMap
	extends TransactionMapBase
{
	public static class I
		extends TransactionMapBase.I
	{
		public static final int EXTRATEXT    = 3;
		public static final int EXTRAVALUE   = 4;
		public static final int ISCOMMISSION = 5;
		public static final int HASTAX       = 6;
		public static final int ENTRYNUMBER  = 7;
		public static final int ENTRYYEAR    = 8;
	}

    public static InsurerAccountingMap GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (InsurerAccountingMap)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_InsurerAccountingMap), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static InsurerAccountingMap GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (InsurerAccountingMap)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_InsurerAccountingMap), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    private Company mrefCompany;

    private transient DocOps mobjDoc;
    private transient BigDecimal mdblTotal;
    private transient Timestamp mdtToday;
    private transient InsurerAccountingReport lrepIA;

	public void Initialize()
		throws JewelEngineException
	{
		super.Initialize();

		try {
			mrefCompany = Company.GetInstance(Engine.getCurrentNameSpace(), (UUID)getAt(I.OWNER));
		} catch (Throwable e) {
			throw new JewelEngineException(e.getMessage(), e);
		}

		mobjDoc = null;
	}

	public Company getCompany()
	{
		// TODO: Sometimes the company gets "lost" in this class, even though it was previously defined.
		// The reason why this happens must be verified, but for now this workaround will try to 
		// surpass a problem verified in the insurer accounting report.
		if (mrefCompany==null && getAt(I.OWNER)!=null) {
			try {
				mrefCompany = Company.GetInstance(Engine.getCurrentNameSpace(), (UUID)getAt(I.OWNER));
			} catch (Throwable e) {
				return null;
			}
		}
		return mrefCompany;
	}

	public UUID getParentType()
	{
		return Constants.ObjID_InsurerAccountingSet;
	}

	public UUID getSubObjectType()
	{
		return Constants.ObjID_InsurerAccountingDetail;
	}

	public void prep()
		throws BigBangJewelException
	{
		TransactionDetailBase[] larrDetails;
		int i;

		larrDetails = getCurrentDetails();
		for ( i = 0; i < larrDetails.length; i++ )
			((InsurerAccountingDetail)larrDetails[i]).prep();
	}

	public void Settle(SQLServer pdb, UUID pidPrintSet)
		throws BigBangJewelException
	{
		ManageInsurers lopMI;

		super.Settle(pdb, pidPrintSet);

		lopMI = new ManageInsurers(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
		lopMI.mobjDocOps = generateDocOp(pdb);
		lopMI.marrAccounting = getAccountingData(pdb);

		try
		{
			lopMI.Execute(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
		
		// Now, it must create the XML used to send the billing info to PHC, but
		// only in Portugal. The conditional is not perfect, but for the time
		// being is the only way to differentiate.
		if (Utils.getCurrency().equals("€")) {
			try {
				if (mrefCompany==null) {
					mrefCompany = Company.GetInstance(Engine.getCurrentNameSpace(), (UUID)getAt(I.OWNER));
				}
				PHCConnector.createPHCFile(mrefCompany, lrepIA, mdtToday);
			} catch (Throwable e) {
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
	}

	public DocOps generateDocOp(SQLServer pdb)
		throws BigBangJewelException
	{ 
		FileXfer lobjFile;
		DocDataLight lobjDoc;
		DocOps lobjResult;

		if ( mobjDoc != null )
			return mobjDoc;

		lrepIA = new InsurerAccountingReport();
		lrepIA.midMap = getKey();
		lrepIA.mdb = pdb;
		try
		{
			lobjFile = lrepIA.Generate();
		}
		finally
		{
			lrepIA.mdb = null;
		}

		mdblTotal = lrepIA.mdblTotal;
		mdtToday = lrepIA.mdtToday;

		lobjDoc = new DocDataLight();
		lobjDoc.mstrName = new Timestamp(new java.util.Date().getTime()).toString();
		lobjDoc.midOwnerType = Constants.ObjID_Company;
		lobjDoc.midOwnerId = (UUID)getAt(TransactionMapBase.I.OWNER);
		lobjDoc.midDocType = Constants.DocID_InsurerReceipt;
		lobjDoc.mstrText = null;
		lobjDoc.mobjFile = lobjFile.GetVarData();
		lobjDoc.marrInfo = new DocInfoData[lrepIA.mstrExtraText == null ? 6 : 7];
		lobjDoc.marrInfo[0] = new DocInfoData();
		lobjDoc.marrInfo[0].mstrType = "Número de Recibos";
		lobjDoc.marrInfo[0].mstrValue = Integer.toString(lrepIA.mlngCount);
		lobjDoc.marrInfo[1] = new DocInfoData();
		lobjDoc.marrInfo[1].mstrType = "Prémios Entregues";
		lobjDoc.marrInfo[1].mstrValue = String.format("%,.2f", lrepIA.mdblPayables);
		lobjDoc.marrInfo[2] = new DocInfoData();
		lobjDoc.marrInfo[2].mstrType = "Comissões Recebidas";
		lobjDoc.marrInfo[2].mstrValue = String.format("%,.2f", lrepIA.mdblTotalComms);
		lobjDoc.marrInfo[3] = new DocInfoData();
		lobjDoc.marrInfo[3].mstrType = "Saldo";
		lobjDoc.marrInfo[3].mstrValue = String.format("%,.2f", lrepIA.mdblPreTax);
		lobjDoc.marrInfo[4] = new DocInfoData();
		lobjDoc.marrInfo[4].mstrType = "Retenção Fiscal";
		lobjDoc.marrInfo[4].mstrValue = String.format("%,.2f", lrepIA.mdblTax);
		lobjDoc.marrInfo[5] = new DocInfoData();
		lobjDoc.marrInfo[5].mstrType = "Total a Liquidar";
		lobjDoc.marrInfo[5].mstrValue = String.format("%,.2f", lrepIA.mdblTotal);
		if ( lrepIA.mstrExtraText != null )
		{
			if ( lrepIA.mstrExtraText.length() > 50 )
				lrepIA.mstrExtraText = lrepIA.mstrExtraText.substring(0, 50);

			lobjDoc.marrInfo[6] = new DocInfoData();
			lobjDoc.marrInfo[6].mstrType = lrepIA.mstrExtraText;
			lobjDoc.marrInfo[6].mstrValue = String.format("%,.2f", lrepIA.mdblExtraValue);
		}

		lobjResult = new DocOps();
		lobjResult.marrCreate2 = new DocDataLight[] {lobjDoc};

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

		lstrAccount = Company.GetInstance(Engine.getCurrentNameSpace(),
				(UUID)getAt(TransactionMapBase.I.OWNER)).getEffectiveAccount();
		if ( lstrAccount == null )
			return null;

		initAccounting(pdb, mdtToday.getYear() + 1900);

		larrResult = new AccountingData[2];

		larrResult[0] = new AccountingData();
		larrResult[0].mlngNumber = (Integer)getAt(I.ENTRYNUMBER);
		larrResult[0].mdtDate = mdtToday;
		larrResult[0].mdblAccount = new BigDecimal("1204");
		larrResult[0].mdblValue = mdblTotal.abs();
		larrResult[0].mstrSign = (mdblTotal.signum() > 0 ? "C" : "D");
		larrResult[0].mlngBook = 6;
		larrResult[0].mstrSupportDoc = ((Integer)mrefSet.getAt(TransactionSetBase.I.NUMBER)).toString();
		larrResult[0].mstrDesc = "Prestação de Conta Seguradora";
		larrResult[0].midDocType = Constants.ObjID_InsurerAccountingMap;
		larrResult[0].mlngYear = (Integer)getAt(I.ENTRYYEAR);
		larrResult[0].midFile = null;
		larrResult[0].midCostCenter = null;

		larrResult[1] = new AccountingData();
		larrResult[1].mlngNumber = (Integer)getAt(I.ENTRYNUMBER);
		larrResult[1].mdtDate = mdtToday;
		larrResult[1].mdblAccount = new BigDecimal(lstrAccount);
		larrResult[1].mdblValue = mdblTotal.abs();
		larrResult[1].mstrSign = (mdblTotal.signum() > 0 ? "D" : "C");
		larrResult[1].mlngBook = 6;
		larrResult[1].mstrSupportDoc = ((Integer)mrefSet.getAt(TransactionSetBase.I.NUMBER)).toString();
		larrResult[1].mstrDesc = "Prestação de Conta Seguradora";
		larrResult[1].midDocType = Constants.ObjID_InsurerAccountingMap;
		larrResult[1].mlngYear = (Integer)getAt(I.ENTRYYEAR);
		larrResult[1].midFile = null;
		larrResult[1].midCostCenter = null;

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

		larrRows = new TR[l + 11];
		larrRows[0] = larrAux[0];

		larrRows[1] = ReportBuilder.constructDualRow("Seguradora",
				Company.GetInstance(getNameSpace(), (UUID)getAt(TransactionMapBase.I.OWNER)).getLabel(), TypeDefGUIDs.T_String, false);

		for ( i = 1; i < l; i++ )
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

		larrCells = Arrays.copyOf(larrAux, i + 1);

		larrCells[i] = ReportBuilder.buildHeaderCell("Comissão");
		ReportBuilder.styleCell(larrCells[i], false, true);

		return larrCells;
	}

	private TR[] buildExtraRows()
		throws BigBangJewelException
	{
		String lstrExtraText;
		BigDecimal ldblExtraValue;
		BigDecimal ldblTotalPremiums, ldblDirectPremiums, ldblPayablePremiums;
		BigDecimal ldblTotalComms, ldblLifeComms, ldblTaxableComms;
		BigDecimal ldblPreTax, ldblTaxCoeff, ldblTax, ldblTotal;
		boolean lbSubtract;
		int i;

		ldblTotalPremiums = BigDecimal.ZERO;
		ldblDirectPremiums = BigDecimal.ZERO;
		ldblTaxableComms = BigDecimal.ZERO;
		ldblTax = BigDecimal.ZERO;
		lbSubtract = false;

		if ( (getAt(I.EXTRATEXT) == null) || (getAt(I.EXTRAVALUE) == null) )
		{
			lstrExtraText = "-";
			ldblExtraValue = BigDecimal.ZERO;
			ldblTotalComms = BigDecimal.ZERO;
			ldblLifeComms = BigDecimal.ZERO;
		}
		else
		{
			lstrExtraText = (String)getAt(I.EXTRATEXT);
			ldblExtraValue = (BigDecimal)getAt(I.EXTRAVALUE);
			if ( (getAt(I.ISCOMMISSION) == null)  || !((Boolean)getAt(I.ISCOMMISSION)) )
			{
				lbSubtract = true;
				ldblTotalComms = BigDecimal.ZERO;
				ldblLifeComms = BigDecimal.ZERO;
			}
			else
			{
				ldblTotalComms = ldblExtraValue;
				if ( (getAt(I.HASTAX) == null) || !((Boolean)getAt(I.HASTAX)) )
					ldblLifeComms = ldblExtraValue;
				else
					ldblLifeComms = BigDecimal.ZERO;
			}
		}

		getDetails();
		for ( i = 0; i < marrDetails.length; i++ )
		{
			ldblTotalPremiums = ldblTotalPremiums.add(((InsurerAccountingDetail)marrDetails[i]).getPremium());
			ldblDirectPremiums = ldblDirectPremiums.add(((InsurerAccountingDetail)marrDetails[i]).getDirectPremium());
			ldblTotalComms = ldblTotalComms.add(((InsurerAccountingDetail)marrDetails[i]).getCommissions());
			ldblLifeComms = ldblLifeComms.add(((InsurerAccountingDetail)marrDetails[i]).getLifeComms());
		}

		ldblPayablePremiums = ldblTotalPremiums.subtract(ldblDirectPremiums);
		ldblTaxableComms = ldblTotalComms.subtract(ldblLifeComms);

		ldblTaxCoeff = Utils.getCommissionsTax();
		ldblTaxCoeff = ldblTaxCoeff.add(new BigDecimal(100)).setScale(10);
		ldblTaxCoeff = Utils.getCommissionsTax().setScale(10).divide(ldblTaxCoeff, RoundingMode.HALF_UP);
		ldblTax = ldblTaxableComms.multiply(ldblTaxCoeff).setScale(2, RoundingMode.HALF_UP);

		ldblPreTax = ldblPayablePremiums.subtract(ldblTotalComms);
		if ( lbSubtract )
			ldblPreTax = ldblPreTax.subtract(ldblExtraValue);
		ldblTotal = ldblPreTax.add(ldblTax);

		TR[] larrRows;

		larrRows = new TR[10];

		larrRows[0] = ReportBuilder.constructDualRow("Total de Prémios", ldblTotalPremiums, TypeDefGUIDs.T_Decimal, false);

		larrRows[1] = ReportBuilder.constructDualRow(" Dos quais, pagamentos directos", ldblDirectPremiums, TypeDefGUIDs.T_Decimal, false);

		larrRows[2] = ReportBuilder.constructDualRow(" E, prémios a entregar", ldblPayablePremiums, TypeDefGUIDs.T_Decimal, false);

		larrRows[3] = ReportBuilder.constructDualRow("Total de Comissões", ldblTotalComms, TypeDefGUIDs.T_Decimal, false);

		larrRows[4] = ReportBuilder.constructDualRow(" Das quais, comissões vida", ldblLifeComms, TypeDefGUIDs.T_Decimal, false);

		larrRows[5] = ReportBuilder.constructDualRow(" E, comissões não vida", ldblTaxableComms, TypeDefGUIDs.T_Decimal, false);

		larrRows[6] = ReportBuilder.constructDualRow(lstrExtraText, ldblExtraValue, TypeDefGUIDs.T_Decimal, false);

		larrRows[7] = ReportBuilder.constructDualRow("Saldo Bruto", ldblPreTax, TypeDefGUIDs.T_Decimal, false);

		larrRows[8] = ReportBuilder.constructDualRow("Retenção Fiscal", ldblTax, TypeDefGUIDs.T_Decimal, false);

		larrRows[9] = ReportBuilder.constructDualRow("Saldo Total", ldblTotal, TypeDefGUIDs.T_Decimal, false);

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
			lrefMaps = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_InsurerAccountingMap)); 

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
