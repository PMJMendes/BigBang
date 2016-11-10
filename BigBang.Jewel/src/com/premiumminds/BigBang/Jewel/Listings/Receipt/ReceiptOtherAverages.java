package com.premiumminds.BigBang.Jewel.Listings.Receipt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import Jewel.Petri.Objects.PNProcess;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Listings.ReceiptListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.CostCenter;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

public class ReceiptOtherAverages
	extends ReceiptListingsBase
{
	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		Receipt[] larrAux;
		HashMap<UUID, ArrayList<Receipt>> larrMap;
		int i;
		UUID lidCenter;
		GenericElement[] larrResult;

		if ( parrParams[1] == null )
			parrParams[1] = new Timestamp(new java.util.Date().getTime()).toString().substring(0, 10);
		if ( parrParams[0] == null )
			parrParams[0] = parrParams[1].substring(0, 5) + "01-01";

		larrAux = getHistoryForOperation(parrParams);

		larrMap = new HashMap<UUID, ArrayList<Receipt>>();
		for ( i = 0; i < larrAux.length; i++ )
		{
			try
			{
				lidCenter = (UUID)UserDecoration.GetByUserID(Engine.getCurrentNameSpace(),
						PNProcess.GetInstance(Engine.getCurrentNameSpace(),
						larrAux[i].getAbsolutePolicy().GetProcessID()).GetManagerID()).getAt(UserDecoration.I.COSTCENTER);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if ( larrMap.get(lidCenter) == null )
				larrMap.put(lidCenter, new ArrayList<Receipt>());
			larrMap.get(lidCenter).add(larrAux[i]);
		}

		getMarkersData();

		larrResult = new GenericElement[1];

		larrResult[0] = buildOuterTable(larrMap, parrParams[0], parrParams[1]);

		return larrResult;
	}

	protected Receipt[] getHistoryForOperation(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<Receipt> larrAux;
		IEntity lrefReceipts;
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrsPolicies;
		UUID lidAgent;

		try
		{
			lrefReceipts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Receipt));
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (")
					.append(lrefReceipts.SQLForSelectAll()).append(") [AuxRecs] ")
					.append("WHERE [Process] IN (SELECT [Process] FROM (")
					.append(lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {false},
							null))
					.append(") [AuxLogs] WHERE 1=1");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( parrParams[0] != null )
			lstrSQL.append(" AND [Timestamp] >= '").append(parrParams[0]).append("'");

		if ( parrParams[1] != null )
			lstrSQL.append(" AND [Timestamp] < DATEADD(d, 1, '").append(parrParams[1]).append("')");
		
		lstrSQL.append(")");

		lidAgent = Utils.getCurrentAgent();
		if ( lidAgent != null )
			filterByAgent(lstrSQL, lidAgent);
		
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
			lrsPolicies = ldb.OpenRecordset(lstrSQL.toString());
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsPolicies.next() ) {
				Receipt rec = Receipt.GetInstance(Engine.getCurrentNameSpace(), lrsPolicies);

				// External receipts are not included in the result.
				if ( parrParams[2].equals("1") ) {
					if (Constants.ProfID_External.equals(rec.getProfile())) {
						continue;
					}
				}
				
				larrAux.add(rec);
			}
		}
		catch (Throwable e)
		{
			try { lrsPolicies.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsPolicies.close();
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

	protected Table buildOuterTable(HashMap<UUID, ArrayList<Receipt>> parrMap, String pstrFrom, String pstrTo)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;
		TD lcell;
		Timestamp ldtFrom;
		Timestamp ldtTo;

		ldtFrom = pstrFrom == null ? null : Timestamp.valueOf(pstrFrom + " 00:00:00.0");
		ldtTo = pstrFrom == null ? null : Timestamp.valueOf(pstrTo + " 00:00:00.0");
		if ( ldtTo != null )
			ldtTo.setTime(ldtTo.getTime() + (1000 * 60 * 60 *24));

		larrRows = new TR[3];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell("Mapa de Prazos de Operações");

		larrRows[1] = ReportBuilder.constructDualRow("Datas ", pstrFrom + " a " + pstrTo, TypeDefGUIDs.T_String, false);

		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInnerTable(parrMap, ldtFrom, ldtTo));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[2] = ReportBuilder.buildRow(new TD[] {lcell});

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected Table buildInnerTable(HashMap<UUID, ArrayList<Receipt>> parrMap, Timestamp pdtFrom, Timestamp pdtTo)
		throws BigBangJewelException
	{
		HashSet<Receipt> larrAll;
		int i;
		Table ltbl;
		TR[] larrRows;

		larrRows = new TR[parrMap.size() + 2];

		larrRows[0] = ReportBuilder.buildRow(buildInnerHeaderRow());
		ReportBuilder.styleRow(larrRows[0], true);

		larrAll = new HashSet<Receipt>();
		i = 1;
		for ( UUID lid: parrMap.keySet() )
		{
			larrAll.addAll(parrMap.get(lid));
			larrRows[i] = ReportBuilder.buildRow(buildInnerRow(lid, parrMap.get(lid), pdtFrom, pdtTo));
			ReportBuilder.styleRow(larrRows[i], false);
			i++;
		}

		larrRows[i] = ReportBuilder.buildRow(buildInnerFooterRow(larrAll, pdtFrom, pdtTo));
		ReportBuilder.styleRow(larrRows[i], false);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[8];

		larrCells[0] = ReportBuilder.buildHeaderCell("Centro Custo");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("Auto Val.");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Val. Manual");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Envio Auto");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Envio Manual");
		ReportBuilder.styleCell(larrCells[4], false, true);

		larrCells[5] = ReportBuilder.buildHeaderCell("Cobrança");
		ReportBuilder.styleCell(larrCells[5], false, true);

		larrCells[6] = ReportBuilder.buildHeaderCell("Prest. Contas");
		ReportBuilder.styleCell(larrCells[6], false, true);

		larrCells[7] = ReportBuilder.buildHeaderCell("Retrocessão");
		ReportBuilder.styleCell(larrCells[7], false, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected TD[] buildInnerRow(UUID pidCenter, ArrayList<Receipt> parrReceipts, Timestamp pdtFrom, Timestamp pdtTo)
		throws BigBangJewelException
	{
		CostCenter lobjCenter;
		int llngCountA;
		int llngDaysA;
		int llngCountV;
		int llngDaysV;
		int llngCountSA;
		int llngDaysSA;
		int llngCountSM;
		int llngDaysSM;
		int llngCountP;
		int llngDaysP;
		int llngCountI;
		int llngDaysI;
		int llngCountM;
		int llngDaysM;
		ReceiptMarkers lobjAux;
		TD[] larrCells;

		lobjCenter = CostCenter.GetInstance(Engine.getCurrentNameSpace(), pidCenter);

		llngCountA = 0;
		llngDaysA = 0;
		llngCountV = 0;
		llngDaysV = 0;
		llngCountSA = 0;
		llngDaysSA = 0;
		llngCountSM = 0;
		llngDaysSM = 0;
		llngCountP = 0;
		llngDaysP = 0;
		llngCountI = 0;
		llngDaysI = 0;
		llngCountM = 0;
		llngDaysM = 0;

		for ( Receipt lobjRec : parrReceipts )
		{
			lobjAux = mmapData.get(lobjRec.getKey());
			if ( (lobjAux != null) )
			{
				if ( (lobjAux.mdtReceived != null) && (lobjAux.mdtAutoVal != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtAutoVal))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtAutoVal))) )
				{
					llngCountA++;
					llngDaysA += (lobjAux.mdtAutoVal.getTime() - lobjAux.mdtReceived.getTime()) / (1000 * 60 * 60 *24);
				}
				if ( (lobjAux.mdtReceived != null) && (lobjAux.mdtValidated != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtValidated))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtValidated))) )
				{
					llngCountV++;
					llngDaysV += (lobjAux.mdtValidated.getTime() - lobjAux.mdtReceived.getTime()) / (1000 * 60 * 60 *24);
				}
				if ( (lobjAux.mdtAutoVal != null) && (lobjAux.mdtSent != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtSent))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtSent))) )
				{
					llngCountSA++;
					llngDaysSA += (lobjAux.mdtSent.getTime() - lobjAux.mdtAutoVal.getTime()) / (1000 * 60 * 60 *24);
				}
				if ( (lobjAux.mdtValidated != null) && (lobjAux.mdtSent != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtSent))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtSent))) )
				{
					llngCountSM++;
					llngDaysSM += (lobjAux.mdtSent.getTime() - lobjAux.mdtValidated.getTime()) / (1000 * 60 * 60 *24);
				}
				if ( (lobjAux.mdtSent != null) && (lobjAux.mdtPayed != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtPayed))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtPayed))) )
				{
					llngCountP++;
					llngDaysP += (lobjAux.mdtPayed.getTime() - lobjAux.mdtSent.getTime()) / (1000 * 60 * 60 *24);
				}
				if ( (lobjAux.mdtPayed != null) && (lobjAux.mdtAccountedI != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtAccountedI))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtAccountedI))) )
				{
					llngCountI++;
					llngDaysI += (lobjAux.mdtAccountedI.getTime() - lobjAux.mdtPayed.getTime()) / (1000 * 60 * 60 *24);
				}
				if ( (lobjAux.mdtPayed != null) && (lobjAux.mdtAccountedM != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtAccountedM))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtAccountedM))) )
				{
					llngCountM++;
					llngDaysM += (lobjAux.mdtAccountedM.getTime() - lobjAux.mdtPayed.getTime()) / (1000 * 60 * 60 *24);
				}
			}
		}

		larrCells = new TD[8];

		larrCells[0] = ReportBuilder.buildCell((String)lobjCenter.getAt(CostCenter.I.DISPLAYNAME), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(llngCountA == 0 ? null : (Integer)(int)(((double)llngDaysA)/((double)llngCountA) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(llngCountV == 0 ? null : (Integer)(int)(((double)llngDaysV)/((double)llngCountV) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(llngCountSA == 0 ? null : (Integer)(int)(((double)llngDaysSA)/((double)llngCountSA) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(llngCountSM == 0 ? null : (Integer)(int)(((double)llngDaysSM)/((double)llngCountSM) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell(llngCountP == 0 ? null : (Integer)(int)(((double)llngDaysP)/((double)llngCountP) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(llngCountI == 0 ? null : (Integer)(int)(((double)llngDaysI)/((double)llngCountI) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[6], true, true);

		larrCells[7] = ReportBuilder.buildCell(llngCountM == 0 ? null : (Integer)(int)(((double)llngDaysM)/((double)llngCountM) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[7], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected TD[] buildInnerFooterRow(HashSet<Receipt> parrReceipts, Timestamp pdtFrom, Timestamp pdtTo)
	{
		int llngCountA;
		int llngDaysA;
		int llngCountV;
		int llngDaysV;
		int llngCountSA;
		int llngDaysSA;
		int llngCountSM;
		int llngDaysSM;
		int llngCountP;
		int llngDaysP;
		int llngCountI;
		int llngDaysI;
		int llngCountM;
		int llngDaysM;
		ReceiptMarkers lobjAux;
		TD[] larrCells;

		llngCountA = 0;
		llngDaysA = 0;
		llngCountV = 0;
		llngDaysV = 0;
		llngCountSA = 0;
		llngDaysSA = 0;
		llngCountSM = 0;
		llngDaysSM = 0;
		llngCountP = 0;
		llngDaysP = 0;
		llngCountI = 0;
		llngDaysI = 0;
		llngCountM = 0;
		llngDaysM = 0;

		for ( Receipt lobjRec : parrReceipts )
		{
			lobjAux = mmapData.get(lobjRec.getKey());
			if ( (lobjAux != null) )
			{
				if ( (lobjAux.mdtReceived != null) && (lobjAux.mdtAutoVal != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtAutoVal))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtAutoVal))) )
				{
					llngCountA++;
					llngDaysA += (lobjAux.mdtAutoVal.getTime() - lobjAux.mdtReceived.getTime()) / (1000 * 60 * 60 *24);
				}
				if ( (lobjAux.mdtReceived != null) && (lobjAux.mdtValidated != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtValidated))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtValidated))) )
				{
					llngCountV++;
					llngDaysV += (lobjAux.mdtValidated.getTime() - lobjAux.mdtReceived.getTime()) / (1000 * 60 * 60 *24);
				}
				if ( (lobjAux.mdtAutoVal != null) && (lobjAux.mdtSent != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtSent))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtSent))) )
				{
					llngCountSA++;
					llngDaysSA += (lobjAux.mdtSent.getTime() - lobjAux.mdtAutoVal.getTime()) / (1000 * 60 * 60 *24);
				}
				if ( (lobjAux.mdtValidated != null) && (lobjAux.mdtSent != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtSent))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtSent))) )
				{
					llngCountSM++;
					llngDaysSM += (lobjAux.mdtSent.getTime() - lobjAux.mdtValidated.getTime()) / (1000 * 60 * 60 *24);
				}
				if ( (lobjAux.mdtSent != null) && (lobjAux.mdtPayed != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtPayed))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtPayed))) )
				{
					llngCountP++;
					llngDaysP += (lobjAux.mdtPayed.getTime() - lobjAux.mdtSent.getTime()) / (1000 * 60 * 60 *24);
				}
				if ( (lobjAux.mdtPayed != null) && (lobjAux.mdtAccountedI != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtAccountedI))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtAccountedI))) )
				{
					llngCountI++;
					llngDaysI += (lobjAux.mdtAccountedI.getTime() - lobjAux.mdtPayed.getTime()) / (1000 * 60 * 60 *24);
				}
				if ( (lobjAux.mdtPayed != null) && (lobjAux.mdtAccountedM != null) && 
						((pdtFrom == null) || (pdtFrom.before(lobjAux.mdtAccountedM))) && ((pdtTo == null) || (pdtTo.after(lobjAux.mdtAccountedM))) )
				{
					llngCountM++;
					llngDaysM += (lobjAux.mdtAccountedM.getTime() - lobjAux.mdtPayed.getTime()) / (1000 * 60 * 60 *24);
				}
			}
		}

		larrCells = new TD[8];

		larrCells[0] = ReportBuilder.buildCell("Totais", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(llngCountA == 0 ? null : (Integer)(int)(((double)llngDaysA)/((double)llngCountA) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(llngCountV == 0 ? null : (Integer)(int)(((double)llngDaysV)/((double)llngCountV) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(llngCountSA == 0 ? null : (Integer)(int)(((double)llngDaysSA)/((double)llngCountSA) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(llngCountSM == 0 ? null : (Integer)(int)(((double)llngDaysSM)/((double)llngCountSM) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell(llngCountP == 0 ? null : (Integer)(int)(((double)llngDaysP)/((double)llngCountP) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(llngCountI == 0 ? null : (Integer)(int)(((double)llngDaysI)/((double)llngCountI) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[6], true, true);

		larrCells[7] = ReportBuilder.buildCell(llngCountM == 0 ? null : (Integer)(int)(((double)llngDaysM)/((double)llngCountM) + 0.5),
				TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[7], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected void setWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth(150);
		parrCells[ 1].setWidth(100);
		parrCells[ 2].setWidth(100);
		parrCells[ 3].setWidth(100);
		parrCells[ 4].setWidth(100);
		parrCells[ 5].setWidth(100);
		parrCells[ 6].setWidth(100);
		parrCells[ 7].setWidth(100);
	}
}
