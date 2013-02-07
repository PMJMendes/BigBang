package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class QuoteRequestObject
	extends ObjectBase
{
    public static QuoteRequestObject GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (QuoteRequestObject)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_QuoteRequestObject), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static QuoteRequestObject GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (QuoteRequestObject)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_QuoteRequestObject), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    private QuoteRequest mrefOwner;

	public void Initialize()
		throws JewelEngineException
	{
		if ( getAt(1) != null )
		{
			try
			{
				mrefOwner = QuoteRequest.GetInstance(getNameSpace(), (UUID)getAt(1));
			}
			catch (Throwable e)
			{
				throw new JewelEngineException(e.getMessage(), e);
			}
		}
	}

    public QuoteRequest GetOwner()
    {
    	if ( mrefOwner == null )
    	{
			try
			{
				Initialize();
			}
			catch (Throwable e)
			{
			}
    	}

    	return mrefOwner;
    }
}
