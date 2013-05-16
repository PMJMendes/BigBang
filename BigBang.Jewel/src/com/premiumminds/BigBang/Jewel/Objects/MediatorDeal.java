package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class MediatorDeal
	extends ObjectBase
{
	public static class I
	{
		public static int MEDIATOR = 0;
		public static int SUBLINE  = 1;
		public static int PERCENT  = 2;
	}

    public static MediatorDeal GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (MediatorDeal)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MediatorDeal), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static MediatorDeal GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
		try
		{
			return (MediatorDeal)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MediatorDeal), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize() throws JewelEngineException
	{
	}
}
