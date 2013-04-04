package com.premiumminds.BigBang.Jewel.Listings.SubCasualty;

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

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualtyListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.Category;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class SubCasualtyOtherClosingTimes
	extends SubCasualtyListingsBase
{
	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		SubCasualty[] larrAux;
		HashMap<UUID, ArrayList<SubCasualty>> larrMap;
		int i;
		UUID lidCategory;
		GenericElement[] larrResult;

		if ( parrParams[1] == null )
			parrParams[1] = new Timestamp(new java.util.Date().getTime()).toString().substring(0, 10);
		if ( parrParams[0] == null )
			parrParams[0] = parrParams[1].substring(0, 5) + "01-01";

		larrAux = getHistoryForOperation(parrParams);

		larrMap = new HashMap<UUID, ArrayList<SubCasualty>>();
		for ( i = 0; i < larrAux.length; i++ )
		{
			try
			{
				lidCategory = larrAux[i].GetSubLine().getLine().getCategory().getKey();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if ( larrMap.get(lidCategory) == null )
				larrMap.put(lidCategory, new ArrayList<SubCasualty>());
			larrMap.get(lidCategory).add(larrAux[i]);
		}

		getMarkersData();

		larrResult = new GenericElement[1];

		larrResult[0] = buildOuterTable(larrMap, parrParams[0], parrParams[1]);

		return larrResult;
	}

	protected SubCasualty[] getHistoryForOperation(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<SubCasualty> larrAux;
		IEntity lrefubCs;
		IEntity lrefLogs;
		IEntity lrefProcs;
		MasterDB ldb;
		ResultSet lrsPolicies;

		try
		{
			lrefubCs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));
			lrefProcs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNProcess));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (")
					.append(lrefubCs.SQLForSelectByMembers(
							new int[] {SubCasualty.I.HASJUDICIAL},
							new java.lang.Object[] {false},
							null)).append(") [AuxSubC] ")
					.append("WHERE [Process] IN (SELECT [PK] FROM (")
					.append(lrefProcs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKScript_In_Process, Jewel.Petri.Constants.IsRunning_In_Process},
							new java.lang.Object[] {Constants.ProcID_SubCasualty, false}, null)).append(") [AuxProcs]) ")
					.append("AND [Process] IN (SELECT [Process] FROM (")
					.append(lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {Constants.OPID_SubCasualty_CloseProcess, false},
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

		larrAux = new ArrayList<SubCasualty>();

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
			while ( lrsPolicies.next() )
				larrAux.add(SubCasualty.GetInstance(Engine.getCurrentNameSpace(), lrsPolicies));
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

		return larrAux.toArray(new SubCasualty[larrAux.size()]);
	}

	protected Table buildOuterTable(HashMap<UUID, ArrayList<SubCasualty>> parrMap, String pstrFrom, String pstrTo)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;
		TD lcell;

		larrRows = new TR[3];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell("Mapa de Prazos de Encerramento");

		larrRows[1] = ReportBuilder.constructDualRow("Datas ", pstrFrom + " a " + pstrTo, TypeDefGUIDs.T_String, false);

		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInnerTable(parrMap));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[2] = ReportBuilder.buildRow(new TD[] {lcell});

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected Table buildInnerTable(HashMap<UUID, ArrayList<SubCasualty>> parrMap)
		throws BigBangJewelException
	{
		R<Integer> llngCountT;
		R<Integer> llngCountS;
		R<Integer> llngDaysM;
		R<Integer> llngDaysR;
		int i;
		Table ltbl;
		TR[] larrRows;

		llngCountT = new R<Integer>(0);
		llngCountS = new R<Integer>(0);
		llngDaysM = new R<Integer>(0);
		llngDaysR = new R<Integer>(0);

		larrRows = new TR[parrMap.size() + 2];

		larrRows[0] = ReportBuilder.buildRow(buildInnerHeaderRow());
		ReportBuilder.styleRow(larrRows[0], true);

		i = 1;
		for ( UUID lid: parrMap.keySet() )
		{
			larrRows[i] = ReportBuilder.buildRow(buildInnerRow(lid, parrMap.get(lid), llngCountT, llngCountS, llngDaysM, llngDaysR));
			ReportBuilder.styleRow(larrRows[i], false);
			i++;
		}

		larrRows[i] = ReportBuilder.buildRow(buildInnerFooterRow(llngCountT.get(), llngCountS.get(), llngDaysM.get(), llngDaysR.get()));
		ReportBuilder.styleRow(larrRows[i], false);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[7];

		larrCells[0] = ReportBuilder.buildHeaderCell("Categoria");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("Encerrados Transactos");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Encerrados do Ano");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Prazo Médio Enc.");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Dias de Gestão");
		ReportBuilder.styleCell(larrCells[4], false, true);

		larrCells[5] = ReportBuilder.buildHeaderCell("Prazo Médio Rev.");
		ReportBuilder.styleCell(larrCells[5], false, true);

		larrCells[6] = ReportBuilder.buildHeaderCell("Dias de Revisão");
		ReportBuilder.styleCell(larrCells[6], false, true);

		setWidths(larrCells);

		return larrCells;
	}

	@SuppressWarnings("deprecation")
	protected TD[] buildInnerRow(UUID pidCategory, ArrayList<SubCasualty> parrReceipts, R<Integer> plngCountT,
			R<Integer> plngCountS, R<Integer> plngDaysM, R<Integer> plngDaysR)
		throws BigBangJewelException
	{
		Category lobjCat;
		int llngCountT;
		int llngCountS;
		int llngDaysM;
		int llngDaysR;
		SubCasualtyMarkers lobjAux;
		TD[] larrCells;

		lobjCat = Category.GetInstance(Engine.getCurrentNameSpace(), pidCategory);

		llngCountT = 0;
		llngCountS = 0;
		llngDaysM = 0;
		llngDaysR = 0;

		for ( SubCasualty lobjSubC : parrReceipts )
		{
			lobjAux = mmapData.get(lobjSubC.getKey());
			if ( (lobjAux != null) && (lobjAux.mdtCreated != null) && (lobjAux.mdtMarked != null) && (lobjAux.mdtClosed != null) )
			{
				if ( lobjAux.mdtClosed.getYear() == lobjAux.mdtCreated.getYear() )
					llngCountS++;
				else
					llngCountT++;
				llngDaysM += (lobjAux.mdtMarked.getTime() - lobjAux.mdtCreated.getTime()) / (1000 * 60 * 60 *24);
				llngDaysR += (lobjAux.mdtClosed.getTime() - lobjAux.mdtMarked.getTime()) / (1000 * 60 * 60 *24);
			}
			else
				llngCountT++;
		}

		larrCells = new TD[7];

		larrCells[0] = ReportBuilder.buildCell(lobjCat.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(llngCountT, TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(llngCountS, TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell((Integer)(int)(((double)llngDaysM)/((double)(llngCountT + llngCountS)) + 0.5), TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(llngDaysM, TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell((Integer)(int)(((double)llngDaysR)/((double)(llngCountT + llngCountS)) + 0.5), TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(llngDaysR, TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[6], true, true);

		setWidths(larrCells);

		plngCountT.set(plngCountT.get() + llngCountT);
		plngCountS.set(plngCountS.get() + llngCountS);
		plngDaysM.set(plngDaysM.get() + llngDaysM);
		plngDaysR.set(plngDaysR.get() + llngDaysR);

		return larrCells;
	}

	protected TD[] buildInnerFooterRow(int plngCountT, int plngCountS, int plngDaysM, int plngDaysR)
	{
		TD[] larrCells;

		larrCells = new TD[7];

		larrCells[0] = ReportBuilder.buildCell("Totais", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(plngCountT, TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(plngCountS, TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell((Integer)(int)(((double)plngDaysM)/((double)(plngCountT + plngCountS)) + 0.5), TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(plngDaysM, TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell((Integer)(int)(((double)plngDaysR)/((double)(plngCountT + plngCountS)) + 0.5), TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(plngDaysR, TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[6], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected void setWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth(300);
		parrCells[ 1].setWidth(150);
		parrCells[ 2].setWidth(150);
		parrCells[ 3].setWidth(150);
		parrCells[ 4].setWidth(150);
		parrCells[ 5].setWidth(150);
		parrCells[ 6].setWidth(150);
	}
}
