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

public class ClientGroup
	extends ObjectBase
{
    public static ClientGroup GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (ClientGroup)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_ClientGroup), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static ClientGroup GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
		try
		{
			return (ClientGroup)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_ClientGroup), prsObject);
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

    public ClientGroup[] GetCurrentSubGroups()
    	throws BigBangJewelException
    {
		int[] larrMembers;
		java.lang.Object[] larrParams;
		Entity lrefGroups;
		MasterDB ldb;
		ArrayList<ClientGroup> larrAux;
		ResultSet lrsSubGroups;

		larrMembers = new int[1];
		larrMembers[0] = Constants.FKParent_In_Group;
		larrParams = new java.lang.Object[1];
		larrParams[0] = getKey();

		larrAux = new ArrayList<ClientGroup>();

		try
		{
			lrefGroups = Entity.GetInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_ClientGroup)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubGroups = lrefGroups.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			while ( lrsSubGroups.next() )
				larrAux.add(ClientGroup.GetInstance(getNameSpace(), lrsSubGroups));
		}
		catch (Throwable e)
		{
			try { lrsSubGroups.close(); } catch (Throwable e1) {}
			try { ldb.Disconnect(); } catch (Throwable e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsSubGroups.close();
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

		return larrAux.toArray(new ClientGroup[larrAux.size()]);
    }
}
