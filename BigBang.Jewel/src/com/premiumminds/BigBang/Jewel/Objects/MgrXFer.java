package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

public class MgrXFer
	extends ProcessData
{
    public static MgrXFer GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (MgrXFer)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MgrXFer), pidKey);
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
		return (UUID)getAt(0);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(0, pidProcess);
	}

	public UUID GetOldManagerID()
	{
		return (UUID)getAt(1);
	}

	public UUID GetNewManagerID()
	{
		return (UUID)getAt(2);
	}

	public String GetTag()
	{
		return (String)getAt(3);
	}

	public UUID GetRequestingUser()
	{
		return (UUID)getAt(4);
	}
}
