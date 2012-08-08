package com.premiumminds.BigBang.Jewel.SysObjects;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.SysObjects.FileData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
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
}
