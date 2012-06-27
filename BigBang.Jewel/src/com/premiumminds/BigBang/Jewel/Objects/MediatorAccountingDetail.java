package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionDetailBase;

public class MediatorAccountingDetail
	extends TransactionDetailBase
{
    public static MediatorAccountingDetail GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (MediatorAccountingDetail)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace,
					Constants.ObjID_MediatorAccountingDetail), pidKey);
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
