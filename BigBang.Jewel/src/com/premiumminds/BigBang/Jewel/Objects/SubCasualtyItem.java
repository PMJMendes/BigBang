package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class SubCasualtyItem
	extends ObjectBase
{
	public static class I
	{
		public static int SUBCASUALTY        = 0;
		public static int POLICYOBJECT       = 1;
		public static int SUBPOLICYOBJECT    = 2;
		public static int POLICYCOVERAGE     = 3;
		public static int SUBOPOLICYCOVERAGE = 4;
		public static int TYPE               = 5;
		public static int DAMAGES            = 6;
		public static int SETTLEMENT         = 7;
		public static int MANUAL             = 8;
	}

    public static SubCasualtyItem GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (SubCasualtyItem)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubCasualtyItem), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static SubCasualtyItem GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (SubCasualtyItem)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubCasualtyItem), prsObject);
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
