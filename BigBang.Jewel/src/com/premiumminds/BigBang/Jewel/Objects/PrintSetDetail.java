package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class PrintSetDetail
	extends ObjectBase
{
	public static class I
	{
		public static int DOCUMENT =  0;
		public static int FILE     =  1;
	}

    public static PrintSetDetail GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (PrintSetDetail)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_PrintSetDetail), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static PrintSetDetail GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (PrintSetDetail)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_PrintSetDetail), prsObject);
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

	public FileXfer getFile()
	{
		java.lang.Object lobjFile;

		lobjFile = getAt(I.FILE);

		if ( lobjFile == null )
			return null;

		if ( lobjFile instanceof FileXfer )
			return (FileXfer)lobjFile;

		return new FileXfer((byte[])lobjFile);
	}
}
