package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class MessageAddress
	extends ObjectBase
{
	public static class I
	{
		public static int ADDRESS     = 0;
		public static int OWNER       = 1;
		public static int USAGE       = 2;
		public static int USER        = 3;
		public static int CONTACTINFO = 4;
		public static int DISPLAYNAME = 5;
	}

    public static MessageAddress GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (MessageAddress)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MessageAddress), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static MessageAddress GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (MessageAddress)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MessageAddress), prsObject);
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
