package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class PrintSet
	extends ObjectBase
{
    public static PrintSet GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (PrintSet)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_PrintSet), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private User mrefUser;
	private Template mrefTemplate;

	public void Initialize()
		throws JewelEngineException
	{
		try
		{
			mrefTemplate = Template.GetInstance(getNameSpace(), (UUID)getAt(0));
		}
		catch (Throwable e)
		{
			throw new JewelEngineException(e.getMessage(), e);
		}
		mrefUser = User.GetInstance(getNameSpace(), (UUID)getAt(2));
	}

    public String AfterSave() 
    	throws JewelEngineException
    {
    	if ( (mrefUser == null) || (mrefTemplate == null) )
    		Initialize();

        return "";
    }

    public String getLabel()
    {
    	return mrefTemplate.getLabel() + " @ " + ((Timestamp)getAt(0)).toString().substring(0, 17) + " (" + mrefUser.getDisplayName() + ")";
    }
}
