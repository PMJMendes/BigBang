package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class MessageAttachment
	extends ObjectBase
{
	public static class I
	{
		public static int OWNER        = 0;
		public static int DOCUMENT     = 1;
		public static int ATTACHMENTID = 2;
	}

    public static MessageAttachment GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
		try
		{
			return (MessageAttachment)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MessageAttachment), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}
    
   /* public static MessageAttachment GetInstance2(UUID pidNameSpace, UUID pidKey)
    		throws BigBangJewelException
    	{
    		try
    		{
    			UUID entity = Engine.FindEntity(pidNameSpace, Constants.ObjID_MessageAttachment);
    			Cache cache = Engine.GetCache(true);
				MessageAttachment result = (MessageAttachment)cache.getAt(entity, pidKey);
				return result;
    		}
    	    catch (Throwable e)
    	    {
    	    	throw new BigBangJewelException(e.getMessage(), e);
    		}
    	} */

	public static MessageAttachment GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (MessageAttachment)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MessageAttachment), prsObject);
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
