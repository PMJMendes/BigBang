package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class QuoteRequestCoverage
	extends ObjectBase
{
    public static QuoteRequestCoverage GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (QuoteRequestCoverage)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_QuoteRequestCoverage), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static QuoteRequestCoverage GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (QuoteRequestCoverage)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_QuoteRequestCoverage), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	Coverage mrefCoverage;

	public void Initialize()
		throws JewelEngineException
	{
		try
		{
			mrefCoverage = Coverage.GetInstance(getNameSpace(), (UUID)getAt(1));
		}
		catch (Throwable e)
		{
			throw new JewelEngineException(e.getMessage(), e);
		}
	}

    public String AfterSave() 
    	throws JewelEngineException
    {
    	if ( mrefCoverage == null )
    		Initialize();

        return "";
    }

	public Coverage GetCoverage()
	{
		return mrefCoverage;
	}

	public Boolean IsPresent()
	{
		return (Boolean)getAt(2);
	}
}
