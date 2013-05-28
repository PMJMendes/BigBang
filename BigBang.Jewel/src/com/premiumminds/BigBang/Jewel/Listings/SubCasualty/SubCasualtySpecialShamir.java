package com.premiumminds.BigBang.Jewel.Listings.SubCasualty;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.premiumminds.BigBang.Jewel.Objects.Casualty;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class SubCasualtySpecialShamir
{
	protected static final UUID gidShamir = UUID.fromString("E91E32C6-0C55-4F49-BA88-9F340153FD6D");
	protected static final UUID gidCost = UUID.fromString("BF80A9FC-AD3F-4630-8DD3-9F9601565146");
	protected static final UUID gidDate = UUID.fromString("64883643-ED00-498E-82D4-9F960156930B");

	protected HashMap<String, UUID> mmapLensIDs;

	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		SubCasualty[] larrAux;
		HashMap<UUID, ArrayList<SubCasualty>> larrMap;
		int i;
		UUID lidPolicy;
		GenericElement[] larrResult;
		R<BigDecimal> ldblTotal;

		larrAux = getHistoryForOperation(parrParams);

		getLensIDs();

		larrMap = new HashMap<UUID, ArrayList<SubCasualty>>();
		for ( i = 0; i < larrAux.length; i++ )
		{
			lidPolicy = larrAux[i].GetPolicy().getKey();
			if ( larrMap.get(lidPolicy) == null )
				larrMap.put(lidPolicy, new ArrayList<SubCasualty>());
			larrMap.get(lidPolicy).add(larrAux[i]);
		}

		larrResult = new GenericElement[larrMap.size() + 1];

		ldblTotal = new R<BigDecimal>(BigDecimal.ZERO);
		i = 1;
		for ( UUID lid: larrMap.keySet() )
		{
			try
			{
				larrResult[i] = buildDataSection("Apólice " + Policy.GetInstance(Engine.getCurrentNameSpace(), lid).getLabel(),
						larrMap.get(lid), ldblTotal);
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

		larrResult[0] = buildHeaderSection("Shamir - Activação de Lentes", larrAux, parrParams[0], parrParams[1], ldblTotal.get());

		return larrResult;
	}

	protected SubCasualty[] getHistoryForOperation(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<SubCasualty> larrAux;
		IEntity lrefSubCs;
		IEntity lrefPolicies;
		IEntity lrefCasualties;
		MasterDB ldb;
		ResultSet lrsSubCs;

		try
		{
			lrefSubCs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefCasualties = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Casualty));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (")
					.append(lrefSubCs.SQLForSelectAll()).append(") [AuxObj] WHERE [Policy] IN (SELECT [PK] FROM (")
					.append(lrefPolicies.SQLForSelectByMembers(
							new int[] {Policy.I.SUBLINE}, new java.lang.Object[] {gidShamir}, null))
					.append(") [AuxPols]) AND [Casualty] IN (SELECT [PK] FROM (")
					.append(lrefCasualties.SQLForSelectAll())
					.append(") [AuxCas] WHERE 1=1");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( parrParams[0] != null )
			lstrSQL.append(" AND [Date] >= '").append(parrParams[0]).append("'");

		if ( parrParams[1] != null )
			lstrSQL.append(" AND [Date] < DATEADD(d, 1, '").append(parrParams[1]).append("')");

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
				larrAux.add(SubCasualty.GetInstance(Engine.getCurrentNameSpace(), lrsSubCs));
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

		return larrAux.toArray(new SubCasualty[larrAux.size()]);
	}

	protected Table buildHeaderSection(String pstrHeader, SubCasualty[] parrSubCs, String pstrFrom, String pstrTo, BigDecimal pdblTotal)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;

		larrRows = new TR[5];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		larrRows[1] = ReportBuilder.constructDualRow("De", pstrFrom, TypeDefGUIDs.T_String, false);

		larrRows[2] = ReportBuilder.constructDualRow("Até", pstrTo, TypeDefGUIDs.T_String, false);

		larrRows[3] = ReportBuilder.constructDualRow("Nº de Sub-Sinistros", parrSubCs.length, TypeDefGUIDs.T_Integer, false);

		larrRows[4] = ReportBuilder.constructDualRow("Valor reclamado", pdblTotal, TypeDefGUIDs.T_Decimal, false);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected Table buildDataSection(String pstrHeader, ArrayList<SubCasualty> parrSubCs, R<BigDecimal> pdblTotal)
		throws BigBangJewelException
	{
		return buildDataSection(pstrHeader, parrSubCs.toArray(new SubCasualty[parrSubCs.size()]), pdblTotal);
	}

	protected Table buildDataSection(String pstrHeader, SubCasualty[] parrSubCs, R<BigDecimal> pdblTotal)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;

		larrRows = buildDataTable(pstrHeader, parrSubCs, pdblTotal);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected TR[] buildDataTable(String pstrHeader, SubCasualty[] parrSubCs, R<BigDecimal> pdblTotal)
		throws BigBangJewelException
	{
		TR[] larrRows;
		TD lcell;
		R<BigDecimal> ldblTotal;

		larrRows = new TR[4];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		ldblTotal = new R<BigDecimal>(BigDecimal.ZERO);
		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInner(parrSubCs, ldblTotal));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[1] = ReportBuilder.buildRow(new TD[] {lcell});

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Sub-Sinistros", parrSubCs.length, TypeDefGUIDs.T_Integer, false);

		larrRows[3] = ReportBuilder.constructDualRow("Valor reclamado", ldblTotal.get(), TypeDefGUIDs.T_Decimal, false);

		pdblTotal.set(pdblTotal.get().add(ldblTotal.get()));

		return larrRows;
	}

	protected Table buildInner(SubCasualty[] parrSubCs, R<BigDecimal> pdblTotal)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;
		int i;

		larrRows = new TR[parrSubCs.length + 1];

		larrRows[0] = ReportBuilder.buildRow(buildInnerHeaderRow());
		ReportBuilder.styleRow(larrRows[0], true);

		for ( i = 1; i <= parrSubCs.length; i++ )
		{
			larrRows[i] = ReportBuilder.buildRow(buildRow(parrSubCs[i - 1], pdblTotal));
			ReportBuilder.styleRow(larrRows[i], false);
		}

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[9];

		larrCells[0] = ReportBuilder.buildHeaderCell("N/Proc.");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("N/Proc. Cli.");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("N. Lente");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Tipo Lente");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Valor Lente");
		ReportBuilder.styleCell(larrCells[4], false, true);

		larrCells[5] = ReportBuilder.buildHeaderCell("Activação");
		ReportBuilder.styleCell(larrCells[5], false, true);

		larrCells[6] = ReportBuilder.buildHeaderCell("Tipo sin.");
		ReportBuilder.styleCell(larrCells[6], false, true);

		larrCells[7] = ReportBuilder.buildHeaderCell("Valido desde");
		ReportBuilder.styleCell(larrCells[7], false, true);

		larrCells[8] = ReportBuilder.buildHeaderCell("Válido até");
		ReportBuilder.styleCell(larrCells[8], false, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected TD[] buildRow(SubCasualty pobjSubC, R<BigDecimal> pdblTotal)
		throws BigBangJewelException
	{
		Casualty lobjCas;
		PolicyObject lobjObject;
		PolicyValue[] larrValues;
		int i;
		String lstrCost;
		TD[] larrCells;

		lstrCost = null;
		try
		{
			lobjCas = pobjSubC.GetCasualty();
			lobjObject = PolicyObject.GetInstance(Engine.getCurrentNameSpace(), mmapLensIDs.get(pobjSubC.GetObjectName().substring(10)));
			larrValues = lobjObject.GetOwner().GetCurrentKeyedValues(lobjObject.getKey(), null);
			for ( i = 0; i < larrValues.length; i++ )
			{
				if ( gidCost.equals(larrValues[i].GetTax().getKey()) )
					lstrCost = larrValues[i].GetValue();
			}
			if ( lstrCost != null )
				pdblTotal.set(pdblTotal.get().add(new BigDecimal(lstrCost.replace(',', '.'))));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrCells = new TD[9];

		larrCells[0] = ReportBuilder.buildCell(lobjCas.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(pobjSubC.getAt(SubCasualty.I.DESCRIPTION) == null ? "?" :
				((String)pobjSubC.getAt(SubCasualty.I.DESCRIPTION)).substring(19), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(lobjObject.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(lobjObject.getAt(PolicyObject.I.MAKEANDMODEL), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(lstrCost, TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell(lobjObject.getAt(PolicyObject.I.INCLUSIONDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(((String)lobjCas.getAt(Casualty.I.NOTES)).substring(0, 6).trim(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[6], true, true);

		larrCells[7] = ReportBuilder.buildCell(lobjObject.getAt(PolicyObject.I.INCLUSIONDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[7], true, true);

		larrCells[8] = ReportBuilder.buildCell(lobjObject.getAt(PolicyObject.I.EXCLUSIONDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[8], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected void setWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth(100);
		parrCells[ 1].setWidth(100);
		parrCells[ 2].setWidth(100);
		parrCells[ 3].setWidth(300);
		parrCells[ 4].setWidth(100);
		parrCells[ 5].setWidth(100);
		parrCells[ 6].setWidth(100);
		parrCells[ 7].setWidth(100);
		parrCells[ 8].setWidth(100);
	}

	protected void getLensIDs()
		throws BigBangJewelException
	{
		IEntity lrefObjects;
		IEntity lrefPols;
		String lstrSQL;
		MasterDB ldb;
		ResultSet lrsObjects;

		if ( mmapLensIDs != null )
			return;

		mmapLensIDs = new HashMap<String, UUID>();

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyObject));
			lrefPols = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));

			lstrSQL = "SELECT [PK], [Name] " +
					"FROM (" + lrefObjects.SQLForSelectAll() + ") [Main] WHERE [Policy] IN (SELECT [PK] FROM (" +
					lrefPols.SQLForSelectByMembers(
							new int[] {Policy.I.SUBLINE},
							new java.lang.Object[] {gidShamir},
							null)
					+ ") [AuxPols]);";

			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects = ldb.OpenRecordset(lstrSQL.toString());
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				mmapLensIDs.put(lrsObjects.getString(2), UUID.fromString(lrsObjects.getString(1)));
		}
		catch (Throwable e)
		{
			try { lrsObjects.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects.close();
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
