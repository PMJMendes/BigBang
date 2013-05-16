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

public class InsurerAccountingSet
	extends TransactionSetBase
{
    public static InsurerAccountingSet GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (InsurerAccountingSet)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_InsurerAccountingSet), pidKey);
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
		return "Prestações de Contas";
	}

	public UUID getSubObjectType()
	{
		return Constants.ObjID_InsurerAccountingMap;
	}

	public UUID getTemplate()
	{
		return Constants.TID_InsurerAccounting;
	}

	public void SettleAll(SQLServer pdb)
		throws BigBangJewelException
	{
		int i;

		getMaps();

		for ( i = 0; i < marrMaps.length; i++ )
			((InsurerAccountingMap)marrMaps[i]).prep();

		super.SettleAll(pdb);
	}

	public int GetNewSetNumber(SQLServer pdb)
		throws BigBangJewelException
	{
		IEntity lrefSets;
        ResultSet lrsSets;
        int llngResult;

		try
		{
			lrefSets = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_InsurerAccountingSet)); 

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
