package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class TotalLoss
	extends ProcessData
{
	public static class I
	{
		public static int REFERENCE    = 0;
		public static int SUBCASUALTY  = 1;
		public static int PROCESS      = 2;
		public static int CAPITAL      = 3;
		public static int DEDUCTIBLE   = 4;
		public static int SETTLEMENT   = 5;
		public static int SALVAGEVALUE = 6;
		public static int SALVAGETYPE  = 7;
		public static int SALVAGEBUYER = 8;
	}

    public static TotalLoss GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (TotalLoss)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_TotalLossFile), pidKey);
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
