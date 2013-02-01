package com.premiumminds.BigBang.Jewel.Objects;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.FileSpec;
import Jewel.Engine.SysObjects.FileData;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.FileIOBase;

public class FileProcesser
	extends ObjectBase
{
    public static FileProcesser GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (FileProcesser)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_FileProcesser), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static FileProcesser GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (FileProcesser)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_FileProcesser), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static FileProcesser GetProcesserForFormat(UUID pidFormat)
		throws BigBangJewelException
	{
		Entity lrefProcessers;
        MasterDB ldb;
        ResultSet lrsProcessers;
        FileProcesser lobjProc;

		lobjProc = null;

		try
		{
			lrefProcessers = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_FileProcesser));
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

        try
        {
	        lrsProcessers = lrefProcessers.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {pidFormat}, null);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
	        if (lrsProcessers.next())
	        	lobjProc = FileProcesser.GetInstance(Engine.getCurrentNameSpace(), lrsProcessers);
        }
        catch (Throwable e)
        {
			try { lrsProcessers.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
        	throw new BigBangJewelException(e.getMessage(), e);
        }

        try
        {
        	lrsProcessers.close();
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

		return lobjProc;
	}

    Constructor<?> mrefConst;

	public void Initialize()
		throws JewelEngineException
	{
	    Class<?> lrefClass;

		try
		{
			lrefClass = Class.forName(((String)getAt(1)).replaceAll("MADDS", "Jewel"));
			mrefConst = lrefClass.getConstructor(new Class<?>[] {});
		}
		catch (Throwable e)
		{
	    	throw new JewelEngineException(e.getMessage(), e);
		}
	}

	public UUID GetStatusTable()
		throws BigBangJewelException
	{
		if ( mrefConst == null )
			throw new BigBangJewelException("Erro inesperado a inicializar o processador de ficheiros.");

		try
		{
			return ((FileIOBase)mrefConst.newInstance()).GetStatusTable();
		} 
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Process(FileXfer pobjFile, UUID pidFormat)
		throws BigBangJewelException
	{
		FileSpec lobjSpec;
		FileData lobjData;
		FileIOBase lobjProcesser;

		if ( mrefConst == null )
			throw new BigBangJewelException("Erro inesperado a inicializar o processador de ficheiros.");

		try
		{
			lobjSpec = FileSpec.GetInstance(Engine.getCurrentNameSpace(), pidFormat);
			lobjData = lobjSpec.ParseFile(pobjFile);
			lobjProcesser = (FileIOBase)mrefConst.newInstance();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

//		lobjProcesser.midUser = Engine.getCurrentUser();
//		lobjProcesser.midNameSpace = Engine.getCurrentNameSpace();
		lobjProcesser.mstrFileName = pobjFile.getFileName();
		lobjProcesser.midFormat = pidFormat;
		lobjProcesser.mobjData = lobjData;

		Engine.getThread(lobjProcesser).start();
	}
}
