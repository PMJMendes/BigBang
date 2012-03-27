package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class PaymentNoticeClient
	extends ObjectBase
{
    public static PaymentNoticeClient GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (PaymentNoticeClient)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_PaymentNoticeClient), pidKey);
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
