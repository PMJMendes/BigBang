package com.premiumminds.BigBang.Jewel.Listings;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

import org.apache.ecs.GenericElement;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Operations.Receipt.Payment;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class ReceiptHistoryPaymentAcct
	extends ReceiptListingsBase
{
	public static GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		Receipt[] larrAux, larrAux2;
		HashMap<UUID, ArrayList<Receipt>> larrMap;
		ArrayList<Receipt> larrTmp;
		int i;
		UUID lidCompany;
		GenericElement[] larrResult;

		larrAux = getHistoryForOperation(parrParams);

		larrMap = new HashMap<UUID, ArrayList<Receipt>>();
		for ( i = 0; i < larrAux.length; i++ )
		{
			lidCompany = larrAux[i].getAbsolutePolicy().GetCompany().getKey();

			if ( larrMap.get(lidCompany) == null )
				larrMap.put(lidCompany, new ArrayList<Receipt>());
			larrMap.get(lidCompany).add(larrAux[i]);
		}

		larrResult = new GenericElement[larrMap.size() + 2];

		larrResult[0] = buildHeaderSection("Folha de Caixa", larrAux, larrMap.size());

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

		larrResult[i] = buildSummarySection(larrAux);

		return larrResult;
	}

	protected static Receipt[] getHistoryForOperation(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<Receipt> larrAux;
		IEntity lrefReceipts, lrefLogs;
		MasterDB ldb;
		ResultSet lrsReceipts;

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

		if ( parrParams[0] != null )
			lstrSQL.append(" AND DATEDIFF(day, [Timestamp], '" + parrParams[0] + "') = 0");

		lstrSQL.append(")");

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

	protected static Table buildSummarySection(Receipt[] parrReceipts)
		throws BigBangJewelException
	{
		HashMap<String, BigDecimal> larrMap;
		Payment lrefPayment;
		boolean b;
		int i, j;
		String lstrType;
		BigDecimal ldblAux, ldblAux2;
		Table ltbl;
		TR[] larrRows;

		larrMap = new HashMap<String, BigDecimal>();

		try
		{
			b = false;
			for ( i = 0; i < parrReceipts.length; i++ )
			{
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

					ldblAux = larrMap.get(lstrType);
					if ( ldblAux == null )
						ldblAux = new BigDecimal(0);

					ldblAux2 = lrefPayment.marrData[j].mdblValue;
					if ( ldblAux2 == null )
						ldblAux2 = (BigDecimal)parrReceipts[i].getAt(3);

					ldblAux = ldblAux.add(ldblAux2);

					larrMap.put(lstrType, ldblAux);
				}
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrRows = new TR[larrMap.size() + (b ? 2 : 1)];

		if ( larrMap.size() == 0 )
			larrRows[0] = ReportBuilder.constructDualHeaderRowCell("Informação sobre tipos de pagamento não disponível.");
		else
			larrRows[0] = ReportBuilder.constructDualHeaderRowCell("Sumário de Tipos de Pagamento");

		i = 1;
		for ( String lstr: larrMap.keySet() )
		{
			larrRows[i] = ReportBuilder.constructDualRow(lstr, larrMap.get(lstr), TypeDefGUIDs.T_Decimal);
			i++;
		}

		if ( b )
			larrRows[i] = ReportBuilder.constructDualHeaderRowCell("(Informação incompleta.)");

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	private static void sSortSubGroup(Receipt[] parrIn)
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
