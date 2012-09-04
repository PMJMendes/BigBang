package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class FileImportDetail
	extends ObjectBase
{
	public static class I
	{
		public static int LINE       = 0;
		public static int SESSION    = 1;
		public static int NUMBER     = 2;
		public static int STATUS     = 3;
		public static int CREATEDOBJ = 4;
	}

    public static FileImportDetail GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (FileImportDetail)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_FileImportDetail), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static FileImportDetail GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (FileImportDetail)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_FileImportDetail), prsObject);
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
