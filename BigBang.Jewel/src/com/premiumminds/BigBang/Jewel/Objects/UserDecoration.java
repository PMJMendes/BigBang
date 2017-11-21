package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IUser;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class UserDecoration
	extends ObjectBase
{
	public static class I
	{
		public static int OWNER       		= 0;
		public static int EMAIL       		= 1;
		public static int COSTCENTER  		= 2;
		public static int MIGRATIONID 		= 3;
		public static int PRINTERNAME 		= 4;
		public static int SURROGATE   		= 5;
		public static int TITLE       		= 6;
		public static int PHONE       		= 7;
		public static int BCHANGEINSURER	= 8;
	}

    public static UserDecoration GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
    	try
    	{
    		return (UserDecoration)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Decorations), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static UserDecoration GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
		try
		{
			return (UserDecoration)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Decorations), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static UserDecoration GetByUserID(UUID pidNameSpace, UUID pidUser)
    	throws BigBangJewelException
    {
        MasterDB ldb;
        ResultSet lrs;
        UserDecoration lobjDeco;

    	ldb = null;
    	lrs = null;
    	lobjDeco = null;
		try
		{
			ldb = new MasterDB();

			lrs = Entity.GetInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Decorations))
					.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {pidUser}, null);
		    if (lrs.next())
		    	lobjDeco = UserDecoration.GetInstance(pidNameSpace, lrs);
		    lrs.close();
	    	lrs = null;

	        ldb.Disconnect();
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			if ( ldb != null ) try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjDeco;
    }

    public static UserDecoration GetByFullName(UUID pidNameSpace, String pstrName)
    	throws BigBangJewelException
    {
        MasterDB ldb;
        ResultSet lrs;
        User lobjUser;

    	ldb = null;
    	lrs = null;
    	lobjUser = null;
		try
		{
			ldb = new MasterDB();

			lrs = Entity.GetInstance(Engine.FindEntity(pidNameSpace, Jewel.Engine.Constants.ObjectGUIDs.O_User))
					.SelectByMembers(ldb, new int[] {Jewel.Engine.Constants.Miscellaneous.FullName_In_User},
							new java.lang.Object[] {pstrName}, null);
		    if (lrs.next())
		    	lobjUser = User.GetInstance(pidNameSpace, lrs);
		    lrs.close();
	    	lrs = null;

	        ldb.Disconnect();
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			if ( ldb != null ) try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( lobjUser == null )
			return null;

		return GetByUserID(pidNameSpace, lobjUser.getKey());
    }

	private IUser mrefUser;

	public void Initialize()
		throws JewelEngineException
	{
	}

    public String getLabel()
    {
    	try
    	{
			return getBaseUser().getLabel() + " *";
		}
    	catch (Throwable e)
    	{
    		return null;
		}
    }

    public IUser getBaseUser()
    	throws BigBangJewelException
    {
    	if ( mrefUser == null )
    	{
    		try
			{
				mrefUser = (IUser)User.GetInstance(getNameSpace(), (UUID)getAt(I.OWNER));
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
    	}

    	return mrefUser;
    }
}
