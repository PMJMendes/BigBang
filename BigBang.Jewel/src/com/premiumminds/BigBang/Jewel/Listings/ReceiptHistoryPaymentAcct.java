package com.premiumminds.BigBang.Jewel.Listings;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.ILog;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.Payment;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class ReceiptHistoryPaymentAcct
	extends ReceiptListingsBase
{
	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		Receipt[] larrAux;
		HashMap<UUID, ArrayList<Receipt>> larrMap;
		UUID lidCompany;
		GenericElement[] larrResult;
		ArrayList<Receipt> larrTmp;
		Receipt[] larrAux2;
		int i;

		if ( (parrParams[0] == null) || "".equals(parrParams[0]) )
			parrParams[0] = new Timestamp(new java.util.Date().getTime()).toString().substring(0, 10);

		larrAux = getHistoryForOperation(parrParams);

		larrMap = new HashMap<UUID, ArrayList<Receipt>>();
		for ( i = 0; i < larrAux.length; i++ )
		{
			lidCompany = larrAux[i].getAbsolutePolicy().GetCompany().getKey();

			if ( larrMap.get(lidCompany) == null )
				larrMap.put(lidCompany, new ArrayList<Receipt>());
			larrMap.get(lidCompany).add(larrAux[i]);
		}

		larrResult = new GenericElement[larrMap.size() + 1];

		larrResult[0] = buildSummarySection(parrParams[0], larrAux, larrMap.size());

		i = 1;
		for ( UUID lid: larrMap.keySet() )
		{
			larrTmp = larrMap.get(lid);
			larrAux2 = larrTmp.toArray(new Receipt[larrTmp.size()]);
			sSortSubGroup(larrAux2);
			try
			{
				larrResult[i] = buildDataSection(Company.GetInstance(Engine.getCurrentNameSpace(), lid).getLabel(),
						larrAux2);
			}
			catch (BigBangJewelException e)
			{
				throw e;
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
			i++;
		}

		return larrResult;
	}

	protected Receipt[] getHistoryForOperation(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<Receipt> larrAux;
		IEntity lrefReceipts, lrefLogs;
		MasterDB ldb;
		ResultSet lrsReceipts;
		Receipt lobjAux;

		larrAux = new ArrayList<Receipt>();

		try
		{
			lrefReceipts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Receipt));
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (" +
					lrefReceipts.SQLForSelectAll() + ") [AuxRecs] WHERE [Process] IN (SELECT [Process] FROM(" + 
					lrefLogs.SQLForSelectByMembers(new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
					new java.lang.Object[] {Constants.OPID_Receipt_Payment, false}, null) + ") [AuxLogs] WHERE 1=1");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		lstrSQL.append(" AND DATEDIFF(day, [Timestamp], '" + parrParams[0] + "') = 0)");

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
			{
				lobjAux = Receipt.GetInstance(Engine.getCurrentNameSpace(), lrsReceipts);
				if ( !lobjAux.isInternal() )
					larrAux.add(lobjAux);
			}
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

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[14];

		larrCells[0] = ReportBuilder.buildHeaderCell("Recibo");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("Tipo");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Cliente");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Apólice");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Ramo");
		ReportBuilder.styleCell(larrCells[4], false, true);

		larrCells[5] = ReportBuilder.buildHeaderCell("Descrição");
		ReportBuilder.styleCell(larrCells[5], false, true);

		larrCells[6] = ReportBuilder.buildHeaderCell("Prémio");
		ReportBuilder.styleCell(larrCells[6], false, true);

		larrCells[7] = ReportBuilder.buildHeaderCell("Comissão");
		ReportBuilder.styleCell(larrCells[7], false, true);

		larrCells[8] = ReportBuilder.buildHeaderCell("Retrocessão");
		ReportBuilder.styleCell(larrCells[8], false, true);

		larrCells[9] = ReportBuilder.buildHeaderCell("Emissão");
		ReportBuilder.styleCell(larrCells[9], false, true);

		larrCells[10] = ReportBuilder.buildHeaderCell("Vencimento");
		ReportBuilder.styleCell(larrCells[10], false, true);

		larrCells[11] = ReportBuilder.buildHeaderCell("Até");
		ReportBuilder.styleCell(larrCells[11], false, true);

		larrCells[12] = ReportBuilder.buildHeaderCell("Data Limite");
		ReportBuilder.styleCell(larrCells[12], false, true);

		larrCells[13] = ReportBuilder.buildHeaderCell("Meios");
		ReportBuilder.styleCell(larrCells[13], false, true);

		setWidths(larrCells);

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

		larrCells = new TD[14];

		larrCells[0] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.NUMBER), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(pobjReceipt.getReceiptType(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(lobjClient.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(lobjSubPolicy == null ? lobjPolicy.getLabel() : lobjSubPolicy.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(lobjPolicy.GetSubLine().getDescription(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.DESCRIPTION), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.TOTALPREMIUM), TypeDefGUIDs.T_Decimal, true);
		ReportBuilder.styleCell(larrCells[6], true, true);

		larrCells[7] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.COMMISSIONS), TypeDefGUIDs.T_Decimal, true);
		ReportBuilder.styleCell(larrCells[7], true, true);

		larrCells[8] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.RETROCESSIONS), TypeDefGUIDs.T_Decimal, true);
		ReportBuilder.styleCell(larrCells[8], true, true);

		larrCells[9] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.ISSUEDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[9], true, true);

		larrCells[10] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.MATURITYDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[10], true, true);

		larrCells[11] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.ENDDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[11], true, true);

		larrCells[12] = ReportBuilder.buildCell(pobjReceipt.getAt(Receipt.I.DUEDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[12], true, true);

		larrCells[13] = ReportBuilder.buildCell((lobjLog == null ? null : getMeans(lobjLog)), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[13], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected void setWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth( 80);
		parrCells[ 1].setWidth( 20);
		parrCells[ 2].setWidth(300);
		parrCells[ 3].setWidth(110);
		parrCells[ 4].setWidth(140);
		parrCells[ 5].setWidth(100);
		parrCells[ 6].setWidth( 90);
		parrCells[ 7].setWidth( 90);
		parrCells[ 8].setWidth( 90);
		parrCells[ 9].setWidth( 90);
		parrCells[10].setWidth( 90);
		parrCells[11].setWidth( 90);
		parrCells[12].setWidth( 90);
		parrCells[13].setWidth( 50);
	}

	protected Table buildSummarySection(String pstrHeader, Receipt[] parrReceipts, int plngMapSize)
		throws BigBangJewelException
	{
		HashMap<String, BigDecimal> larrPlusMap;
		HashMap<String, BigDecimal> larrMinusMap;
		BigDecimal ldblTotal;
		BigDecimal ldblTotalCom;
		BigDecimal ldblTotalRetro;
		boolean b;
		int i, j;
		Payment lrefPayment;
		String lstrType;
		BigDecimal ldblAux, ldblAux2;
		Table ltbl;
		TR[] larrRows;

		larrPlusMap = new HashMap<String, BigDecimal>();
		larrMinusMap = new HashMap<String, BigDecimal>();
		ldblTotal = BigDecimal.ZERO;
		ldblTotalCom = BigDecimal.ZERO;
		ldblTotalRetro = BigDecimal.ZERO;
		b = false;

		try
		{
			for ( i = 0; i < parrReceipts.length; i++ )
			{
				ldblTotal = ldblTotal.add((parrReceipts[i].getAt(Receipt.I.TOTALPREMIUM) == null ? BigDecimal.ZERO :
						(BigDecimal)parrReceipts[i].getAt(Receipt.I.TOTALPREMIUM)));
				ldblTotalCom = ldblTotalCom.add((parrReceipts[i].getAt(Receipt.I.COMMISSIONS) == null ? BigDecimal.ZERO :
						(BigDecimal)parrReceipts[i].getAt(Receipt.I.COMMISSIONS)));
				ldblTotalRetro = ldblTotalRetro.add((parrReceipts[i].getAt(Receipt.I.RETROCESSIONS) == null ? BigDecimal.ZERO :
						(BigDecimal)parrReceipts[i].getAt(Receipt.I.RETROCESSIONS)));

				lrefPayment = (Payment)parrReceipts[i].getPaymentLog().GetOperationData();

				if ( (lrefPayment == null) || (lrefPayment.marrData == null) || (lrefPayment.marrData.length == 0) )
				{
					b = true;
					continue;
				}

				for ( j = 0; j < lrefPayment.marrData.length; j++ )
				{
					lstrType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PaymentType),
							lrefPayment.marrData[j].midPaymentType).getLabel();

					ldblAux2 = lrefPayment.marrData[j].mdblValue;
					if ( ldblAux2 == null )
						ldblAux2 = (BigDecimal)parrReceipts[i].getAt(3);

					ldblAux = (ldblAux2.signum() >= 0 ? larrPlusMap.get(lstrType) : larrMinusMap.get(lstrType));
					if ( ldblAux == null )
						ldblAux = BigDecimal.ZERO;

					ldblAux = ldblAux.add(ldblAux2);

					(ldblAux2.signum() >= 0 ? larrPlusMap : larrMinusMap).put(lstrType, ldblAux);
				}
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrRows = new TR[8 + larrPlusMap.size() + larrMinusMap.size() + (b ? 2 : 1)];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell("Folha de Caixa");

		larrRows[1] = ReportBuilder.constructDualRow("Data", Timestamp.valueOf(pstrHeader + " 00:00:00.0"), TypeDefGUIDs.T_Date, false);

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Seguradoras", plngMapSize, TypeDefGUIDs.T_Integer, false);

		larrRows[3] = ReportBuilder.constructDualRow("Nº de Recibos", parrReceipts.length, TypeDefGUIDs.T_Integer, false);

		larrRows[4] = ReportBuilder.constructDualRow("Total de Prémios", ldblTotal, TypeDefGUIDs.T_Decimal, false);

		larrRows[5] = ReportBuilder.constructDualRow("Total de Comissões", ldblTotalCom, TypeDefGUIDs.T_Decimal, false);

		larrRows[6] = ReportBuilder.constructDualRow("Total de Retrocessões", ldblTotalRetro, TypeDefGUIDs.T_Decimal, false);

		if ( (larrPlusMap.size() == 0) && (larrMinusMap.size() == 0) )
			larrRows[7] = ReportBuilder.constructDualHeaderRowCell("Informação sobre tipos de pagamento não disponível.");
		else
		{
			larrRows[7] = ReportBuilder.constructDualHeaderRowCell("Sumário de Tipos de Pagamento - Entradas");
			i = 1;
			for ( String lstr: larrPlusMap.keySet() )
			{
				larrRows[7 + i] = ReportBuilder.constructDualRow(lstr, larrPlusMap.get(lstr), TypeDefGUIDs.T_Decimal, true);
				i++;
			}
			larrRows[7 + i] = ReportBuilder.constructDualHeaderRowCell("Sumário de Tipos de Pagamento - Saídas");
			for ( String lstr: larrMinusMap.keySet() )
			{
				larrRows[8 + i] = ReportBuilder.constructDualRow(lstr, larrMinusMap.get(lstr), TypeDefGUIDs.T_Decimal, true);
				i++;
			}

			if ( b )
				larrRows[8 + i] = ReportBuilder.constructDualHeaderRowCell("(Informação incompleta.)");
		}

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	private void sSortSubGroup(Receipt[] parrIn)
	{
		Arrays.sort(parrIn, new Comparator<Receipt>()
		{
			public int compare(Receipt o1, Receipt o2)
			{
				ObjectBase lobjType1, lobjType2;
				try
				{
					lobjType1 = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
							Constants.ObjID_ReceiptType), (UUID)o1.getAt(Receipt.I.TYPE));
					lobjType2 = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
							Constants.ObjID_ReceiptType), (UUID)o2.getAt(Receipt.I.TYPE));
				}
				catch (Throwable e)
				{
					return 0;
				}
				return lobjType1.getLabel().compareTo(lobjType2.getLabel());
			}
		});
	}
}
