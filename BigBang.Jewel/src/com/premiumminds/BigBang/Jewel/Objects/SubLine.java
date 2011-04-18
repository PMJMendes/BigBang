package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class SubLine
	extends ObjectBase
{
    public static SubLine GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (SubLine)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubLine), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

    public static SubLine GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
		try
		{
			return (SubLine)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_SubLine), prsObject);
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

    public Coverage[] GetCurrentCoverages()
    	throws BigBangJewelException
    {
		int[] larrMembers;
		java.lang.Object[] larrParams;
		Entity lrefCoverages;
		MasterDB ldb;
		ArrayList<Coverage> larrAux;
		ResultSet lrsCoverages;

		larrMembers = new int[1];
		larrMembers[0] = Constants.FKSubLine_In_Coverage;
		larrParams = new java.lang.Object[1];
		larrParams[0] = getKey();

		larrAux = new ArrayList<Coverage>();

		try
		{
			lrefCoverages = Entity.GetInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_Coverage)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}


		try
		{
			lrsCoverages = lrefCoverages.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsCoverages.next() )
				larrAux.add(Coverage.GetInstance(getNameSpace(), lrsCoverages));
		}
		catch (Throwable e)
		{
			try { lrsCoverages.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsCoverages.close();
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

		return larrAux.toArray(new Coverage[larrAux.size()]);
    }
}
