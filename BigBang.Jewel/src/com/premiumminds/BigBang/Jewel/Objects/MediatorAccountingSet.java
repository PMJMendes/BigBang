package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class MediatorAccountingSet
	extends ObjectBase
{
	public static class I
	{
		public static int DATE = 0;
		public static int USER = 1;
	}

    public static MediatorAccountingSet GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (MediatorAccountingSet)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MediatorAccountingSet), pidKey);
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

	public boolean isComplete()
		throws BigBangJewelException
	{
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;
        boolean b;

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_MediatorAccountingMap)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {MediatorAccountingMap.I.SET}, new java.lang.Object[] {getKey()},
					new int[] {MediatorAccountingMap.I.SETTLEDON});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		b = true;
		try
		{
			while ( lrsObjects.next() )
			{
				if ( !MediatorAccountingMap.GetInstance(getNameSpace(), lrsObjects).isSettled() )
				{
					b = false;
					break;
				}
			}
		}
		catch (BigBangJewelException e)
		{
			try { lrsObjects.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw e;
		}
		catch (Throwable e)
		{
			try { lrsObjects.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return b;
	}
}
