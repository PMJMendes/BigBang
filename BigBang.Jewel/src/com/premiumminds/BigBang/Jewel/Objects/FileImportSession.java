package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.apache.ecs.GenericElement;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;

import Jewel.Engine.Engine;
import Jewel.Engine.Constants.ObjectGUIDs;
import Jewel.Engine.Constants.TypeDefGUIDs;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.FileIOBase;
import com.premiumminds.BigBang.Jewel.SysObjects.ReportBuilder;

public class FileImportSession
	extends ObjectBase
{
	public static class I
	{
		public static int NAME      = 0;
		public static int USER      = 1;
		public static int TIMESTAMP = 2;
		public static int FORMAT    = 3;
		public static int FILE      = 4;
	}

    public static FileImportSession GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (FileImportSession)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_FileImportSession), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static FileImportSession GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (FileImportSession)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_FileImportSession), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize()
		throws JewelEngineException
	{
	}

	public User getUser()
		throws BigBangJewelException
	{
		try
		{
			return User.GetInstance(getNameSpace(), (UUID)getAt(I.USER));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public String getFormat()
		throws BigBangJewelException
	{
		ObjectBase lobjAux;

		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(getNameSpace(), ObjectGUIDs.O_FileSpec), (UUID)getAt(I.FORMAT));
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjAux.getLabel();
	}

	public FileImportDetail[] getDetails()
		throws BigBangJewelException
	{
		ArrayList<FileImportDetail> larrAux;
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;

		larrAux = new ArrayList<FileImportDetail>();

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_FileImportDetail)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {FileImportDetail.I.SESSION}, new java.lang.Object[] {getKey()},
					new int[] {FileImportDetail.I.STATUS});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				larrAux.add(FileImportDetail.GetInstance(getNameSpace(), lrsObjects));
		}
		catch (Throwable e)
		{
			try { lrsObjects.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
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

		return larrAux.toArray(new FileImportDetail[larrAux.size()]);
	}

	public GenericElement[] printReport(String[] parrParams)
		throws BigBangJewelException
	{
		FileImportDetail[] larrDetails;
		HashMap<UUID, ArrayList<FileImportDetail>> larrMap;
		ArrayList<FileImportDetail> larrAux;
		UUID lidStatus;
		GenericElement[] larrResult;
		int i;

		larrDetails = getDetails();
		larrMap = new HashMap<UUID, ArrayList<FileImportDetail>>();
		for ( i = 0; i < larrDetails.length; i++ )
		{
			lidStatus = (UUID)larrDetails[i].getAt(FileImportDetail.I.STATUS);
			larrAux = larrMap.get(lidStatus);
			if ( larrAux == null )
			{
				larrAux = new ArrayList<FileImportDetail>();
				larrMap.put(lidStatus, larrAux);
			}
			larrAux.add(larrDetails[i]);
		}

		larrResult = new GenericElement[larrMap.size() + 1];
		larrResult[0] = buildHeader(larrDetails.length);
		i = 1;
		for ( UUID lid: larrMap.keySet() )
		{
			larrResult[i] = buildSection(lid, larrMap.get(lid));
			i++;
		}

		return larrResult;
	}

    private Table buildHeader(int plngCount)
    	throws BigBangJewelException
    {
    	Table ltbl;
    	TR[] larrRows;

		larrRows = new TR[6];

		larrRows[0] = ReportBuilder.constructDualHeaderRowCell("Importação de Ficheiro");

		larrRows[1] = ReportBuilder.constructDualRow("Nome do Ficheiro", getLabel(), TypeDefGUIDs.T_String);

		larrRows[2] = ReportBuilder.constructDualRow("Importado por", getUser().getDisplayName(), TypeDefGUIDs.T_String);

		larrRows[3] = ReportBuilder.constructDualRow("Importado em", getAt(I.TIMESTAMP), TypeDefGUIDs.T_Date);

		larrRows[4] = ReportBuilder.constructDualRow("Formato", getFormat(), TypeDefGUIDs.T_String);

		larrRows[4] = ReportBuilder.constructDualRow("Total de Linhas", plngCount, TypeDefGUIDs.T_Integer);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, false);

		return ltbl;
    }

    private Table buildSection(UUID pidStatus, ArrayList<FileImportDetail> parrDetails)
    	throws BigBangJewelException
    {
    	ObjectBase lobjStatus;
		Table ltbl;
		TR[] larrRows;
		int i;

		try
		{
			lobjStatus = Engine.GetWorkInstance(Engine.FindEntity(getNameSpace(), FileIOBase.ObjID_ImportStatus), pidStatus);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		larrRows = new TR[parrDetails.size() + 2];

		larrRows[0] = ReportBuilder.buildRow(buildSectionHeader(lobjStatus.getLabel()));
		ReportBuilder.styleRow(larrRows[0], true);

		for ( i = 1; i <= parrDetails.size(); i++ )
		{
			larrRows[i] = ReportBuilder.buildRow(buildSectionRow(parrDetails.get(i - 1)));
			ReportBuilder.styleRow(larrRows[i], false);
		}

		larrRows[parrDetails.size() + 1] = ReportBuilder.buildRow(buildSectionEndRow(parrDetails.size()));
		ReportBuilder.styleRow(larrRows[0], true);

		ltbl = ReportBuilder.buildTable(larrRows);
		ReportBuilder.styleTable(ltbl, true);

		return ltbl;
    }

	private TD[] buildSectionHeader(String pstrHeader)
	{
		TD[] larrCells;

		larrCells = new TD[2];

		larrCells[0] = ReportBuilder.buildHeaderCell(pstrHeader);
		ReportBuilder.styleCell(larrCells[0], false, false);

		larrCells[1] = ReportBuilder.buildHeaderCell("Linha nº");
		ReportBuilder.styleCell(larrCells[1], false, true);

		return larrCells;
	}

	private TD[] buildSectionRow(FileImportDetail pobjDetail)
	{
		TD[] larrCells;

		larrCells = new TD[2];

		larrCells[0] = ReportBuilder.buildCell(pobjDetail.getLabel(), TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(pobjDetail.getAt(FileImportDetail.I.NUMBER), TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[1], true, true);

		return larrCells;
	}

	private TD[] buildSectionEndRow(int plngCount)
	{
		TD[] larrCells;

		larrCells = new TD[2];

		larrCells[0] = ReportBuilder.buildCell("Total", TypeDefGUIDs.T_String);
		ReportBuilder.styleCell(larrCells[0], true, false);

		larrCells[1] = ReportBuilder.buildCell(plngCount, TypeDefGUIDs.T_Integer);
		ReportBuilder.styleCell(larrCells[1], true, true);

		return larrCells;
	}
}
