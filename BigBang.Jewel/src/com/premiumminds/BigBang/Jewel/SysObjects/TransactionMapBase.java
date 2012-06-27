package com.premiumminds.BigBang.Jewel.SysObjects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;

public abstract class TransactionMapBase
	extends ObjectBase
{
	public static class I
	{
		public static int SET       = 0;
		public static int OWNER     = 1;
		public static int SETTLEDON = 2;
	}

	public abstract UUID getSubObjectType();

	public TransactionDetailBase[] getCurrentDetails()
		throws BigBangJewelException
	{
		ArrayList<TransactionDetailBase> larrAux;
		IEntity lrefObjects;
        MasterDB ldb;
        ResultSet lrsObjects;

		larrAux = new ArrayList<TransactionDetailBase>();

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
			lrsObjects = lrefObjects.SelectByMembers(ldb, new int[] {TransactionDetailBase.I.OWNER}, new java.lang.Object[] {getKey()},
					new int[] {TransactionDetailBase.I.VOIDED});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsObjects.next() )
				larrAux.add((TransactionDetailBase)Engine.GetWorkInstance(lrefObjects.getKey(), lrsObjects));
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

		return larrAux.toArray(new TransactionDetailBase[larrAux.size()]);
	}

	public boolean isSettled()
	{
		return (getAt(I.SETTLEDON) != null);
	}
}
