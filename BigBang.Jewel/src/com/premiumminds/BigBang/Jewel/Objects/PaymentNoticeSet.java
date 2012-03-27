package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class PaymentNoticeSet
	extends ObjectBase
{
    public static PaymentNoticeSet GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (PaymentNoticeSet)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_PaymentNoticeSet), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private User mrefUser;

	public void Initialize()
		throws JewelEngineException
	{
		mrefUser = User.GetInstance(getNameSpace(), (UUID)getAt(1));
	}

    public String AfterSave() 
    	throws JewelEngineException
    {
    	if ( mrefUser == null )
    		Initialize();

        return "";
    }

    public String getLabel()
    {
    	return mrefUser.getDisplayName() + " @ " + ((Timestamp)getAt(0)).toString().substring(0, 17);
    }
}
