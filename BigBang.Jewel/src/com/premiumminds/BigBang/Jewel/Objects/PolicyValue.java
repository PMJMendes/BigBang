package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class PolicyValue
	extends ObjectBase
{
    public static PolicyValue GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (PolicyValue)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_PolicyValue), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static PolicyValue GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (PolicyValue)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_PolicyValue), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private Tax mrefTax;

	public void Initialize()
		throws JewelEngineException
	{
		try
		{
			mrefTax = Tax.GetInstance(getNameSpace(), (UUID)getAt(2));
		}
		catch (Throwable e)
		{
			throw new JewelEngineException(e.getMessage(), e);
		}
	}

	public Tax GetTax()
	{
		return mrefTax;
	}

    public String AfterSave() 
    	throws JewelEngineException
    {
    	if ( mrefTax == null )
    		Initialize();

        return "";
    }
}
