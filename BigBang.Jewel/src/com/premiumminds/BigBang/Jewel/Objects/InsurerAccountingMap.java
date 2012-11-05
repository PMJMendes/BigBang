package com.premiumminds.BigBang.Jewel.Objects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.JewelEngineException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DocInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.General.ManageInsurers;
import com.premiumminds.BigBang.Jewel.Reports.InsurerAccountingReport;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionDetailBase;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionMapBase;

public class InsurerAccountingMap
	extends TransactionMapBase
{
	public static class I
		extends TransactionMapBase.I
	{
		public static int EXTRATEXT    = 3;
		public static int EXTRAVALUE   = 4;
		public static int ISCOMMISSION = 5;
		public static int HASTAX       = 6;
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

    private transient DocOps mobjDoc;

	public void Initialize()
		throws JewelEngineException
	{
		super.Initialize();
		mobjDoc = null;
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

		try
		{
			lopMI.Execute(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public DocOps generateDocOp(SQLServer pdb)
		throws BigBangJewelException
	{
		InsurerAccountingReport lrepIA;
		FileXfer lobjFile;
		DocumentData lobjDoc;
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

		lobjDoc = new DocumentData();
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
		lobjDoc.marrInfo[4].mstrType = "Imposto de Selo";
		lobjDoc.marrInfo[4].mstrValue = String.format("%,.2f", lrepIA.mdblTax);
		lobjDoc.marrInfo[5] = new DocInfoData();
		lobjDoc.marrInfo[5].mstrType = "Total a Liquidar";
		lobjDoc.marrInfo[5].mstrValue = String.format("%,.2f", lrepIA.mdblTotal);
		if ( lrepIA.mstrExtraText != null )
		{
			lobjDoc.marrInfo[6] = new DocInfoData();
			lobjDoc.marrInfo[6].mstrType = lrepIA.mstrExtraText;
			lobjDoc.marrInfo[6].mstrValue = String.format("%,.2f", lrepIA.mdblExtraValue);
		}

		lobjResult = new DocOps();
		lobjResult.marrCreate = new DocumentData[]{lobjDoc};

		mobjDoc = lobjResult;

		return lobjResult;
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
		BigDecimal ldblPreTax, ldblTax, ldblTotal;
		boolean lbSubtract;
		int i;

		ldblTotalPremiums = BigDecimal.ZERO;
		ldblDirectPremiums = BigDecimal.ZERO;
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
		ldblPreTax = ldblPayablePremiums.subtract(ldblTotalComms);
		if ( lbSubtract )
			ldblPreTax = ldblPreTax.subtract(ldblExtraValue);
		ldblTax = ldblTaxableComms.multiply((new BigDecimal(2.0/102.0))).setScale(2, RoundingMode.HALF_UP);
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

		larrRows[8] = ReportBuilder.constructDualRow("Imposto de Selo", ldblTax, TypeDefGUIDs.T_Decimal, false);

		larrRows[9] = ReportBuilder.constructDualRow("Saldo Total", ldblTotal, TypeDefGUIDs.T_Decimal, false);

		return larrRows;
	}
}
