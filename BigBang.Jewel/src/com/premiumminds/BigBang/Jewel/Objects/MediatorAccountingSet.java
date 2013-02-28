package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.TransactionSetBase;

public class MediatorAccountingSet
	extends TransactionSetBase
{
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

	public String getTitle()
	{
		return "Retrocessões";
	}

	public UUID getSubObjectType()
	{
		return Constants.ObjID_MediatorAccountingMap;
	}

	public UUID getTemplate()
	{
		return Constants.TID_MediatorAccounting;
	}

	public int GetNewSetNumber(SQLServer pdb)
		throws BigBangJewelException
	{
		IEntity lrefSets;
        ResultSet lrsSets;
        int llngResult;

		try
		{
			lrefSets = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_MediatorAccountingSet)); 

			lrsSets = lrefSets.SelectAllSort(pdb, new int[] {-I.NUMBER});
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		llngResult = 1;
		try
		{
			if ( lrsSets.next() )
			{
				if( lrsSets.getObject(2 + I.NUMBER) != null )
					llngResult = lrsSets.getInt(2 + I.NUMBER) + 1;
			}
		}
		catch (Throwable e)
		{
			try { lrsSets.close(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSets.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return llngResult;
	}
}
