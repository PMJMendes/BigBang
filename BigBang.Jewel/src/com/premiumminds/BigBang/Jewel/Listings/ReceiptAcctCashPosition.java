package com.premiumminds.BigBang.Jewel.Listings;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.apache.ecs.GenericElement;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.R;
import Jewel.Petri.Interfaces.ILog;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.Payment;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class ReceiptAcctCashPosition
	extends ReceiptListingsBase
{
	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		Receipt[] larrAux;
		HashMap<UUID, ArrayList<Receipt>> larrMap;
		UUID lidCompany;
		GenericElement[] larrResult;
		int i;

		if ( (parrParams[0] == null) || "".equals(parrParams[0]) )
			parrParams[0] = new Timestamp(new java.util.Date().getTime()).toString().substring(0, 10);

		larrAux = getPendingForOperation(parrParams);

		larrMap = new HashMap<UUID, ArrayList<Receipt>>();
		for ( i = 0; i < larrAux.length; i++ )
		{
			lidCompany = larrAux[i].getAbsolutePolicy().GetCompany().getKey();

			if ( larrMap.get(lidCompany) == null )
				larrMap.put(lidCompany, new ArrayList<Receipt>());
			larrMap.get(lidCompany).add(larrAux[i]);
		}

		larrResult = new GenericElement[1];

		larrResult[0] = buildOuterTable(larrMap);

		return larrResult;
	}

	protected Receipt[] getPendingForOperation(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<Receipt> larrAux;
		IEntity lrefReceipts, lrefSteps;
		MasterDB ldb;
		ResultSet lrsReceipts;

		try
		{
			lrefReceipts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Receipt));
			lrefSteps = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNStep));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (" +
					lrefReceipts.SQLForSelectAll() + ") [AuxRecs] WHERE [Process] IN (SELECT [Process] FROM (" +
					lrefSteps.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Step, Jewel.Petri.Constants.FKLevel_In_Step},
							new java.lang.Object[] {Constants.OPID_Receipt_Payment, Constants.UrgID_Pending}, null) +
					" UNION ALL " +
					lrefSteps.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Step, Jewel.Petri.Constants.FKLevel_In_Step},
							new java.lang.Object[] {Constants.OPID_Receipt_InsurerAccounting, Constants.UrgID_Pending}, null) +
					") [AuxSteps])");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( parrParams[0] != null )
			filterByCompany(lstrSQL, UUID.fromString(parrParams[0]));

		larrAux = new ArrayList<Receipt>();

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
			lrsReceipts = ldb.OpenRecordset(lstrSQL.toString());
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsReceipts.next() )
				larrAux.add(Receipt.GetInstance(Engine.getCurrentNameSpace(), lrsReceipts));
		}
		catch (Throwable e)
		{
			try { lrsReceipts.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsReceipts.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
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

		return larrAux.toArray(new Receipt[larrAux.size()]);
	}

	protected Table buildOuterTable(HashMap<UUID, ArrayList<Receipt>> parrMap)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;
		TD lcell;

		larrRows = new TR[3];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell("Posição de Tesouraria");

		larrRows[1] = ReportBuilder.constructDualRow("Data", new Timestamp(new java.util.Date().getTime()), TypeDefGUIDs.T_Date, false);

		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInnerTable(parrMap));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[2] = ReportBuilder.buildRow(new TD[] {lcell});

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected Table buildInnerTable(HashMap<UUID, ArrayList<Receipt>> parrMap)
		throws BigBangJewelException
	{
		R<BigDecimal> ldblPending;
		R<BigDecimal> ldblPaid;
		R<BigDecimal> ldblComissions;
		int i;
		Table ltbl;
		TR[] larrRows;

		ldblPending = new R<BigDecimal>(BigDecimal.ZERO);
		ldblPaid = new R<BigDecimal>(BigDecimal.ZERO);
		ldblComissions = new R<BigDecimal>(BigDecimal.ZERO);

		larrRows = new TR[parrMap.size() + 2];

		larrRows[0] = ReportBuilder.buildRow(buildInnerHeaderRow());
		ReportBuilder.styleRow(larrRows[0], true);

		i = 1;
		for ( UUID lid: parrMap.keySet() )
		{
			larrRows[i] = ReportBuilder.buildRow(buildInnerRow(lid, parrMap.get(lid), ldblPending, ldblPaid, ldblComissions));
			ReportBuilder.styleRow(larrRows[i], false);
		}

		larrRows[i] = ReportBuilder.buildRow(buildInnerFooterRow(ldblPending.get(), ldblPaid.get(), ldblComissions.get()));
		ReportBuilder.styleRow(larrRows[i], false);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[5];

		larrCells[0] = ReportBuilder.buildHeaderCell("Seguradora");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("Prémios por Cobrar");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Prémios por Entregar");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Comissões");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Líquido");
		ReportBuilder.styleCell(larrCells[4], false, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected TD[] buildInnerRow(UUID pidCompany, ArrayList<Receipt> parrReceipts, R<BigDecimal> pdblPending, R<BigDecimal> pdblPaid,
			R<BigDecimal> pdblComissions)
		throws BigBangJewelException
	{
		Company lobjComp;
		BigDecimal ldblPending;
		BigDecimal ldblPaid;
		BigDecimal ldblDirect;
		BigDecimal ldblCommissions;
		ILog lobjLog;
		Payment lobjPayment;
		int i;
		TD[] larrCells;

		lobjComp = Company.GetInstance(Engine.getCurrentNameSpace(), pidCompany);

		ldblPending = BigDecimal.ZERO;
		ldblPaid = BigDecimal.ZERO;
		ldblCommissions = BigDecimal.ZERO;

		for ( Receipt lRec : parrReceipts )
		{
			lobjLog = lRec.getPaymentLog();
			if ( lobjLog == null )
			{
				ldblPending = ldblPending.add((BigDecimal)lRec.getAt(Receipt.I.TOTALPREMIUM));
			}
			else
			{
				ldblDirect = BigDecimal.ZERO;
				try
				{
					lobjPayment = (Payment)lobjLog.GetOperationData();
					for ( i = 0; i < lobjPayment.marrData.length; i++ )
					{
						if ( Constants.PayID_DirectToInsurer.equals(lobjPayment.marrData[i].midPaymentType) ||
								Constants.PayID_FromTheInsurer.equals(lobjPayment.marrData[i].midPaymentType) ||
								Constants.PayID_DirectCheque.equals(lobjPayment.marrData[i].midPaymentType) )
							ldblDirect = ldblDirect.add(lobjPayment.marrData[i].mdblValue);
					}
				}
				catch (Throwable e)
				{
				}
				ldblPaid = ldblPaid.add(((BigDecimal)lRec.getAt(Receipt.I.TOTALPREMIUM)).subtract(ldblDirect));
			}
			ldblCommissions = ldblCommissions.add((lRec.getAt(Receipt.I.COMMISSIONS) == null ? BigDecimal.ZERO :
					(BigDecimal)lRec.getAt(Receipt.I.COMMISSIONS)));
		}

		larrCells = new TD[5];

		larrCells[0] = ReportBuilder.buildCell(lobjComp.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(ldblPending, TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(ldblPaid, TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(ldblCommissions, TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(ldblPending.add(ldblPaid).subtract(ldblCommissions), TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[4], true, true);

		setWidths(larrCells);

		pdblPending.set(pdblPending.get().add(ldblPending));
		pdblPaid.set(pdblPending.get().add(ldblPaid));
		pdblComissions.set(pdblPending.get().add(ldblCommissions));

		return larrCells;
	}

	protected TD[] buildInnerFooterRow(BigDecimal pdblPending, BigDecimal pdblPaid, BigDecimal pdblComissions)
	{
		TD[] larrCells;

		larrCells = new TD[5];

		larrCells[0] = ReportBuilder.buildCell("Totais", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(pdblPending, TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(pdblPaid, TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(pdblComissions, TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(pdblPending.add(pdblPaid).subtract(pdblComissions), TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[4], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected void setWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth(150);
		parrCells[ 1].setWidth( 80);
		parrCells[ 2].setWidth( 80);
		parrCells[ 3].setWidth( 80);
		parrCells[ 4].setWidth( 80);
	}
}
