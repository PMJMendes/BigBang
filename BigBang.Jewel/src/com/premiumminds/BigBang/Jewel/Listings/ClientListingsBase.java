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
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.ClientGroup;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class ClientListingsBase
{
	protected static class ClientTotals
	{
		public int mlngPoliciesToValidate;
		public int mlngPoliciesValid;
		public int mlngPoliciesTotal;
		public int mlngQRsRunning;
		public int mlngQRsStopped;
		public Timestamp mdtCreated;
	}

	protected HashMap<UUID, ClientTotals> mmapData;

	protected Table buildHeaderSection(String pstrHeader, Client[] parrClients, int plngMapSize)
		throws BigBangJewelException
	{
		int llngTotalQRs;
		int llngTotalValPs;
		int llngTotalOldPs;
		int i;
		ClientTotals lobjAux;
		Table ltbl;
		TR[] larrRows;

		getTotalsData();

		llngTotalQRs = 0;
		llngTotalValPs = 0;
		llngTotalOldPs = 0;
		for ( i = 0; i < parrClients.length; i++ )
		{
			lobjAux = mmapData.get(parrClients[i].getKey());
			if ( lobjAux == null )
				continue;

			llngTotalQRs = llngTotalQRs + lobjAux.mlngQRsRunning;
			llngTotalValPs = llngTotalValPs + lobjAux.mlngPoliciesToValidate + lobjAux.mlngPoliciesValid;
			llngTotalOldPs = llngTotalOldPs + lobjAux.mlngPoliciesTotal -
					(lobjAux.mlngPoliciesToValidate + lobjAux.mlngPoliciesValid);
		}

		larrRows = new TR[6];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		larrRows[1] = ReportBuilder.constructDualRow("Nº de Gestores", plngMapSize, TypeDefGUIDs.T_Integer, false);

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Clientes", parrClients.length, TypeDefGUIDs.T_Integer, false);

		larrRows[3] = ReportBuilder.constructDualRow("Total de Consultas Abertas", llngTotalQRs, TypeDefGUIDs.T_Integer, false);

		larrRows[4] = ReportBuilder.constructDualRow("Total de Apólices Abertas", llngTotalValPs, TypeDefGUIDs.T_Integer, false);

		larrRows[5] = ReportBuilder.constructDualRow("Total de Apólices Antigas", llngTotalOldPs, TypeDefGUIDs.T_Integer, false);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected Table buildDataSection(String pstrHeader, ArrayList<Client> parrClients)
		throws BigBangJewelException
	{
		return buildDataSection(pstrHeader, parrClients.toArray(new Client[parrClients.size()]));
	}

	protected Table buildDataSection(String pstrHeader, Client[] parrClients)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;

		larrRows = buildDataTable(pstrHeader, parrClients);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected TR[] buildDataTable(String pstrHeader, Client[] parrClients)
		throws BigBangJewelException
	{
		int llngTotalQRs;
		int llngTotalValPs;
		int llngTotalOldPs;
		int i;
		ClientTotals lobjAux;
		TR[] larrRows;
		TD lcell;

		getTotalsData();

		llngTotalQRs = 0;
		llngTotalValPs = 0;
		llngTotalOldPs = 0;
		for ( i = 0; i < parrClients.length; i++ )
		{
			lobjAux = mmapData.get(parrClients[i].getKey());
			if ( lobjAux == null )
				continue;

			llngTotalQRs = llngTotalQRs + lobjAux.mlngQRsRunning;
			llngTotalValPs = llngTotalValPs + lobjAux.mlngPoliciesToValidate + lobjAux.mlngPoliciesValid;
			llngTotalOldPs = llngTotalOldPs + lobjAux.mlngPoliciesTotal -
					(lobjAux.mlngPoliciesToValidate + lobjAux.mlngPoliciesValid);
		}

		larrRows = new TR[6];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInner(parrClients));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[1] = ReportBuilder.buildRow(new TD[] {lcell});

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Clientes", parrClients.length, TypeDefGUIDs.T_Integer, false);

		larrRows[3] = ReportBuilder.constructDualRow("Total de Consultas Abertas", llngTotalQRs, TypeDefGUIDs.T_Integer, false);

		larrRows[4] = ReportBuilder.constructDualRow("Total de Apólices Abertas", llngTotalValPs, TypeDefGUIDs.T_Integer, false);

		larrRows[5] = ReportBuilder.constructDualRow("Total de Apólices Antigas", llngTotalOldPs, TypeDefGUIDs.T_Integer, false);

		return larrRows;
	}

	protected Table buildInner(Client[] parrClients)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;
		int i;

		larrRows = new TR[parrClients.length + 1];

		larrRows[0] = ReportBuilder.buildRow(buildInnerHeaderRow());
		ReportBuilder.styleRow(larrRows[0], true);

		for ( i = 1; i <= parrClients.length; i++ )
		{
			larrRows[i] = ReportBuilder.buildRow(buildRow(parrClients[i - 1]));
			ReportBuilder.styleRow(larrRows[i], false);
		}

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[11];

		larrCells[0] = ReportBuilder.buildHeaderCell("Número");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("Nome");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Tipo");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Sub-tipo");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Grupo");
		ReportBuilder.styleCell(larrCells[4], false, true);

		larrCells[5] = ReportBuilder.buildHeaderCell("Perfil");
		ReportBuilder.styleCell(larrCells[5], false, true);

		larrCells[6] = ReportBuilder.buildHeaderCell("Mediador");
		ReportBuilder.styleCell(larrCells[6], false, true);

		larrCells[7] = ReportBuilder.buildHeaderCell("Consultas Activas");
		ReportBuilder.styleCell(larrCells[7], false, true);

		larrCells[8] = ReportBuilder.buildHeaderCell("Apólices Activas");
		ReportBuilder.styleCell(larrCells[8], false, true);

		larrCells[9] = ReportBuilder.buildHeaderCell("Apólices Anuladas");
		ReportBuilder.styleCell(larrCells[9], false, true);

		larrCells[10] = ReportBuilder.buildHeaderCell("Data Criação");
		ReportBuilder.styleCell(larrCells[10], false, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected TD[] buildRow(Client pobjClient)
		throws BigBangJewelException
	{
		ObjectBase lobjType;
		ObjectBase lobjSubType;
		ObjectBase lobjProfile;
		ClientGroup lobjGroup;
		Mediator lobjMed;
		ClientTotals lobjTotals;
		TD[] larrCells;

		try
		{
			lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientType),
					(UUID)pobjClient.getAt(Client.I.ENTITYTYPE));
			lobjSubType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientSubtype),
					(UUID)pobjClient.getAt(Client.I.ENTITYSUBTYPE));

			lobjProfile = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_OpProfile),
					pobjClient.getProfile());
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		lobjGroup = ClientGroup.GetInstance(Engine.getCurrentNameSpace(), (UUID)pobjClient.getAt(Client.I.GROUP));

		lobjMed = pobjClient.getMediator();

		lobjTotals = mmapData.get(pobjClient.getKey());

		larrCells = new TD[11];

		larrCells[0] = ReportBuilder.buildCell(pobjClient.getAt(Client.I.NUMBER), TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(pobjClient.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(lobjType.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(lobjSubType.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(lobjGroup.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell(lobjProfile.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(lobjMed.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[6], true, true);

		larrCells[7] = ReportBuilder.buildCell(lobjTotals == null ? null : lobjTotals.mlngQRsRunning, TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[7], true, true);

		larrCells[8] = ReportBuilder.buildCell(lobjTotals == null ? null : lobjTotals.mlngPoliciesToValidate +
				lobjTotals.mlngPoliciesValid, TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[8], true, true);

		larrCells[9] = ReportBuilder.buildCell(lobjTotals == null ? null : lobjTotals.mlngPoliciesTotal -
				(lobjTotals.mlngPoliciesToValidate + lobjTotals.mlngPoliciesValid), TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[9], true, true);

		larrCells[10] = ReportBuilder.buildCell(lobjTotals == null ? null : lobjTotals.mdtCreated, TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[10], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected void setWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth(100);
		parrCells[ 1].setWidth(350);
		parrCells[ 2].setWidth( 20);
		parrCells[ 3].setWidth( 20);
		parrCells[ 4].setWidth(130);
		parrCells[ 5].setWidth(140);
		parrCells[ 6].setWidth(170);
		parrCells[ 7].setWidth(100);
		parrCells[ 8].setWidth(100);
		parrCells[ 9].setWidth(100);
		parrCells[10].setWidth(100);
	}

	protected void filterByClientGroup(StringBuilder pstrSQL, UUID pidGroup)
		throws BigBangJewelException
	{
		pstrSQL.append(" AND [Group] = '" + pidGroup.toString() + "'");
	}

	protected void filterByAgent(StringBuilder pstrSQL, UUID pidAgent)
		throws BigBangJewelException
	{
		pstrSQL.append(" AND [Mediator] = '" + pidAgent.toString() + "'");
	}

	protected void getTotalsData()
		throws BigBangJewelException
	{
		IEntity lrefClients;
		IEntity lrefPolicies;
		IEntity lrefQRequests;
		IEntity lrefProcs;
		IEntity lrefLogs;
		String lstrSQL;
		MasterDB ldb;
		ResultSet lrsClients;
		UUID lidAux;
		ClientTotals lobjAux;

		if ( mmapData != null )
			return;

		mmapData = new HashMap<UUID, ClientTotals>();

		try
		{
			lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefQRequests = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_QuoteRequest));
			lrefProcs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNProcess));
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));

			lstrSQL = "SELECT [PK], " +
					"(SELECT COUNT(PK) FROM (" + lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.STATUS},
							new java.lang.Object[] {Constants.StatusID_InProgress}, null) + ") [AuxPol1] " +
							"WHERE [Client] = [Main].PK), " +
					"(SELECT COUNT(PK) FROM (" + lrefPolicies.SQLForSelectByMembers(new int[] {Policy.I.STATUS},
							new java.lang.Object[] {Constants.StatusID_Valid}, null) + ") [AuxPol2] " +
							"WHERE [Client] = [Main].PK), " +
					"(SELECT COUNT(PK) FROM (" + lrefPolicies.SQLForSelectAll() + ") [AuxPol3] " +
							"WHERE [Client] = [Main].PK), " +
					"(SELECT COUNT(PK) FROM (" + lrefQRequests.SQLForSelectAll() + ") [AuxQR1] " +
							"WHERE [Client] = [Main].PK AND [Process] IN (SELECT [Process] FROM (" + lrefProcs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKScript_In_Process, Jewel.Petri.Constants.IsRunning_In_Process},
							new java.lang.Object[] {Constants.ProcID_QuoteRequest, true}, null) + ") [AuxProcs1])), " +
					"(SELECT COUNT(PK) FROM (" + lrefQRequests.SQLForSelectAll() + ") [AuxQR2] " +
							"WHERE [Client] = [Main].PK AND [Process] IN (SELECT [Process] FROM (" + lrefProcs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKScript_In_Process, Jewel.Petri.Constants.IsRunning_In_Process},
							new java.lang.Object[] {Constants.ProcID_QuoteRequest, false}, null) + ") [AuxProcs2])), " +
					"(SELECT [Timestamp] FROM (" + lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {Constants.OPID_General_CreateClient, false}, null) + ") [AuxLogs] " +
							"WHERE [External Process] = [Main].[Process]) " +
					"FROM (" + lrefClients.SQLForSelectAll() + ") [Main];";

			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsClients = ldb.OpenRecordset(lstrSQL.toString());
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsClients.next() )
			{
				lidAux = UUID.fromString(lrsClients.getString(1));
				lobjAux = new ClientTotals();
				lobjAux.mlngPoliciesToValidate = lrsClients.getInt(2);
				lobjAux.mlngPoliciesValid = lrsClients.getInt(3);
				lobjAux.mlngPoliciesTotal = lrsClients.getInt(4);
				lobjAux.mlngQRsRunning = lrsClients.getInt(5);
				lobjAux.mlngQRsStopped = lrsClients.getInt(6);
				lobjAux.mdtCreated = lrsClients.getTimestamp(7);
				mmapData.put(lidAux, lobjAux);
			}
		}
		catch (Throwable e)
		{
			try { lrsClients.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsClients.close();
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
