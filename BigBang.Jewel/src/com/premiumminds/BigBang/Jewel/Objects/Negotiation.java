package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

public class Negotiation
	extends ProcessData
{
    public static Negotiation GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (Negotiation)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Negotiation), pidKey);
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
		return null;
	}

	public void SetProcessID(UUID pidProcess)
	{
	}
}
