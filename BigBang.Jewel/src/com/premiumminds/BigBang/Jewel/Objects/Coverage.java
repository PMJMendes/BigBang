package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

public class Coverage
	extends ObjectBase
{
    public static Coverage GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (Coverage)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Coverage), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static Coverage GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
		try
		{
			return (Coverage)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_Coverage), prsObject);
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

	public boolean IsHeader()
	{
		return (Boolean)getAt(3);
	}

	public boolean IsMandatory()
	{
		return (Boolean)getAt(2);
	}

    public Tax[] GetCurrentTaxes()
    	throws BigBangJewelException
    {
		int[] larrMembers;
		java.lang.Object[] larrParams;
		Entity lrefCoverages;
		MasterDB ldb;
		ArrayList<Tax> larrAux;
		ResultSet lrsTaxes;

		larrMembers = new int[1];
		larrMembers[0] = Constants.FKCoverage_In_Tax;
		larrParams = new java.lang.Object[1];
		larrParams[0] = getKey();

		larrAux = new ArrayList<Tax>();

		try
		{
			lrefCoverages = Entity.GetInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_Tax)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}


		try
		{
			lrsTaxes = lrefCoverages.SelectByMembers(ldb, new int[] {Constants.FKCoverage_In_Tax},
					new java.lang.Object[] {getKey()}, new int[] {Constants.Order_In_Tax});
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsTaxes.next() )
				larrAux.add(Tax.GetInstance(getNameSpace(), lrsTaxes));
		}
		catch (Throwable e)
		{
			try { lrsTaxes.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsTaxes.close();
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

		return larrAux.toArray(new Tax[larrAux.size()]);
    }
}
