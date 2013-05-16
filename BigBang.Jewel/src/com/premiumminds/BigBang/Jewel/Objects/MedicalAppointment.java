package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class MedicalAppointment
	extends ObjectBase
{
	public static class I
	{
		public static int FILE  = 0;
		public static int LABEL = 1;
		public static int DATE  = 2;
	}

    public static MedicalAppointment GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (MedicalAppointment)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MedicalAppointment),
					pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static MedicalAppointment GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (MedicalAppointment)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MedicalAppointment),
					prsObject);
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
