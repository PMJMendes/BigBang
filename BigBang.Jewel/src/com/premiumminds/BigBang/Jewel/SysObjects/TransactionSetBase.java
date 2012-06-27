package com.premiumminds.BigBang.Jewel.SysObjects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public abstract class TransactionSetBase
	extends ObjectBase
{
	public static class I
	{
		public static int DATE = 0;
		public static int USER = 1;
	}

	public abstract UUID getSubObjectType();
	public abstract String getOwnerHeader();
	public abstract UUID getOwnerObjectType();

	public TransactionMapBase[] getCurrentMaps()
		throws BigBangJewelException
	{
		ArrayList<TransactionMapBase> larrAux;
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;

		larrAux = new ArrayList<TransactionMapBase>();

		try
		{
			lrefObjects = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), getSubObjectType())); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {TransactionMapBase.I.SET}, new java.lang.Object[] {getKey()},
					new int[] {TransactionMapBase.I.SETTLEDON});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				larrAux.add((TransactionMapBase)Engine.GetWorkInstance(lrefObjects.getKey(), lrsObjects));
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

		return larrAux.toArray(new TransactionMapBase[larrAux.size()]);
	}

	public int getTotalCount()
		throws BigBangJewelException
	{
		TransactionMapBase[] larrAux;
        int llngTotal;
		int i;

		larrAux = getCurrentMaps();

		llngTotal = 0;
		for ( i = 0; i < larrAux.length; i++ )
			llngTotal += larrAux[i].getCurrentDetails().length;

		return llngTotal;
	}

	public boolean isComplete()
		throws BigBangJewelException
	{
		TransactionMapBase[] larrAux;
        boolean b;
		int i;

		larrAux = getCurrentMaps();

		b = true;
		for ( i = 0; i < larrAux.length; i++ )
		{
			if ( !larrAux[i].isSettled() )
			{
				b = false;
				break;
			}
		}

		return b;
	}

    public User getUser()
    {
    	try
    	{
			return User.GetInstance(getNameSpace(), (UUID)getAt(TransactionSetBase.I.USER));
		}
    	catch (JewelEngineException e)
    	{
    		return null;
		}
    }
}
