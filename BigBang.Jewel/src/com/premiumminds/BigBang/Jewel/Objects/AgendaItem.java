package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Engine.SysObjects.ObjectBase;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class AgendaItem
	extends ObjectBase
{
    public static AgendaItem GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (AgendaItem)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_AgendaItem), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static AgendaItem GetInstance(UUID pidNameSpace, ResultSet prsObject)
		throws BigBangJewelException
	{
	    try
	    {
			return (AgendaItem)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_AgendaItem), prsObject);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private UUID[] marrOperations;
	private UUID[] marrProcesses;

	public void Initialize()
		throws JewelEngineException
	{
			int[] larrMembers;
			java.lang.Object[] larrParams;
			IEntity lrefAux;
			MasterDB ldb;
			ArrayList<UUID> larrAux;
			ResultSet lrs;

			larrMembers = new int[1];
			larrMembers[0] = 0;
			larrParams = new java.lang.Object[1];
			larrParams[0] = getKey();

			larrAux = new ArrayList<UUID>();
			try
			{
				lrefAux = Entity.GetInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_AgendaOp));
				ldb = new MasterDB();
				lrs = lrefAux.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
				while ( lrs.next() )
				{
					larrAux.add(UUID.fromString(lrs.getString(1)));
				}
				lrs.close();
				ldb.Disconnect();
			}
			catch (Throwable e)
			{
				throw new JewelEngineException(e.getMessage(), e);
			}

			marrOperations = larrAux.toArray(new UUID[larrAux.size()]);

			larrAux = new ArrayList<UUID>();
			try
			{
				lrefAux = Entity.GetInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_AgendaProcess));
				ldb = new MasterDB();
				lrs = lrefAux.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
				while ( lrs.next() )
				{
					larrAux.add(UUID.fromString(lrs.getString(1)));
				}
				lrs.close();
				ldb.Disconnect();
			}
			catch (Throwable e)
			{
				throw new JewelEngineException(e.getMessage(), e);
			}

			marrProcesses = larrAux.toArray(new UUID[larrAux.size()]);
	}

	public UUID[] GetProcessIDs()
	{
		return marrProcesses;
	}

	public UUID[] GetOperationIDs()
	{
		return marrOperations;
	}
}
