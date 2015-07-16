package com.premiumminds.BigBang.Jewel.Listings.SubCasualty;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Listings.SubCasualtyListingsBase;
import com.premiumminds.BigBang.Jewel.Objects.Casualty;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.MedicalDetail;
import com.premiumminds.BigBang.Jewel.Objects.MedicalFile;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualtyItem;
import com.premiumminds.BigBang.Jewel.Objects.Template;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;
import com.premiumminds.BigBang.Jewel.SysObjects.Utils;

public class SubCasualtyExternAT
	extends SubCasualtyListingsBase
{
	private static final String gstrAT = "53DB03E7-F423-4656-A23A-9EE9010A5B87";

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
				return o1.getLabel().compareTo(o2.getLabel());
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
					lrefPols.SQLForSelectForReports(new String[] {"[:SubLine:Line:Category]"}, new String[] {" = '" + gstrAT + "'"}, null) +
					") [AuxP1]) OR [Sub Policy] IN (SELECT [PK] FROM (" +
					lrefSubPs.SQLForSelectAll() +
					") [AuxSubP] WHERE [Policy] IN (SELECT [PK] FROM (" +
					lrefPols.SQLForSelectForReports(new String[] {"[:SubLine:Line:Category]"}, new String[] {" = '" + gstrAT + "'"}, null) +
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
		lobjDiv.addElementToRegistry(new Strong("Sinistralidade de Acidentes de Trabalho"));
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
		TR[] larrRows;
		TD lcell;

		larrRows = new TR[1];

		lcell = new TD();
		lcell.setColSpan(2);
		lcell.addElement(buildInner(parrSubCs));
		ReportBuilder.styleInnerContainer(lcell);
		larrRows[0] = ReportBuilder.buildRow(new TD[] {lcell});

		return larrRows;
	}

	protected TD[] buildInnerHeaderRow()
	{
		TD[] larrCells;

		larrCells = new TD[18];

		larrCells[ 0] = ReportBuilder.buildHeaderCell("N. Apólice");
		ReportBuilder.styleCell(larrCells[ 0], false, false);

		larrCells[ 1] = ReportBuilder.buildHeaderCell("N. Processo");
		ReportBuilder.styleCell(larrCells[ 1], false, true);

		larrCells[ 2] = ReportBuilder.buildHeaderCell("Data Sinistro");
		ReportBuilder.styleCell(larrCells[ 2], false, true);

		larrCells[ 3] = ReportBuilder.buildHeaderCell("Sinistrado");
		ReportBuilder.styleCell(larrCells[ 3], false, true);

		larrCells[ 4] = ReportBuilder.buildHeaderCell("Tipo de Lesão");
		ReportBuilder.styleCell(larrCells[ 4], false, true);

		larrCells[ 5] = ReportBuilder.buildHeaderCell("Causa da Lesão");
		ReportBuilder.styleCell(larrCells[ 5], false, true);

		larrCells[ 6] = ReportBuilder.buildHeaderCell("Parte do Corpo");
		ReportBuilder.styleCell(larrCells[ 6], false, true);

		larrCells[ 7] = ReportBuilder.buildHeaderCell("Tipo Incap");
		ReportBuilder.styleCell(larrCells[ 7], false, true);

		larrCells[ 8] = ReportBuilder.buildHeaderCell("% Incap");
		ReportBuilder.styleCell(larrCells[ 8], false, true);

		larrCells[ 9] = ReportBuilder.buildHeaderCell("De");
		ReportBuilder.styleCell(larrCells[ 9], false, true);

		larrCells[10] = ReportBuilder.buildHeaderCell("Até");
		ReportBuilder.styleCell(larrCells[10], false, true);

		larrCells[11] = ReportBuilder.buildHeaderCell("Dias");
		ReportBuilder.styleCell(larrCells[11], false, true);

		larrCells[12] = ReportBuilder.buildHeaderCell("Valor Ind");
		ReportBuilder.styleCell(larrCells[12], false, true);

		larrCells[13] = ReportBuilder.buildHeaderCell("Outras Ind");
		ReportBuilder.styleCell(larrCells[13], false, true);

		larrCells[14] = ReportBuilder.buildHeaderCell("Tipo Ind");
		ReportBuilder.styleCell(larrCells[14], false, true);

		larrCells[15] = ReportBuilder.buildHeaderCell("Local");
		ReportBuilder.styleCell(larrCells[15], false, true);

		larrCells[16] = ReportBuilder.buildHeaderCell("Encerrado");
		ReportBuilder.styleCell(larrCells[16], false, true);

		larrCells[17] = ReportBuilder.buildHeaderCell("Descrição");
		ReportBuilder.styleCell(larrCells[17], false, true);

		setWidths(larrCells);

		return larrCells;
	}

	protected Table buildInner(SubCasualty[] parrSubCs)
		throws BigBangJewelException
	{
		Table ltbl;
		ArrayList<TR> larrRows;
		TR lrow;
		TD[] larrCells;
		ArrayList<TR> larrInner;
		int i;

		getMarkersData();

		larrRows = new ArrayList<TR>();

		lrow = ReportBuilder.buildRow(buildInnerHeaderRow());
		ReportBuilder.styleRow(lrow, true);
		larrRows.add(lrow);

		for ( i = 0; i < parrSubCs.length; i++ )
		{
			larrCells = buildRow(parrSubCs[i]);
			larrInner = buildMedicalRows(parrSubCs[i], larrCells);

			lrow = ReportBuilder.buildRow(larrCells);
			ReportBuilder.styleRow(lrow, false);
			larrRows.add(lrow);

			larrRows.addAll(larrInner);
		}

		ltbl = ReportBuilder.buildTable(larrRows.toArray(new TR[larrRows.size()]));
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
	}

	protected TD[] buildRow(SubCasualty pobjSubC)
		throws BigBangJewelException
	{
		Casualty lobjC;
		SubCasualtyItem[] larrItems;
		ObjectBase lobjType, lobjCause, lobjPart;
		java.lang.Object lobjSettlement, lobjNotes;
		SubCasualtyMarkers lobjMarkers;
		TD[] larrCells;

		lobjC = pobjSubC.GetCasualty();
		larrItems = pobjSubC.GetCurrentItems();

		lobjType = null;
		lobjCause = null;
		lobjPart = null;
		lobjSettlement = null;
		lobjNotes = null;
		if (larrItems.length > 0)
		{
			try
			{
				lobjType = larrItems[0].getAt(SubCasualtyItem.I.INJURYTYPE) == null ? null :
						Engine.GetWorkInstance(Engine.FindEntity(pobjSubC.getNameSpace(), Constants.ObjID_InjuryType),
								(UUID)larrItems[0].getAt(SubCasualtyItem.I.INJURYTYPE));
			}
			catch (Throwable e)
			{
			}

			try
			{
				lobjCause = larrItems[0].getAt(SubCasualtyItem.I.INJURYCAUSE) == null ? null :
						Engine.GetWorkInstance(Engine.FindEntity(pobjSubC.getNameSpace(), Constants.ObjID_InjuryCause),
								(UUID)larrItems[0].getAt(SubCasualtyItem.I.INJURYCAUSE));
			}
			catch (Throwable e)
			{
			}

			try
			{
				lobjPart = larrItems[0].getAt(SubCasualtyItem.I.INJUREDPART) == null ? null :
						Engine.GetWorkInstance(Engine.FindEntity(pobjSubC.getNameSpace(), Constants.ObjID_InjuredPart),
								(UUID)larrItems[0].getAt(SubCasualtyItem.I.INJUREDPART));
			}
			catch (Throwable e)
			{
			}
			lobjSettlement = larrItems[0].getAt(SubCasualtyItem.I.SETTLEMENT);
			lobjNotes = larrItems[0].getAt(SubCasualtyItem.I.NOTES);
		}

		lobjMarkers = null;
		if (mmapData.containsKey(pobjSubC.getKey()))
			lobjMarkers = mmapData.get(pobjSubC.getKey());

		larrCells = new TD[18];

		larrCells[ 0] = ReportBuilder.buildCell(pobjSubC.getAbsolutePolicy().getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[ 0], true, false);

		larrCells[ 1] = ReportBuilder.buildCell(pobjSubC.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[ 1], true, true);

		larrCells[ 2] = ReportBuilder.buildCell(lobjC.getAt(Casualty.I.DATE), TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[ 2], true, true);

		larrCells[ 3] = ReportBuilder.buildCell(pobjSubC.GetObjectName(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[ 3], true, true);

		larrCells[ 4] = ReportBuilder.buildCell(lobjType == null ? "" : lobjType.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[ 4], true, true);

		larrCells[ 5] = ReportBuilder.buildCell(lobjCause == null ? "" : lobjCause.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[ 5], true, true);

		larrCells[ 6] = ReportBuilder.buildCell(lobjPart == null ? "" : lobjPart.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[ 6], true, true);

		larrCells[ 7] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[ 7], true, true);

		larrCells[ 8] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[ 8], true, true);

		larrCells[ 9] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[ 9], true, true);

		larrCells[10] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[10], true, true);

		larrCells[11] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[11], true, true);

		larrCells[12] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[12], true, true);

		larrCells[13] = lobjSettlement == null
				? ReportBuilder.buildCell("", TypeDefGUIDs.T_String)
				: ReportBuilder.buildCell(lobjSettlement, TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[13], true, true);

		larrCells[14] = ReportBuilder.buildCell(lobjNotes == null ? "" : lobjNotes, TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[14], true, true);

		larrCells[15] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[15], true, true);

		larrCells[16] = ReportBuilder.buildCell(lobjMarkers == null ? null : lobjMarkers.mdtClosed, TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[16], true, true);

		larrCells[17] = ReportBuilder.buildCell(lobjC.getAt(Casualty.I.DESCRIPTION), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[17], true, true);

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
		parrCells[ 9].setWidth(100);
		parrCells[10].setWidth(100);
		parrCells[11].setWidth(100);
		parrCells[12].setWidth(100);
		parrCells[13].setWidth(100);
		parrCells[14].setWidth(100);
		parrCells[15].setWidth(100);
		parrCells[16].setWidth(100);
		parrCells[17].setWidth(500);
	}

	private ArrayList<TR> buildMedicalRows(SubCasualty pobjSubC, TD[] parrFirst)
		throws BigBangJewelException
	{
		MedicalFile[] larrMedFs;
		ArrayList<TR> larrRows;
		int i;

		larrMedFs = getFiles(pobjSubC.getKey());

		larrRows = new ArrayList<TR>();

		for (i = 0; i < larrMedFs.length; i++)
		{
			larrRows.addAll(buildDetailRows(larrMedFs[i], parrFirst));
		}

		return larrRows;
	}

	private MedicalFile[] getFiles(UUID pidSubC)
		throws BigBangJewelException
	{
		ArrayList<MedicalFile> larrAux;
		IEntity lrefMedFs;
		MasterDB ldb;
		ResultSet lrsMedFs;

		larrAux = new ArrayList<MedicalFile>();

		try
		{
			lrefMedFs = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_MedicalFile));

			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsMedFs = lrefMedFs.SelectByMembers(ldb, new int[] {MedicalFile.I.SUBCASUALTY}, new java.lang.Object[] {pidSubC},
					new int[] {MedicalFile.I.REFERENCE});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsMedFs.next() )
				larrAux.add(MedicalFile.GetInstance(Engine.getCurrentNameSpace(), lrsMedFs));
		}
		catch (Throwable e)
		{
			try { lrsMedFs.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsMedFs.close();
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

		return larrAux.toArray(new MedicalFile[larrAux.size()]);
	}

	private ArrayList<TR> buildDetailRows(MedicalFile pobjMedF, TD[] parrFirst)
		throws BigBangJewelException
	{
		MedicalDetail[] larrMedDets;
		ArrayList<TR> larrRows;
		TR lrow;
		int i;

		larrMedDets = pobjMedF.GetCurrentDetails();

		larrRows = new ArrayList<TR>();

		if (larrMedDets.length > 0)
		{
			i = 0;
			if (parrFirst != null)
			{
				buildMedicalDetailRow(larrMedDets[i], parrFirst);
				i++;
			}

			for (; i < larrMedDets.length; i++)
			{
				lrow = ReportBuilder.buildRow(buildMedicalDetailRow(larrMedDets[i], null));
				ReportBuilder.styleRow(lrow, false);
				larrRows.add(lrow);
			}
		}

		return larrRows;
	}

	private TD[] buildMedicalDetailRow(MedicalDetail pobjMedDet, TD[] parrFirst)
	{
		ObjectBase lobjType;
		TD[] larrCells;
		Timestamp ldtFrom;
		Timestamp ldtTo;
		Integer llngDays;

		lobjType = null;
		try
		{
			lobjType = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_DisabilityType),
					(UUID)pobjMedDet.getAt(MedicalDetail.I.DISABILITYTYPE));
		}
		catch (Throwable e)
		{
		}

		ldtFrom = (Timestamp)pobjMedDet.getAt(MedicalDetail.I.STARTDATE);
		ldtTo = (Timestamp)pobjMedDet.getAt(MedicalDetail.I.ENDDATE);
		llngDays = (((ldtFrom == null) || (ldtTo == null)) ? null
				: (int)TimeUnit.DAYS.convert(ldtTo.getTime() - ldtFrom.getTime() + 43200000L, TimeUnit.MILLISECONDS));

		larrCells = parrFirst == null ? new TD[18] : parrFirst;

		larrCells[ 7] = ReportBuilder.buildCell(lobjType == null ? null : lobjType.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[ 7], true, true);

		larrCells[ 8] = ReportBuilder.buildCell(pobjMedDet.getAt(MedicalDetail.I.PERCENT), TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[ 8], true, true);

		larrCells[ 9] = ReportBuilder.buildCell(ldtFrom, TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[ 9], true, true);

		larrCells[10] = ReportBuilder.buildCell(ldtTo, TypeDefGUIDs.T_Date);
		ReportBuilder.styleCell(larrCells[10], true, true);

		larrCells[11] = ReportBuilder.buildCell(llngDays, TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[11], true, true);

		larrCells[12] = ReportBuilder.buildCell(pobjMedDet.getAt(MedicalDetail.I.BENEFITS), TypeDefGUIDs.T_Decimal);
		ReportBuilder.styleCell(larrCells[12], true, true);

		larrCells[15] = ReportBuilder.buildCell(pobjMedDet.getAt(MedicalDetail.I.PLACE), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[15], true, true);

		if (parrFirst == null)
		{
			larrCells[ 0] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(larrCells[ 0], true, false);
		
			larrCells[ 1] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(larrCells[ 1], true, true);
		
			larrCells[ 2] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(larrCells[ 2], true, true);
		
			larrCells[ 3] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(larrCells[ 3], true, true);
		
			larrCells[ 4] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(larrCells[ 4], true, true);
		
			larrCells[ 5] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(larrCells[ 5], true, true);
		
			larrCells[ 6] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(larrCells[ 6], true, true);

			larrCells[13] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(larrCells[13], true, true);

			larrCells[14] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(larrCells[14], true, true);

			larrCells[16] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(larrCells[16], true, true);

			larrCells[17] = ReportBuilder.buildCell("", TypeDefGUIDs.T_String);
			ReportBuilder.styleCell(larrCells[17], true, true);

			setWidths(larrCells);
		}

		return larrCells;
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
