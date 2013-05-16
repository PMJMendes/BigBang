package com.premiumminds.BigBang.Jewel.SysObjects;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.FileData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.FileImportDetail;
import com.premiumminds.BigBang.Jewel.Objects.FileImportSession;

public abstract class FileIOBase
	implements Runnable
{
//	public UUID midUser;
//	public UUID midNameSpace;
	public String mstrFileName;
	public UUID midFormat;
	public FileData mobjData;
	public UUID midSession;

	public abstract void Parse() throws BigBangJewelException;
	public abstract UUID GetStatusTable() throws BigBangJewelException;

	public void run()
	{
		try
		{
			Parse();
		}
		catch (BigBangJewelException e)
		{
		}
	}

	protected void createSession(SQLServer pdb)
		throws BigBangJewelException
	{
		FileImportSession lobjSession;

		lobjSession = FileImportSession.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
		try
		{
			lobjSession.setAt(FileImportSession.I.NAME,      mstrFileName);
			lobjSession.setAt(FileImportSession.I.USER,      Engine.getCurrentUser());
			lobjSession.setAt(FileImportSession.I.TIMESTAMP, new Timestamp(new java.util.Date().getTime()));
			lobjSession.setAt(FileImportSession.I.FORMAT,    midFormat);
			lobjSession.setAt(FileImportSession.I.FILE,      null);
			lobjSession.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		midSession = lobjSession.getKey();
	}

	protected void createDetail(SQLServer pdb, String pstrText, int plngNumber, UUID pidStatus, UUID pidObject)
		throws BigBangJewelException
	{
		FileImportDetail lobjDetail;

		lobjDetail = FileImportDetail.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
		try
		{
			lobjDetail.setAt(FileImportDetail.I.LINE,       pstrText);
			lobjDetail.setAt(FileImportDetail.I.SESSION,    midSession);
			lobjDetail.setAt(FileImportDetail.I.NUMBER,     plngNumber);
			lobjDetail.setAt(FileImportDetail.I.STATUS,     pidStatus);
			lobjDetail.setAt(FileImportDetail.I.CREATEDOBJ, pidObject);
			lobjDetail.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	protected void createReport(SQLServer pdb, String pstrTitle, UUID pidReport, String pstrParams)
		throws BigBangJewelException
	{
		Timestamp ldtAux;
		Calendar ldtAux2;
		AgendaItem lobjItem;

		ldtAux = new Timestamp(new java.util.Date().getTime());
    	ldtAux2 = Calendar.getInstance();
    	ldtAux2.setTimeInMillis(ldtAux.getTime());
    	ldtAux2.add(Calendar.DAY_OF_MONTH, 7);

    	try
    	{
			lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjItem.setAt(0, pstrTitle);
			lobjItem.setAt(1, Engine.getCurrentUser());
			lobjItem.setAt(2, Constants.ProcID_GenSys);
			lobjItem.setAt(3, ldtAux);
			lobjItem.setAt(4, new Timestamp(ldtAux2.getTimeInMillis()));
			lobjItem.setAt(5, Constants.UrgID_Completed);
			lobjItem.setAt(6, null);
			lobjItem.setAt(7, pstrParams);
			lobjItem.setAt(8, pidReport);
			lobjItem.SaveToDb(pdb);
			lobjItem.InitNew(new UUID[] {}, new UUID[] {}, pdb);
    	}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
