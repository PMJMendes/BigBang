package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class DebitNote
	extends ObjectBase
{
    public static DebitNote GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (DebitNote)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_DebitNote), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static DebitNote GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
		try
		{
			return (DebitNote)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_DebitNote), prsObject);
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
