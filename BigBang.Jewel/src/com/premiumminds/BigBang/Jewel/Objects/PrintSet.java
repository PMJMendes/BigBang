package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
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
	public static class I
	{
		public static int TEMPLATE  = 0;
		public static int DATE      = 1;
		public static int USER      = 2;
		public static int PRINTEDON = 3;
	}

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

    public static PrintSet GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (PrintSet)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_PrintSet), prsObject);
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
