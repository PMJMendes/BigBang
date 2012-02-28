package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

public class QuoteRequest
	extends ProcessData
{
    public static QuoteRequest GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (QuoteRequest)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_QuoteRequest), pidKey);
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

	public UUID GetProcessID()
	{
		return (UUID)getAt(1);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(1, pidProcess);
	}
}
