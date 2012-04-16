package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class MediatorAccountingMap
	extends ObjectBase
{
	public static class I
	{
		public static int SET       = 0;
		public static int INSURER   = 1;
		public static int SETTLEDON = 2;
	}

    public static MediatorAccountingMap GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (MediatorAccountingMap)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MediatorAccountingMap), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static MediatorAccountingMap GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (MediatorAccountingMap)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MediatorAccountingMap),
					prsObject);
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

	boolean isSettled()
	{
		return (getAt(I.SETTLEDON) != null);
	}
}
