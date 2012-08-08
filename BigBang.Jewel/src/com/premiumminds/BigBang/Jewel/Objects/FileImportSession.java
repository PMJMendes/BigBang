package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

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
}
