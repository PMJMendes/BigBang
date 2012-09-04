package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class DocInfo
	extends ObjectBase
{
    public static DocInfo GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (DocInfo)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_DocInfo), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static DocInfo GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (DocInfo)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_DocInfo), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private Document mrefOwner;

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

	public Document getOwner()
		throws BigBangJewelException
	{
		if ( mrefOwner == null )
		{
			try
			{
				mrefOwner = Document.GetInstance(getNameSpace(), (UUID)getAt(0));
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

		return mrefOwner;
	}
}
