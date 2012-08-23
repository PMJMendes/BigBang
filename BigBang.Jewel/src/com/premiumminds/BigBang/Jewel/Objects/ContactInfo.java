package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class ContactInfo
	extends ObjectBase
{
	public static class I
	{
		public static int OWNER = 0;
		public static int TYPE  = 1;
		public static int VALUE = 2;
	}

    public static ContactInfo GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (ContactInfo)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_ContactInfo), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static ContactInfo GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (ContactInfo)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_ContactInfo), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private Contact mrefOwner;

	public void Initialize()
		throws JewelEngineException
	{
	}

    public String getLabel()
    {
    	try
    	{
			return (String)getAt(1) + " for " + getOwner().getLabel();
		}
    	catch (Throwable e)
    	{
    		return null;
		}
    }

    public Contact getOwner()
    	throws BigBangJewelException
    {
    	if ( mrefOwner == null )
    	{
    		try
			{
    			mrefOwner = Contact.GetInstance(getNameSpace(), (UUID)getAt(0));
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
    	}

    	return mrefOwner;
    }
}
