package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IUser;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class UserDecoration
	extends ObjectBase
{
	private IUser mrefUser;

    public static UserDecoration GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
    	try
    	{
    		return (UserDecoration)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Decorations), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static UserDecoration GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
		try
		{
			return (UserDecoration)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Decorations), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public void Initialize()
		throws JewelEngineException
	{
		try
		{
			mrefUser = (IUser)User.GetInstance(Engine.getCurrentNameSpace(), (UUID)getAt(0));
		}
		catch (Throwable e)
		{
			throw new JewelEngineException(e.getMessage(), e);
		}
	}

    public String getLabel()
    {
    	return mrefUser.getLabel() + " *";
    }

    public IUser getBaseUser()
    {
    	return mrefUser;
    }
}
