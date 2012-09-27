package com.premiumminds.BigBang.Jewel.Objects;

import java.math.BigDecimal;
import java.sql.ResultSet;
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
import com.premiumminds.BigBang.Jewel.Operations.General.ManageMediators;
import com.premiumminds.BigBang.Jewel.Reports.MediatorAccountingReport;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionMapBase;

public class MediatorAccountingMap
	extends TransactionMapBase
{
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

		ldblTotal = new BigDecimal(0.0);

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
}
