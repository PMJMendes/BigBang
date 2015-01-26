package com.premiumminds.BigBang.Jewel.Listings;

import java.math.BigDecimal;
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
import com.premiumminds.BigBang.Jewel.Objects.Company;
import com.premiumminds.BigBang.Jewel.Objects.Mediator;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class PolicyListingsBase
{
	protected static class PolicyMarkers
	{
		public Timestamp mdtLastRec;
		public Timestamp mdtCreated;
	}

	protected HashMap<UUID, PolicyMarkers> mmapData;

	protected Table buildHeaderSection(String pstrHeader, Policy[] parrPolicies, int plngMapSize)
		throws BigBangJewelException
	{
		BigDecimal ldblTotal;
		int i;
		Table ltbl;
		TR[] larrRows;

		ldblTotal = BigDecimal.ZERO;
		for ( i = 0; i < parrPolicies.length; i++ )
		{
			if ( parrPolicies[i].getAt(Policy.I.PREMIUM) != null )
				ldblTotal = ldblTotal.add((BigDecimal)parrPolicies[i].getAt(Policy.I.PREMIUM));
		}

		larrRows = new TR[4];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		larrRows[1] = ReportBuilder.constructDualRow("Nº de Gestores", plngMapSize, TypeDefGUIDs.T_Integer, false);

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Apólices", parrPolicies.length, TypeDefGUIDs.T_Integer, false);

		larrRows[3] = ReportBuilder.constructDualRow("Total de Prémios", ldblTotal, TypeDefGUIDs.T_Decimal, false);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected Table buildDataSection(String pstrHeader, ArrayList<Policy> parrPolicies)
		throws BigBangJewelException
	{
		return buildDataSection(pstrHeader, parrPolicies.toArray(new Policy[parrPolicies.size()]));
	}

	protected Table buildDataSection(String pstrHeader, Policy[] parrPolicies)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;

		larrRows = buildDataTable(pstrHeader, parrPolicies);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected TR[] buildDataTable(String pstrHeader, Policy[] parrPolicies)
		throws BigBangJewelException
	{
		BigDecimal ldblTotal;
		int i;
		TR[] larrRows;
		TD lcell;

		ldblTotal = BigDecimal.ZERO;
		for ( i = 0; i < parrPolicies.length; i++ )
		{
			if ( parrPolicies[i].getAt(Policy.I.PREMIUM) != null )
				ldblTotal = ldblTotal.add((BigDecimal)parrPolicies[i].getAt(Policy.I.PREMIUM));
		}

		larrRows = new TR[4];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInner(parrPolicies));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[1] = ReportBuilder.buildRow(new TD[] {lcell});

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Apólices", parrPolicies.length, TypeDefGUIDs.T_Integer, false);

		larrRows[3] = ReportBuilder.constructDualRow("Total de Prémios", ldblTotal, TypeDefGUIDs.T_Decimal, false);

		return larrRows;
	}

	protected Table buildInner(Policy[] parrPolicies)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;
		int i;

		getMarkersData();

		larrRows = new TR[parrPolicies.length + 1];

		larrRows[0] = ReportBuilder.buildRow(buildInnerHeaderRow());
		ReportBuilder.styleRow(larrRows[0], true);

		for ( i = 1; i <= parrPolicies.length; i++ )
		{
			larrRows[i] = ReportBuilder.buildRow(buildRow(parrPolicies[i - 1]));
			ReportBuilder.styleRow(larrRows[i], false);
		}

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[15];

		larrCells[0] = ReportBuilder.buildHeaderCell("Número");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("Cliente");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Comp");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Ramo");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Perfil");
		ReportBuilder.styleCell(larrCells[4], false, true);

		larrCells[5] = ReportBuilder.buildHeaderCell("Prémio");
		ReportBuilder.styleCell(larrCells[5], false, true);

		larrCells[6] = ReportBuilder.buildHeaderCell("Criação");
		ReportBuilder.styleCell(larrCells[6], false, true);

		larrCells[7] = ReportBuilder.buildHeaderCell("Início");
		ReportBuilder.styleCell(larrCells[7], false, true);

		larrCells[8] = ReportBuilder.buildHeaderCell("Fim");
		ReportBuilder.styleCell(larrCells[8], false, true);

		larrCells[9] = ReportBuilder.buildHeaderCell("Venc. (M/D)");
		ReportBuilder.styleCell(larrCells[9], false, true);

		larrCells[10] = ReportBuilder.buildHeaderCell("Mediador");
		ReportBuilder.styleCell(larrCells[10], false, true);

		larrCells[11] = ReportBuilder.buildHeaderCell("Estado");
		ReportBuilder.styleCell(larrCells[11], false, true);

		larrCells[12] = ReportBuilder.buildHeaderCell("Duração");
		ReportBuilder.styleCell(larrCells[12], false, true);

		larrCells[13] = ReportBuilder.buildHeaderCell("Fracc.");
		ReportBuilder.styleCell(larrCells[13], false, true);

		larrCells[14] = ReportBuilder.buildHeaderCell("Último rec.");
		ReportBuilder.styleCell(larrCells[14], false, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected TD[] buildRow(Policy pobjPolicy)
		throws BigBangJewelException
	{
		Client lobjClient;
		Company lobjComp;
		SubLine lobjSubLine;
		ObjectBase lobjProfile;
		String lstrMaturity;
		Mediator lobjMed;
		ObjectBase lobjStatus;
		ObjectBase lobjDur;
		ObjectBase lobjFrac;
		PolicyMarkers lobjMarkers;
		TD[] larrCells;

		try
		{
			lobjClient = pobjPolicy.GetClient();
			lobjComp = pobjPolicy.GetCompany();
			lobjSubLine = pobjPolicy.GetSubLine();
			lobjProfile = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_OpProfile),
					pobjPolicy.getProfile());
			lstrMaturity = ((pobjPolicy.getAt(Policy.I.MATURITYMONTH) == null) || (pobjPolicy.getAt(Policy.I.MATURITYDAY) == null)) ?
					null :
					pobjPolicy.getAt(Policy.I.MATURITYMONTH).toString() + " / " + pobjPolicy.getAt(Policy.I.MATURITYDAY).toString();
			lobjMed = pobjPolicy.getMediator();
			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyStatus),
					(UUID)pobjPolicy.getAt(Policy.I.STATUS));
			lobjDur = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Durations),
					(UUID)pobjPolicy.getAt(Policy.I.DURATION));
			lobjFrac = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Fractioning),
					(UUID)pobjPolicy.getAt(Policy.I.FRACTIONING));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		lobjMarkers = mmapData.get(pobjPolicy.getKey());

		larrCells = new TD[15];

		larrCells[0] = ReportBuilder.buildCell(pobjPolicy.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(lobjClient.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(lobjComp.getAt(Company.I.ACRONYM), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(lobjSubLine.getDescription(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(lobjProfile.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell(pobjPolicy.getAt(Policy.I.TOTALPREMIUM), TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(lobjMarkers == null ? null : lobjMarkers.mdtCreated, TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[6], true, true);

		larrCells[7] = ReportBuilder.buildCell(pobjPolicy.getAt(Policy.I.BEGINDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[7], true, true);

		larrCells[8] = ReportBuilder.buildCell(pobjPolicy.getAt(Policy.I.ENDDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[8], true, true);

		larrCells[9] = ReportBuilder.buildCell(lstrMaturity, TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[9], true, true);

		larrCells[10] = ReportBuilder.buildCell(lobjMed.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[10], true, true);

		larrCells[11] = ReportBuilder.buildCell(lobjStatus.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[11], true, true);

		larrCells[12] = ReportBuilder.buildCell(lobjDur.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[12], true, true);

		larrCells[13] = ReportBuilder.buildCell(lobjFrac.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[13], true, true);

		larrCells[14] = ReportBuilder.buildCell(lobjMarkers == null ? null : lobjMarkers.mdtLastRec, TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[14], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected void setWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth(100);
		parrCells[ 1].setWidth(250);
		parrCells[ 2].setWidth( 50);
		parrCells[ 3].setWidth(100);
		parrCells[ 4].setWidth( 60);
		parrCells[ 5].setWidth( 80);
		parrCells[ 6].setWidth( 70);
		parrCells[ 7].setWidth( 70);
		parrCells[ 8].setWidth( 70);
		parrCells[ 9].setWidth( 90);
		parrCells[10].setWidth(150);
		parrCells[11].setWidth( 80);
		parrCells[12].setWidth( 90);
		parrCells[13].setWidth( 80);
		parrCells[14].setWidth( 70);
	}

	protected void filterByClient(StringBuilder pstrSQL, UUID pidGroup)
		throws BigBangJewelException
	{
		pstrSQL.append(" AND [Client] = '" + pidGroup.toString() + "'");
	}

	protected void filterByClientGroup(StringBuilder pstrSQL, UUID pidGroup)
		throws BigBangJewelException
	{
		IEntity lrefClients;

		try
		{
			lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));

			pstrSQL.append(" AND [Client] IN (SELECT [PK] FROM (")
					.append(lrefClients.SQLForSelectByMembers(new int[] {Client.I.GROUP}, new java.lang.Object[] {pidGroup}, null))
					.append(") [AuxCli])");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	protected void filterByAgent(StringBuilder pstrSQL, UUID pidAgent)
		throws BigBangJewelException
	{
		IEntity lrefClients;
		IEntity lrefGroups;

		try
		{
			lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client));
			lrefGroups = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_ClientGroup));

			pstrSQL.append(" AND ([Mediator] = '" + pidAgent.toString() + "' OR ([Mediator] IS NULL")
					.append(" AND [Client] IN (SELECT [PK] FROM (")
					.append(lrefClients.SQLForSelectByMembers(new int[] {Client.I.MEDIATOR}, new java.lang.Object[] {pidAgent}, null))
					.append(") [AuxCli1] UNION ALL SELECT [PK] FROM (")
					.append(lrefClients.SQLForSelectAll())
					.append(") [AuxCli1b] WHERE [Group] IN (SELECT [PK] FROM (")
					.append(lrefGroups.SQLForSelectByMembers(new int[] {ClientGroup.I.MEDIATOR}, new java.lang.Object[] {pidAgent}, null))
					.append(") [AuxGrp]))))");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	protected void filterByCompany(StringBuilder pstrSQL, UUID pidAgent)
		throws BigBangJewelException
	{
		pstrSQL.append(" AND [Company] = '" + pidAgent.toString() + "'");
	}

	protected void getMarkersData()
		throws BigBangJewelException
	{
		IEntity lrefPolicies;
		IEntity lrefReceipts;
		IEntity lrefLogs;
		String lstrSQL;
		MasterDB ldb;
		ResultSet lrsClients;
		UUID lidAux;
		PolicyMarkers lobjAux;

		if ( mmapData != null )
			return;

		mmapData = new HashMap<UUID, PolicyMarkers>();

		try
		{
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefReceipts = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Receipt));
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));

			lstrSQL = "SELECT [PK], " +
					"(SELECT MAX([Maturity Date]) FROM (" + lrefReceipts.SQLForSelectAll() +
							") [AuxRecs] WHERE [Type] IN ('" + Constants.RecType_New + "', '" + Constants.RecType_Continuing + "'" +
//							", '" + Constants.RecType_Adjustment + "'" +
							") AND [Process] NOT IN (SELECT [Process] FROM (" + lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {Constants.OPID_Receipt_ReturnToInsurer, false}, null) + ") [AuxLogs1]) " +
							"AND [Policy] = [Main].[PK]), " +
					"(SELECT [Timestamp] FROM (" + lrefLogs.SQLForSelectByMembers(
							new int[] {Jewel.Petri.Constants.FKOperation_In_Log, Jewel.Petri.Constants.Undone_In_Log},
							new java.lang.Object[] {Constants.OPID_Client_CreatePolicy, false}, null) + ") [AuxLogs2] " +
							"WHERE [External Process] = [Main].[Process]) " +
					"FROM (" + lrefPolicies.SQLForSelectAll() + ") [Main];";

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
				lobjAux = new PolicyMarkers();
				lobjAux.mdtLastRec = lrsClients.getTimestamp(2);
				lobjAux.mdtCreated = lrsClients.getTimestamp(3);
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
