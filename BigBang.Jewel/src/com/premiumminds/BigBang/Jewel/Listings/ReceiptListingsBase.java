package com.premiumminds.BigBang.Jewel.Listings;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Interfaces.ILog;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.Payment;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class ReceiptListingsBase
{
	protected Table buildHeaderSection(String pstrHeader, Receipt[] parrReceipts, int plngMapSize)
	{
		BigDecimal ldblTotal;
		BigDecimal ldblTotalCom;
		BigDecimal ldblTotalRetro;
		int i;
		Table ltbl;
		TR[] larrRows;

		ldblTotal = new BigDecimal(0);
		ldblTotalCom = new BigDecimal(0);
		ldblTotalRetro = new BigDecimal(0);
		for ( i = 0; i < parrReceipts.length; i++ )
		{
			ldblTotal = ldblTotal.add((parrReceipts[i].getAt(Receipt.I.TOTALPREMIUM) == null ? new BigDecimal(0) :
					(BigDecimal)parrReceipts[i].getAt(Receipt.I.TOTALPREMIUM)));
			ldblTotalCom = ldblTotalCom.add((parrReceipts[i].getAt(Receipt.I.COMMISSIONS) == null ? new BigDecimal(0) :
					(BigDecimal)parrReceipts[i].getAt(Receipt.I.COMMISSIONS)));
			ldblTotalRetro = ldblTotalRetro.add((parrReceipts[i].getAt(Receipt.I.RETROCESSIONS) == null ? new BigDecimal(0) :
					(BigDecimal)parrReceipts[i].getAt(Receipt.I.RETROCESSIONS)));
		}

		larrRows = new TR[6];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		larrRows[1] = ReportBuilder.constructDualRow("Nº de Gestores", plngMapSize, TypeDefGUIDs.T_Integer, false);

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Recibos", parrReceipts.length, TypeDefGUIDs.T_Integer, false);

		larrRows[3] = ReportBuilder.constructDualRow("Total de Prémios", ldblTotal, TypeDefGUIDs.T_Decimal, false);

		larrRows[4] = ReportBuilder.constructDualRow("Total de Comissões", ldblTotalCom, TypeDefGUIDs.T_Decimal, false);

		larrRows[5] = ReportBuilder.constructDualRow("Total de Retrocessões", ldblTotalRetro, TypeDefGUIDs.T_Decimal, false);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected Table buildDataSection(String pstrHeader, ArrayList<Receipt> parrReceipts)
		throws BigBangJewelException
	{
		return buildDataSection(pstrHeader, parrReceipts.toArray(new Receipt[parrReceipts.size()]));
	}

	protected Table buildDataSection(String pstrHeader, Receipt[] parrReceipts)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;

		larrRows = buildDataTable(pstrHeader, parrReceipts);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected TR[] buildDataTable(String pstrHeader, Receipt[] parrReceipts)
		throws BigBangJewelException
	{
		BigDecimal ldblTotal;
		BigDecimal ldblTotalCom;
		BigDecimal ldblTotalRetro;
		int i;
		TR[] larrRows;
		TD lcell;

		ldblTotal = new BigDecimal(0);
		ldblTotalCom = new BigDecimal(0);
		ldblTotalRetro = new BigDecimal(0);
		for ( i = 0; i < parrReceipts.length; i++ )
		{
			ldblTotal = ldblTotal.add((parrReceipts[i].getAt(Receipt.I.TOTALPREMIUM) == null ? new BigDecimal(0) :
					(BigDecimal)parrReceipts[i].getAt(Receipt.I.TOTALPREMIUM)));
			ldblTotalCom = ldblTotalCom.add((parrReceipts[i].getAt(Receipt.I.COMMISSIONS) == null ? new BigDecimal(0) :
					(BigDecimal)parrReceipts[i].getAt(Receipt.I.COMMISSIONS)));
			ldblTotalRetro = ldblTotalRetro.add((parrReceipts[i].getAt(Receipt.I.RETROCESSIONS) == null ? new BigDecimal(0) :
					(BigDecimal)parrReceipts[i].getAt(Receipt.I.RETROCESSIONS)));
		}

		larrRows = new TR[6];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInner(parrReceipts));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[1] = ReportBuilder.buildRow(new TD[] {lcell});

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Recibos", parrReceipts.length, TypeDefGUIDs.T_Integer, false);

		larrRows[3] = ReportBuilder.constructDualRow("Total de Prémios", ldblTotal, TypeDefGUIDs.T_Decimal, false);

		larrRows[4] = ReportBuilder.constructDualRow("Total de Comissões", ldblTotalCom, TypeDefGUIDs.T_Decimal, false);

		larrRows[5] = ReportBuilder.constructDualRow("Total de Retrocessões", ldblTotalRetro, TypeDefGUIDs.T_Decimal, false);

		return larrRows;
	}

	protected Table buildInner(Receipt[] parrReceipts)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;
		int i;

		larrRows = new TR[parrReceipts.length + 1];

		larrRows[0] = ReportBuilder.buildRow(buildInnerHeaderRow());
		ReportBuilder.styleRow(larrRows[0], true);

		for ( i = 1; i <= parrReceipts.length; i++ )
		{
			larrRows[i] = ReportBuilder.buildRow(buildRow(parrReceipts[i - 1]));
			ReportBuilder.styleRow(larrRows[i], false);
		}

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[16];

		larrCells[0] = ReportBuilder.buildHeaderCell("Recibo");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("Tipo");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Cliente");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Apólice");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Companhia");
		ReportBuilder.styleCell(larrCells[4], false, true);

		larrCells[5] = ReportBuilder.buildHeaderCell("Ramo");
		ReportBuilder.styleCell(larrCells[5], false, true);

		larrCells[6] = ReportBuilder.buildHeaderCell("Descrição");
		ReportBuilder.styleCell(larrCells[6], false, true);

		larrCells[7] = ReportBuilder.buildHeaderCell("Prémio");
		ReportBuilder.styleCell(larrCells[7], false, true);

		larrCells[8] = ReportBuilder.buildHeaderCell("Comissão");
		ReportBuilder.styleCell(larrCells[8], false, true);

		larrCells[9] = ReportBuilder.buildHeaderCell("Retrocessão");
		ReportBuilder.styleCell(larrCells[9], false, true);

		larrCells[10] = ReportBuilder.buildHeaderCell("Emissão");
		ReportBuilder.styleCell(larrCells[10], false, true);

		larrCells[11] = ReportBuilder.buildHeaderCell("Vencimento");
		ReportBuilder.styleCell(larrCells[11], false, true);

		larrCells[12] = ReportBuilder.buildHeaderCell("Até");
		ReportBuilder.styleCell(larrCells[12], false, true);

		larrCells[13] = ReportBuilder.buildHeaderCell("Data Limite");
		ReportBuilder.styleCell(larrCells[13], false, true);

		larrCells[14] = ReportBuilder.buildHeaderCell("Data Cobrança");
		ReportBuilder.styleCell(larrCells[14], false, true);

		larrCells[15] = ReportBuilder.buildHeaderCell("Meios");
		ReportBuilder.styleCell(larrCells[15], false, true);

		return larrCells;
	}

	protected TD[] buildRow(Receipt pobjReceipt)
		throws BigBangJewelException
	{
		Policy lobjPolicy;
		SubPolicy lobjSubPolicy;
		Client lobjClient;
		ILog lobjLog;
		TD[] larrCells;

		lobjPolicy = pobjReceipt.getDirectPolicy();

		if ( lobjPolicy == null )
		{
			lobjPolicy = pobjReceipt.getAbsolutePolicy();
			lobjSubPolicy = pobjReceipt.getSubPolicy();
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjSubPolicy.getAt(2));
		}
		else
		{
			lobjClient = lobjPolicy.GetClient();
			lobjSubPolicy = null;
		}

		lobjLog = pobjReceipt.getPaymentLog();

		larrCells = new TD[16];

		larrCells[0] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.NUMBER), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(pobjReceipt.getReceiptType(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(lobjClient.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(lobjSubPolicy == null ? lobjPolicy.getLabel() : lobjSubPolicy.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(lobjPolicy.GetCompany().getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell(lobjPolicy.GetSubLine().getDescription(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.DESCRIPTION), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[6], true, true);

		larrCells[7] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.TOTALPREMIUM), TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[7], true, true);
		larrCells[7].setAlign("right");

		larrCells[8] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.COMMISSIONS), TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[8], true, true);
		larrCells[8].setAlign("right");

		larrCells[9] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.RETROCESSIONS), TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[9], true, true);
		larrCells[9].setAlign("right");

		larrCells[10] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.ISSUEDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[10], true, true);

		larrCells[11] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.MATURITYDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[11], true, true);

		larrCells[12] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.ENDDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[12], true, true);

		larrCells[13] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.DUEDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[13], true, true);

		larrCells[14] = ReportBuilder.buildCell((lobjLog == null ? null : lobjLog.GetTimestamp()), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[14], true, true);

		larrCells[15] = ReportBuilder.buildCell((lobjLog == null ? null : getMeans(lobjLog)), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[15], true, true);

		return larrCells;
	}

	protected String getMeans(ILog pobjLog)
		throws BigBangJewelException
	{
		Payment lobjPayment;
		StringBuilder lstrAux;
		int i;

		try
		{
			lobjPayment = (Payment)pobjLog.GetOperationData();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( lobjPayment == null )
			return "?";
		else
		{
			lstrAux = new StringBuilder();
			for ( i = 0; i < lobjPayment.marrData.length; i++ )
			{
				try
				{
					lstrAux.append(Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
							Constants.ObjID_PaymentType), lobjPayment.marrData[i].midPaymentType).getAt(1));
				}
				catch (Throwable e)
				{
					lstrAux.append("*");
				}
			}
			return lstrAux.toString();
		}
	}

	protected void filterByClient(StringBuilder lstrSQL, UUID pidClient)
		throws BigBangJewelException
	{
		IEntity lrefProcesses;
		IEntity lrefPolicies;
		IEntity lrefSubPolicies;

		try
		{
			lrefProcesses = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNProcess));
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));

			lstrSQL.append(" AND ([Process] IN (SELECT [PK] FROM(")
					.append(lrefProcesses.SQLForSelectAll()).append(") [AuxProcs] WHERE [Parent] IN (SELECT [Process] FROM (")
					.append(lrefPolicies.SQLForSelectByMembers(new int[] {17}, new java.lang.Object[] {pidClient}, null))
					.append(") [AuxPols]) OR [Parent] IN (SELECT [Process] FROM (")
					.append(lrefSubPolicies.SQLForSelectByMembers(new int[] {2}, new java.lang.Object[] {pidClient}, null))
					.append(") [AuxSPols])))");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
