package com.premiumminds.BigBang.Jewel.Listings.Policy;

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

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.PolicyObject;
import com.premiumminds.BigBang.Jewel.Objects.PolicyValue;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class PolicySpecialShamir
{
	protected static final UUID gidShamir = UUID.fromString("E91E32C6-0C55-4F49-BA88-9F340153FD6D");
	protected static final UUID gidCost = UUID.fromString("BF80A9FC-AD3F-4630-8DD3-9F9601565146");
	protected static final UUID gidDate = UUID.fromString("64883643-ED00-498E-82D4-9F960156930B");

	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		PolicyObject[] larrAux;
		HashMap<UUID, ArrayList<PolicyObject>> larrMap;
		int i;
		UUID lidPolicy;
		GenericElement[] larrResult;

		larrAux = getHistoryForOperation(parrParams);

		larrMap = new HashMap<UUID, ArrayList<PolicyObject>>();
		for ( i = 0; i < larrAux.length; i++ )
		{
			lidPolicy = larrAux[i].GetOwner().getKey();
			if ( larrMap.get(lidPolicy) == null )
				larrMap.put(lidPolicy, new ArrayList<PolicyObject>());
			larrMap.get(lidPolicy).add(larrAux[i]);
		}

		larrResult = new GenericElement[larrMap.size() + 1];

		larrResult[0] = buildHeaderSection("Shamir - Activação de Lentes", larrAux, parrParams[0], parrParams[1]);

		i = 1;
		for ( UUID lid: larrMap.keySet() )
		{
			try
			{
				larrResult[i] = buildDataSection("Apólice " + Policy.GetInstance(Engine.getCurrentNameSpace(), lid).getLabel(),
						larrMap.get(lid));
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

	protected PolicyObject[] getHistoryForOperation(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<PolicyObject> larrAux;
		IEntity lrefObjects, lrefPolicies;
		MasterDB ldb;
		ResultSet lrsObjects;

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_PolicyObject));
			lrefPolicies = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));

			lstrSQL = new StringBuilder();
			lstrSQL.append("SELECT * FROM (")
					.append(lrefObjects.SQLForSelectAll()).append(") [AuxObj] WHERE [Policy] IN (SELECT [PK] FROM(")
					.append(lrefPolicies.SQLForSelectByMembers(
							new int[] {Policy.I.SUBLINE}, new java.lang.Object[] {gidShamir}, null))
					.append(") [AuxPols])");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( parrParams[0] != null )
			lstrSQL.append(" AND [Inclusion Date] >= '").append(parrParams[0]).append("'");

		if ( parrParams[1] != null )
			lstrSQL.append(" AND [Inclusion Date] < DATEADD(d, 1, '").append(parrParams[1]).append("')");

		larrAux = new ArrayList<PolicyObject>();

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
				larrAux.add(PolicyObject.GetInstance(Engine.getCurrentNameSpace(), lrsObjects));
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

		return larrAux.toArray(new PolicyObject[larrAux.size()]);
	}

	protected Table buildHeaderSection(String pstrHeader, PolicyObject[] parrObjects, String pstrFrom, String pstrTo)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;

		larrRows = new TR[4];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		larrRows[1] = ReportBuilder.constructDualRow("De", pstrFrom, TypeDefGUIDs.T_String, false);

		larrRows[2] = ReportBuilder.constructDualRow("Até", pstrTo, TypeDefGUIDs.T_String, false);

		larrRows[3] = ReportBuilder.constructDualRow("Nº de Lentes", parrObjects.length, TypeDefGUIDs.T_Integer, false);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected Table buildDataSection(String pstrHeader, ArrayList<PolicyObject> parrObjects)
		throws BigBangJewelException
	{
		return buildDataSection(pstrHeader, parrObjects.toArray(new PolicyObject[parrObjects.size()]));
	}

	protected Table buildDataSection(String pstrHeader, PolicyObject[] parrObjects)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;

		larrRows = buildDataTable(pstrHeader, parrObjects);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
	}

	protected TR[] buildDataTable(String pstrHeader, PolicyObject[] parrObjects)
		throws BigBangJewelException
	{
		TR[] larrRows;
		TD lcell;

		larrRows = new TR[3];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell(pstrHeader);

		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInner(parrObjects));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[1] = ReportBuilder.buildRow(new TD[] {lcell});

		larrRows[2] = ReportBuilder.constructDualRow("Nº de Lentes", parrObjects.length, TypeDefGUIDs.T_Integer, false);

		return larrRows;
	}

	protected Table buildInner(PolicyObject[] parrObjects)
		throws BigBangJewelException
	{
		Table ltbl;
		TR[] larrRows;
		int i;

		larrRows = new TR[parrObjects.length + 1];

		larrRows[0] = ReportBuilder.buildRow(buildInnerHeaderRow());
		ReportBuilder.styleRow(larrRows[0], true);

		for ( i = 1; i <= parrObjects.length; i++ )
		{
			larrRows[i] = ReportBuilder.buildRow(buildRow(parrObjects[i - 1]));
			ReportBuilder.styleRow(larrRows[i], false);
		}

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[6];

		larrCells[0] = ReportBuilder.buildHeaderCell("Número");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("Tipo");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Custo");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Venda");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Activação");
		ReportBuilder.styleCell(larrCells[4], false, true);

		larrCells[5] = ReportBuilder.buildHeaderCell("Exclusão");
		ReportBuilder.styleCell(larrCells[5], false, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected TD[] buildRow(PolicyObject pobjObject)
		throws BigBangJewelException
	{
		PolicyValue[] larrValues;
		int i;
		String lstrCost;
		String lstrDate;
		TD[] larrCells;

		lstrCost = null;
		lstrDate = null;
		try
		{
			larrValues = pobjObject.GetOwner().GetCurrentKeyedValues(pobjObject.getKey(), null);
			for ( i = 0; i < larrValues.length; i++ )
			{
				if ( gidCost.equals(larrValues[i].GetTax().getKey()) )
					lstrCost = larrValues[i].GetValue();
				else if ( gidDate.equals(larrValues[i].GetTax().getKey()) )
					lstrDate = larrValues[i].GetValue();
					
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrCells = new TD[6];

		larrCells[0] = ReportBuilder.buildCell(pobjObject.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(pobjObject.getAt(PolicyObject.I.MAKEANDMODEL), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(lstrCost, TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(lstrDate, TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(pobjObject.getAt(PolicyObject.I.INCLUSIONDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell(pobjObject.getAt(PolicyObject.I.EXCLUSIONDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[5], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected void setWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth(100);
		parrCells[ 1].setWidth(300);
		parrCells[ 2].setWidth(100);
		parrCells[ 3].setWidth(100);
		parrCells[ 4].setWidth(100);
		parrCells[ 5].setWidth(100);
	}
}
