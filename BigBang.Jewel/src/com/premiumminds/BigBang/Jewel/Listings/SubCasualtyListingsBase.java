package com.premiumminds.BigBang.Jewel.Listings;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

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
import com.premiumminds.BigBang.Jewel.Objects.Casualty;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.OtherEntity;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class SubCasualtyListingsBase
{
	protected static class SubCasualtyMarkers
	{
		public Timestamp mdtCreated;
		public Timestamp mdtReported;
		public Timestamp mdtClosed;
	}

	protected HashMap<UUID, SubCasualtyMarkers> mmapData;

	protected Table buildHeaderSection(String pstrHeader, SubCasualty[] parrSubCs, int plngMapSize)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;

		larrRows = new TR[3];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		larrRows[1] = ReportBuilder.constructDualRow("Nº de Gestores", plngMapSize, TypeDefGUIDs.T_Integer, false);

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Sub-Sinsitros", parrSubCs.length, TypeDefGUIDs.T_Integer, false);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected Table buildDataSection(String pstrHeader, ArrayList<SubCasualty> parrSubCs)
		throws BigBangJewelException
	{
		return buildDataSection(pstrHeader, parrSubCs.toArray(new SubCasualty[parrSubCs.size()]));
	}

	protected Table buildDataSection(String pstrHeader, SubCasualty[] parrSubCs)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;

		larrRows = buildDataTable(pstrHeader, parrSubCs);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected TR[] buildDataTable(String pstrHeader, SubCasualty[] parrSubCs)
		throws BigBangJewelException
	{
		TR[] larrRows;
		TD lcell;

		larrRows = new TR[3];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInner(parrSubCs));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[1] = ReportBuilder.buildRow(new TD[] {lcell});

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Sub-Sinistros", parrSubCs.length, TypeDefGUIDs.T_Integer, false);

		return larrRows;
	}

	protected Table buildInner(SubCasualty[] parrSubCs)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;
		int i;

		getMarkersData();

		larrRows = new TR[parrSubCs.length + 1];

		larrRows[0] = ReportBuilder.buildRow(buildInnerHeaderRow());
		ReportBuilder.styleRow(larrRows[0], true);

		for ( i = 1; i <= parrSubCs.length; i++ )
		{
			larrRows[i] = ReportBuilder.buildRow(buildRow(parrSubCs[i - 1]));
			ReportBuilder.styleRow(larrRows[i], false);
		}

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[14];

		larrCells[0] = ReportBuilder.buildHeaderCell("Número");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("Cliente");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Apólice");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Ramo");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Comp");
		ReportBuilder.styleCell(larrCells[4], false, true);

		larrCells[5] = ReportBuilder.buildHeaderCell("Objecto");
		ReportBuilder.styleCell(larrCells[5], false, true);

		larrCells[6] = ReportBuilder.buildHeaderCell("Processo");
		ReportBuilder.styleCell(larrCells[6], false, true);

		larrCells[7] = ReportBuilder.buildHeaderCell("Oficina");
		ReportBuilder.styleCell(larrCells[7], false, true);

		larrCells[8] = ReportBuilder.buildHeaderCell("Judicial");
		ReportBuilder.styleCell(larrCells[8], false, true);

		larrCells[9] = ReportBuilder.buildHeaderCell("Estado");
		ReportBuilder.styleCell(larrCells[9], false, true);

		larrCells[10] = ReportBuilder.buildHeaderCell("Data Sinistro");
		ReportBuilder.styleCell(larrCells[10], false, true);

		larrCells[11] = ReportBuilder.buildHeaderCell("Abertura");
		ReportBuilder.styleCell(larrCells[11], false, true);

		larrCells[12] = ReportBuilder.buildHeaderCell("Participação");
		ReportBuilder.styleCell(larrCells[12], false, true);

		larrCells[13] = ReportBuilder.buildHeaderCell("Encerramento");
		ReportBuilder.styleCell(larrCells[13], false, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected TD[] buildRow(SubCasualty pobjSubC)
		throws BigBangJewelException
	{
		Casualty lobjCas;
		Client lobjClient;
		Policy lobjPol;
		SubPolicy lobjSubPol;
		Company lobjComp;
		SubLine lobjSubLine;
		OtherEntity lobjCenter;
		String lstrState;
		SubCasualtyMarkers lobjMarkers;
		TD[] larrCells;

		try
		{
			lobjCas = pobjSubC.GetCasualty();
			lobjClient = pobjSubC.GetClient();
			lobjPol = pobjSubC.GetPolicy();
			lobjSubPol = pobjSubC.GetSubPolicy();
			lobjComp = pobjSubC.GetCompany();
			lobjSubLine = pobjSubC.GetSubLine();
			lobjCenter = pobjSubC.GetServiceCenter();
			lstrState = ( PNProcess.GetInstance(Engine.getCurrentNameSpace(), pobjSubC.GetProcessID()).IsRunning() ?
					"Aberto" : "Fechado" );
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		lobjMarkers = mmapData.get(pobjSubC.getKey());

		larrCells = new TD[14];

		larrCells[0] = ReportBuilder.buildCell(pobjSubC.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(lobjClient.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(lobjSubPol == null ? lobjPol.getLabel() : lobjSubPol.getLabel(),
				TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(lobjSubLine.getDescription(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(lobjComp.getAt(Company.I.ACRONYM), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell(pobjSubC.GetObjectName(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(pobjSubC.getAt(SubCasualty.I.INSURERPROCESS), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[6], true, true);

		larrCells[7] = ReportBuilder.buildCell(lobjCenter == null ? null : lobjCenter.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[7], true, true);

		larrCells[8] = ReportBuilder.buildCell(pobjSubC.getAt(SubCasualty.I.HASJUDICIAL), TypeDefGUIDs.T_Boolean);
		ReportBuilder.styleCell(larrCells[8], true, true);

		larrCells[9] = ReportBuilder.buildCell(lstrState, TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[9], true, true);

		larrCells[10] = ReportBuilder.buildCell(lobjCas.getAt(Casualty.I.DATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[10], true, true);

		larrCells[11] = ReportBuilder.buildCell(lobjMarkers == null ? null : lobjMarkers.mdtCreated, TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[11], true, true);

		larrCells[12] = ReportBuilder.buildCell(lobjMarkers == null ? null : lobjMarkers.mdtReported, TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[12], true, true);

		larrCells[13] = ReportBuilder.buildCell(lobjMarkers == null ? null : lobjMarkers.mdtClosed, TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[13], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected void setWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth(100);
		parrCells[ 1].setWidth(300);
		parrCells[ 2].setWidth(100);
		parrCells[ 3].setWidth(100);
		parrCells[ 4].setWidth( 50);
		parrCells[ 5].setWidth(200);
		parrCells[ 6].setWidth(100);
		parrCells[ 7].setWidth(100);
		parrCells[ 8].setWidth( 50);
		parrCells[ 9].setWidth( 50);
		parrCells[10].setWidth(100);
		parrCells[11].setWidth(100);
		parrCells[12].setWidth(100);
		parrCells[13].setWidth(100);
	}

	protected void filterByClient(StringBuilder pstrSQL, UUID pidClient)
		throws BigBangJewelException
	{
		IEntity lrefPolicies;
		IEntity lrefSubPolicies;

		try
		{
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));

			pstrSQL.append(" AND ([Policy] IN (SELECT [PK] FROM (" +
					lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.CLIENT},
							new java.lang.Object[] {pidClient}, null) +
					") [AuxPol]) OR [SubPolicy] IN (SELECT [PK] FROM (" +
					lrefSubPolicies.SQLForSelectByMembers(new int[] {SubPolicy.I.SUBSCRIBER},
							new java.lang.Object[] {pidClient}, null) +
					") [AuxSPol]))");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	protected void filterByClientGroup(StringBuilder pstrSQL, UUID pidGroup)
		throws BigBangJewelException
	{
		IEntity lrefPolicies;
		IEntity lrefSubPolicies;
		IEntity lrefClients;

		try
		{
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
			lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));

			pstrSQL.append(" AND ([Policy] IN (SELECT [PK] FROM (" +
					lrefPolicies.SQLForSelectAll() +
					") [AuxPol] WHERE [Client] IN (SELECT [PK] FROM (" +
					lrefClients.SQLForSelectByMembers(new int[] {Client.I.GROUP}, new java.lang.Object[] {pidGroup}, null) +
					"))) OR [SubPolicy] IN (SELECT [PK] FROM (" +
					lrefSubPolicies.SQLForSelectAll() +
					") [AuxSPol] WHERE [Subscriber} IN (SELECT [PK] FROM (" +
					lrefClients.SQLForSelectByMembers(new int[] {Client.I.GROUP}, new java.lang.Object[] {pidGroup}, null) +
					"))))");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	protected void filterByCompany(StringBuilder pstrSQL, UUID pidCompany)
		throws BigBangJewelException
	{
		IEntity lrefPolicies;
		IEntity lrefSubPolicies;

		try
		{
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefSubPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));

			pstrSQL.append(" AND ([Policy] IN (SELECT [PK] FROM (" +
					lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.COMPANY},
							new java.lang.Object[] {pidCompany}, null) +
					") [AuxPolD]) OR [SubPolicy] IN (SELECT [PK] FROM (" +
					lrefSubPolicies.SQLForSelectAll() +
					") [AuxSPol] WHERE [Policy] IN (SELECT [PK] FROM (" +
					lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.COMPANY},
							new java.lang.Object[] {pidCompany}, null) +
					") [AuxPolI])))");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	protected void filterByCategory(StringBuilder pstrSQL, UUID pidCategory)
		throws BigBangJewelException
	{
	}

	protected void filterByLine(StringBuilder pstrSQL, UUID pidLine)
		throws BigBangJewelException
	{
	}

	protected void filterBySubLine(StringBuilder pstrSQL, UUID pidSubLine)
		throws BigBangJewelException
	{
	}

	protected void getMarkersData()
		throws BigBangJewelException
	{
		IEntity lrefSubCs;
		IEntity lrefLogs;
		String lstrSQL;
		MasterDB ldb;
		ResultSet lrsSubCs;
		UUID lidAux;
		SubCasualtyMarkers lobjAux;

		if ( mmapData != null )
			return;

		mmapData = new HashMap<UUID, SubCasualtyMarkers>();

		try
		{
			lrefSubCs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));

			lstrSQL = "SELECT [PK], " +
					"(SELECT [Timestamp] FROM (" + lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {Constants.OPID_Casualty_CreateSubCasualty, false}, null) + ") [AuxLogs1] " +
							"WHERE [External Process] = [Main].[Process]), " +
					"(SELECT [Timestamp] FROM (" + lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {Constants.OPID_SubCasualty_SendNotification, false}, null) + ") [AuxLogs2] " +
							"WHERE [Process] = [Main].[Process]), " +
					"(SELECT MAX([Timestamp]) FROM (" + lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {Constants.OPID_SubCasualty_CloseProcess, false}, null) + ") [AuxLogs3] " +
							"WHERE [Process] = [Main].[Process]) " +
					"FROM (" + lrefSubCs.SQLForSelectAll() + ") [Main];";

			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubCs = ldb.OpenRecordset(lstrSQL.toString());
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsSubCs.next() )
			{
				lidAux = UUID.fromString(lrsSubCs.getString(1));
				lobjAux = new SubCasualtyMarkers();
				lobjAux.mdtCreated = lrsSubCs.getTimestamp(2);
				lobjAux.mdtReported = lrsSubCs.getTimestamp(3);
				lobjAux.mdtClosed = lrsSubCs.getTimestamp(4);
				mmapData.put(lidAux, lobjAux);
			}
		}
		catch (Throwable e)
		{
			try { lrsSubCs.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubCs.close();
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
	}
}
