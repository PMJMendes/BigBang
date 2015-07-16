package com.premiumminds.BigBang.Jewel.Listings.SubCasualty;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.ecs.GenericElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.IMG;
import org.apache.ecs.html.Strong;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualtyListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.Assessment;
import com.premiumminds.BigBang.Jewel.Objects.Casualty;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubLine;
import com.premiumminds.BigBang.Jewel.Objects.Template;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

public class SubCasualtyExternAuto
	extends SubCasualtyListingsBase
{
	private static final String gstrAuto = "7F5F77EB-8348-4914-8525-9EE9010AB1C6";

	protected static class SubCasualtyValues
	{
		public BigDecimal mdblDirects;
		public BigDecimal mdblThirds;
		public BigDecimal mdblDeductibles;
	}

	protected HashMap<UUID, SubCasualtyValues> mmapValues;

	public GenericElement[] doReport(String[] parrParams)
		throws BigBangJewelException
	{
		SubCasualty[] larrAux;
		GenericElement[] larrResult;

		if ( (parrParams[0] == null) || "".equals(parrParams[0]) )
			return doNotValid();

		larrAux = getPendingForOperation(parrParams);

		Arrays.sort(larrAux, new Comparator<SubCasualty>()
		{
			public int compare(SubCasualty o1, SubCasualty o2)
			{
				SubLine s1, s2;

				s1 = null;
				s2 = null;
				try
				{
					s1 = o1.GetSubLine();
					s2 = o2.GetSubLine();
				}
				catch (Throwable e)
				{
				}

				if ((s1 == null) || (s2 == null) || s2.getDescription().equals(s1.getDescription()))
				{
					return o1.getLabel().compareTo(o2.getLabel());
				}

				return s1.getDescription().compareTo(s2.getDescription());
			}
		});

		larrResult = new GenericElement[2];

		larrResult[0] = buildHeaderSection(parrParams[0]);

		try
		{
			larrResult[1] = buildDataSection("", larrAux, true);
		}
		catch (BigBangJewelException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrResult;
	}

	protected SubCasualty[] getPendingForOperation(String[] parrParams)
		throws BigBangJewelException
	{
		StringBuilder lstrSQL;
		ArrayList<SubCasualty> larrAux;
		IEntity lrefSubCs;
		IEntity lrefPols;
		IEntity lrefSubPs;
		IEntity lrefCas;
		IEntity lrefLogs;
		MasterDB ldb;
		ResultSet lrsSubCs;

		if ( Utils.getCurrentAgent() != null )
			return new SubCasualty[0];

		lstrSQL = new StringBuilder();
		try
		{
			lrefSubCs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualty));
			lrefPols = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Policy));
			lrefSubPs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubPolicy));
			lrefCas = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Casualty));
			lrefLogs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Jewel.Petri.Constants.ObjID_PNLog));

			lstrSQL.append("SELECT * FROM (" +
					lrefSubCs.SQLForSelectAll() + ") [AuxSubC] WHERE (([Policy] IN (SELECT [PK] FROM (" +
					lrefPols.SQLForSelectForReports(new String[] {"[:SubLine:Line:Category]"}, new String[] {" = '" + gstrAuto + "'"}, null) +
					") [AuxP1]) OR [Sub Policy] IN (SELECT [PK] FROM (" +
					lrefSubPs.SQLForSelectAll() +
					") [AuxSubP] WHERE [Policy] IN (SELECT [PK] FROM (" +
					lrefPols.SQLForSelectForReports(new String[] {"[:SubLine:Line:Category]"}, new String[] {" = '" + gstrAuto + "'"}, null) +
					") [AuxP2]))) AND [Casualty] IN (SELECT [PK] FROM (" +
					lrefCas.SQLForSelectByMembers(new int[] {Casualty.I.CLIENT}, new java.lang.Object[] {parrParams[0]}, null) +
					") [AuxCas] WHERE 1=1");
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if (parrParams[1] != null)
		{
			lstrSQL.append(" AND [Date] >= '").append(parrParams[1]).append("'");
		}

		if (parrParams[2] != null)
		{
			lstrSQL.append(" AND [Date] < DATEADD(d, 1, '").append(parrParams[2]).append("')");
		}

		lstrSQL.append(")");

		if ((parrParams[3] != null) || (parrParams[4] != null))
		{
			try
			{
				lstrSQL.append(" AND [Process] IN (SELECT [Process] FROM (")
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

			if ( parrParams[3] != null )
				lstrSQL.append(" AND [Timestamp] >= '").append(parrParams[3]).append("'");

			if ( parrParams[4] != null )
				lstrSQL.append(" AND [Timestamp] < DATEADD(d, 1, '").append(parrParams[4]).append("')");

			lstrSQL.append(")");
		}

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

	protected Table buildHeaderSection(String lstrClient)
		throws BigBangJewelException
	{
		Client lobjClient;
		Template lobjLogo;
		Table ltbl;
		TR[] larrRows;
		TD[] larrCells;
		FileXfer lobjFile;
		String lstr64;
		IMG lobjImg;
		Div lobjDiv;

		try
		{
			lobjClient = Client.GetInstance(Engine.getCurrentNameSpace(), UUID.fromString(lstrClient));
			lobjLogo = Template.GetInstance(Engine.getCurrentNameSpace(), Constants.TID_Logo);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrRows = new TR[1];

		larrCells = new TD[2];

		larrCells[0] = new TD();
		lobjFile = lobjLogo.getFile();
		if ( lobjFile != null )
		{
			lstr64 = Base64.encodeBase64String(lobjFile.getData());
			lobjImg = new IMG();
			lobjImg.setSrc("data:" + lobjFile.getContentType() + ";base64," + lstr64);
			larrCells[0].addElementToRegistry(lobjImg);
		}

		larrCells[1] = new TD();
		lobjDiv = new Div();
		lobjDiv.setStyle("font-size: small;");
		larrCells[1].addElementToRegistry(lobjDiv);
		larrCells[1].setAlign("right");
		lobjDiv.addElementToRegistry(new Strong(lobjClient.getLabel()));
		lobjDiv.addElementToRegistry(new BR());
		lobjDiv.addElementToRegistry(new Strong("Sinistralidade do Cliente"));
		lobjDiv.addElementToRegistry(new BR());
		lobjDiv.addElementToRegistry(ReportBuilder.BuildValue(TypeDefGUIDs.T_Date, new Timestamp(new java.util.Date().getTime())));

		larrRows[0] = ReportBuilder.buildRow(larrCells);

		ltbl = ReportBuilder.buildTable(larrRows);
		ltbl.setWidth("100%");

		return ltbl;
	}

	protected TR[] buildDataTable(String pstrHeader, SubCasualty[] parrSubCs)
		throws BigBangJewelException
	{
		BigDecimal ldblDirects;
		BigDecimal ldblThirds;
		SubCasualtyValues lobjValues;
		int i;
		TR[] larrRows;
		TD lcell;

		getValuesData();

		ldblDirects = BigDecimal.ZERO;
		ldblThirds = BigDecimal.ZERO;
		for ( i = 0; i < parrSubCs.length; i++ )
		{
			if (mmapValues.containsKey(parrSubCs[i].getKey()))
			{
				lobjValues = mmapValues.get(parrSubCs[i].getKey());
				ldblDirects = ldblDirects.add(lobjValues.mdblDirects);
				ldblThirds = ldblThirds.add(lobjValues.mdblThirds);
			}
		}

		larrRows = new TR[4];

		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInner(parrSubCs));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[0] = ReportBuilder.buildRow(new TD[] {lcell});

		lcell = ReportBuilder.buildHeaderCell("");
		lcell.setColSpan(2);
		ReportBuilder.styleCell(lcell, true, false);
		larrRows[1] = ReportBuilder.buildRow(new TD[] {lcell});
		ReportBuilder.styleRow(larrRows[1], false);

		larrRows[2] = ReportBuilder.constructDualRow("Total de Pagamentos a Terceiros", ldblThirds, TypeDefGUIDs.T_Decimal, false);

		larrRows[3] = ReportBuilder.constructDualRow("Total de Pagamentos ao Tomador", ldblDirects, TypeDefGUIDs.T_Decimal, false);

		return larrRows;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[10];

		larrCells[0] = ReportBuilder.buildHeaderCell("N. Apólice");
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("N. Processo");
		ReportBuilder.styleCell(larrCells[1], false, true);

		larrCells[2] = ReportBuilder.buildHeaderCell("Data Sinistro");
		ReportBuilder.styleCell(larrCells[2], false, true);

		larrCells[3] = ReportBuilder.buildHeaderCell("Matricula");
		ReportBuilder.styleCell(larrCells[3], false, true);

		larrCells[4] = ReportBuilder.buildHeaderCell("Data 1ª Perit");
		ReportBuilder.styleCell(larrCells[4], false, true);

		larrCells[5] = ReportBuilder.buildHeaderCell("Responsabilidade");
		ReportBuilder.styleCell(larrCells[5], false, true);

		larrCells[6] = ReportBuilder.buildHeaderCell("Pagamentos a Terceiros");
		ReportBuilder.styleCell(larrCells[6], false, true);

		larrCells[7] = ReportBuilder.buildHeaderCell("Pagamentos ao Tomador");
		ReportBuilder.styleCell(larrCells[7], false, true);

		larrCells[8] = ReportBuilder.buildHeaderCell("Encerrado");
		ReportBuilder.styleCell(larrCells[8], false, true);

		larrCells[9] = ReportBuilder.buildHeaderCell("Descrição");
		ReportBuilder.styleCell(larrCells[9], false, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected TD[] buildRow(SubCasualty pobjSubC)
		throws BigBangJewelException
	{
		Casualty lobjC;
		Assessment lobjA;
		SubCasualtyValues lobjValues;
		SubCasualtyMarkers lobjMarkers;
		TD[] larrCells;

		lobjC = pobjSubC.GetCasualty();
		lobjA = getFirstEffectiveAssessment(pobjSubC.getKey());

		lobjValues = null;
		if (mmapValues.containsKey(pobjSubC.getKey()))
			lobjValues = mmapValues.get(pobjSubC.getKey());
		lobjMarkers = null;
		if (mmapData.containsKey(pobjSubC.getKey()))
			lobjMarkers = mmapData.get(pobjSubC.getKey());

		larrCells = new TD[10];

		larrCells[0] = ReportBuilder.buildCell(pobjSubC.getAbsolutePolicy().getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(pobjSubC.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[1], true, true);

		larrCells[2] = ReportBuilder.buildCell(lobjC.getAt(Casualty.I.DATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[2], true, true);

		larrCells[3] = ReportBuilder.buildCell(pobjSubC.GetObjectName(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[3], true, true);

		larrCells[4] = ReportBuilder.buildCell(lobjA == null ? null : lobjA.getAt(Assessment.I.EFFECTIVEDATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[4], true, true);

		larrCells[5] = ReportBuilder.buildCell(lobjC.getAt(Casualty.I.PERCENTFAULT), TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[5], true, true);

		larrCells[6] = ReportBuilder.buildCell(lobjValues == null ? new BigDecimal(0) : lobjValues.mdblThirds, TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[6], true, true);

		larrCells[7] = ReportBuilder.buildCell(lobjValues == null ? new BigDecimal(0) : lobjValues.mdblDirects, TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[7], true, true);

		larrCells[8] = ReportBuilder.buildCell(lobjMarkers == null ? null : lobjMarkers.mdtClosed, TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[8], true, true);

		larrCells[9] = ReportBuilder.buildCell(lobjC.getAt(Casualty.I.DESCRIPTION), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[9], true, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected void setWidths(TD[] parrCells)
	{
		parrCells[ 0].setWidth(100);
		parrCells[ 1].setWidth(100);
		parrCells[ 2].setWidth(100);
		parrCells[ 3].setWidth(100);
		parrCells[ 4].setWidth(100);
		parrCells[ 5].setWidth(100);
		parrCells[ 6].setWidth(100);
		parrCells[ 7].setWidth(100);
		parrCells[ 8].setWidth(100);
		parrCells[ 9].setWidth(500);
	}

	protected void getValuesData()
		throws BigBangJewelException
	{
		IEntity lrefSubCDets;
		String lstrSQL;
		MasterDB ldb;
		ResultSet lrsSubCDets;
		UUID lidAux;
		SubCasualtyValues lobjAux;

		if ( mmapValues != null )
			return;

		try
		{
			lrefSubCDets = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_SubCasualtyItem));

			lstrSQL = "SELECT [Sub Casualty], COALESCE(SUM(CASE [Third Party] WHEN 0 THEN [Settlement] ELSE 0.0 END), 0.0) Directs, " +
					"COALESCE(SUM(CASE [Third Party] WHEN 1 THEN [Settlement] ELSE 0.0 END), 0) Thirds, " +
					"COALESCE(SUM([Expected Deductible]), 0) Deductibles FROM (" +
					lrefSubCDets.SQLForSelectAll() + ") [Main] GROUP BY [Sub Casualty];";

			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubCDets = ldb.OpenRecordset(lstrSQL.toString());
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		mmapValues = new HashMap<UUID, SubCasualtyValues>();

		try
		{
			while ( lrsSubCDets.next() )
			{
				lidAux = UUID.fromString(lrsSubCDets.getString(1));
				lobjAux = new SubCasualtyValues();
				lobjAux.mdblDirects = lrsSubCDets.getBigDecimal(2);
				lobjAux.mdblThirds = lrsSubCDets.getBigDecimal(3);
				lobjAux.mdblDeductibles = lrsSubCDets.getBigDecimal(4);
				mmapValues.put(lidAux, lobjAux);
			}
		}
		catch (Throwable e)
		{
			try { lrsSubCDets.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubCDets.close();
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

	private Assessment getFirstEffectiveAssessment(UUID pidSubC)
		throws BigBangJewelException
	{
		Assessment larrAux;
		IEntity lrefAssessments;
		MasterDB ldb;
		ResultSet lrsAssessments;

		larrAux = null;

		try
		{
			lrefAssessments = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Assessment));

			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsAssessments = lrefAssessments.SelectByMembers(ldb, new int[] {Assessment.I.SUBCASUALTY}, new java.lang.Object[] {pidSubC},
					new int[] {Assessment.I.EFFECTIVEDATE});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsAssessments.next() )
			{
				larrAux = Assessment.GetInstance(Engine.getCurrentNameSpace(), lrsAssessments);
				if (larrAux.getAt(Assessment.I.EFFECTIVEDATE) != null)
					break;
				larrAux = null;
			}
		}
		catch (Throwable e)
		{
			try { lrsAssessments.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsAssessments.close();
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

		return larrAux;
	}

	private GenericElement[] doNotValid()
	{
		TR[] larrRows;
		Table ltbl;

		larrRows = new TR[1];
		larrRows[0] = ReportBuilder.constructDualHeaderRowCell("Tem que indicar o cliente.");

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return new GenericElement[] {ltbl};
	}
}
