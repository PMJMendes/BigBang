package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class Tax
	extends ObjectBase
{
    public static Tax GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (Tax)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Tax), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static Tax GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
		try
		{
			return (Tax)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Tax), prsObject);
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

	public boolean GetVariesByObject()
	{
		return (Boolean)getAt(5);
	}

	public boolean GetVariesByExercise()
	{
		return (Boolean)getAt(6);
	}

	public int GetColumnOrder()
	{
		return (Integer)getAt(8);
	}
}
