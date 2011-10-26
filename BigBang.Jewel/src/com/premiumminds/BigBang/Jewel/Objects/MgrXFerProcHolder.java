package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class MgrXFerProcHolder
	extends ObjectBase
{
    public static MgrXFerProcHolder GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (MgrXFerProcHolder)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MgrXFerProc), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static MgrXFerProcHolder GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (MgrXFerProcHolder)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MgrXFerProc), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private MgrXFer mrefOwner;

	public void Initialize()
		throws JewelEngineException
	{
	}

	public MgrXFer getOwner()
		throws BigBangJewelException
	{
		if ( mrefOwner == null )
		{
			try
			{
				mrefOwner = MgrXFer.GetInstance(getNameSpace(), (UUID)getAt(0));
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

		return mrefOwner;
	}
}
