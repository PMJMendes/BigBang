package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class CostCenter
	extends ObjectBase
{
	public static class I
	{
		public static int CODE          =  0;
		public static int DISPLAYNAME   =  1;
		public static int MIGRATIONID   =  2;
	}

    public static CostCenter GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (CostCenter)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_CostCenter), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static CostCenter GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (CostCenter)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_CostCenter), prsObject);
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
