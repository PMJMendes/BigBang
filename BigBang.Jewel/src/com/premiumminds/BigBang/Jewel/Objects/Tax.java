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
	private Coverage lobjCoverage;

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
		try
		{
			lobjCoverage = Coverage.GetInstance(getNameSpace(), (UUID)getAt(1));
		}
		catch (Throwable e)
		{
			throw new JewelEngineException(e.getMessage(), e);
		}
	}

	public Coverage GetCoverage()
	{
		return lobjCoverage;
	}

	public UUID GetFieldType()
	{
		return (UUID)getAt(2);
	}

	public String GetUnitsLabel()
	{
		return (String)getAt(3);
	}

	public boolean GetVariesByObject()
	{
		return (Boolean)getAt(5);
	}

	public boolean GetVariesByExercise()
	{
		return (Boolean)getAt(6);
	}

	public UUID GetRefersToID()
	{
		return (UUID)getAt(7);
	}

	public int GetColumnOrder()
	{
		return (Integer)getAt(8);
	}

	public boolean IsMandatory()
	{
		return (Boolean)getAt(9);
	}

	public String GetTag()
	{
		return (String)getAt(10);
	}

	public boolean IsVisible()
	{
		return (Boolean)getAt(11);
	}
}
