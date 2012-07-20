package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class InsurerReceipt
	extends ObjectBase
{
	public static class I
	{
		public static int NUMBER  = 0;
		public static int INSURER = 1;
		public static int VALUE   = 2;
		public static int TAX     = 3;
		public static int DATE    = 4;
	}

    public static InsurerReceipt GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (InsurerReceipt)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_InsurerReceipt), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static InsurerReceipt GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (InsurerReceipt)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_InsurerReceipt), prsObject);
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
