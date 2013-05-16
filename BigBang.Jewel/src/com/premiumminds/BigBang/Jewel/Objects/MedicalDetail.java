package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class MedicalDetail
	extends ObjectBase
{
	public static class I
	{
		public static int FILE           = 0;
		public static int DISABILITYTYPE = 1;
		public static int STARTDATE      = 2;
		public static int PLACE          = 3;
		public static int PERCENT        = 4;
		public static int ENDDATE        = 5;
		public static int BENEFITS       = 6;
	}

    public static MedicalDetail GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (MedicalDetail)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MedicalDetail), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static MedicalDetail GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (MedicalDetail)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MedicalDetail), prsObject);
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
