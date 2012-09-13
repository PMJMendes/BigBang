package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class SubPolicyObject
	extends ObjectBase
{
    public static SubPolicyObject GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (SubPolicyObject)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubPolicyObject), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    private SubPolicy mrefOwner;

	public static SubPolicyObject GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (SubPolicyObject)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubPolicyObject), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize()
		throws JewelEngineException
	{
		if ( getAt(1) != null )
		{
			try
			{
				mrefOwner = SubPolicy.GetInstance(getNameSpace(), (UUID)getAt(1));
			}
			catch (Throwable e)
			{
				throw new JewelEngineException(e.getMessage(), e);
			}
		}
	}

    public SubPolicy GetOwner()
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
