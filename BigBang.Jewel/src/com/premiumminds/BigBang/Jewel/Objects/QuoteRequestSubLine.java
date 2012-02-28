package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class QuoteRequestSubLine
	extends ObjectBase
{
    public static QuoteRequestSubLine GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (QuoteRequestSubLine)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_QuoteRequestSubLine), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static QuoteRequestSubLine GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (QuoteRequestSubLine)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_QuoteRequestSubLine), prsObject);
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
