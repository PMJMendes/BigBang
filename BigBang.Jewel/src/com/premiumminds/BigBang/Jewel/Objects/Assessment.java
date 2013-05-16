package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

public class Assessment
	extends ProcessData
{
	public static class I
	{
		public static int REFERENCE     = 0;
		public static int SUBCASUALTY   = 1;
		public static int PROCESS       = 2;
		public static int SCHEDULEDDATE = 3;
		public static int EFFECTIVEDATE = 4;
		public static int ISCONDITIONAL = 5;
		public static int ISTOTALLOSS   = 6;
		public static int NOTES         = 7;
		public static int LIMITDATE     = 8;
	}

    public static Assessment GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (Assessment)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Assessment), pidKey);
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
