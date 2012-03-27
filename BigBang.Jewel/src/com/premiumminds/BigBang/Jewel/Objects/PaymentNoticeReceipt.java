package com.premiumminds.BigBang.Jewel.Objects;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class PaymentNoticeReceipt
	extends ObjectBase
{
    public static PaymentNoticeReceipt GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (PaymentNoticeReceipt)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_PaymentNoticeReceipt), pidKey);
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
