package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

public class SubCasualty
	extends ProcessData
{
	public static class I
	{
		public static int NUMBER         =  0;
		public static int PROCESS        =  1;
		public static int POLICY         =  2;
		public static int SUBPOLICY      =  3;
		public static int INSURERPROCESS =  4;
		public static int DESCRIPTION    =  5;
		public static int NOTES          =  6;
		public static int BODY           =  7;
		public static int FROM           =  8;
		public static int SUBJECT        =  9;
		public static int LIMITDATE      = 10;
	}

    public static SubCasualty GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (SubCasualty)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubCasualty), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static SubCasualty GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (SubCasualty)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubCasualty), prsObject);
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

	public UUID GetProcessID()
	{
		return (UUID)getAt(I.PROCESS);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(I.PROCESS, pidProcess);
	}
}
