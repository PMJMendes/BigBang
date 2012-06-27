package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionDetailBase;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;

public class InsurerAccountingDetail
	extends TransactionDetailBase
{
    public static InsurerAccountingDetail GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (InsurerAccountingDetail)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace,
					Constants.ObjID_InsurerAccountingDetail), pidKey);
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
