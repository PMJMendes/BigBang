package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class ReportParam
	extends ObjectBase
{
	public static class I
	{
		public static int LABEL       = 0;
		public static int ORDER       = 1;
		public static int OWNER       = 2;
		public static int TYPE        = 3;
		public static int UNITS       = 4;
		public static int REFERENCETO = 5;
	}

    public static ReportParam GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (ReportParam)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_ReportParam), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static ReportParam GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (ReportParam)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_ReportParam), prsObject);
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
