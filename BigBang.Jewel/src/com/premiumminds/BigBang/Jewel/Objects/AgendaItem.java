package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
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
				larrAux.add(UUID.fromString(lrs.getString(2)));
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
				larrAux.add(UUID.fromString(lrs.getString(2)));
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

	public void InitNew(UUID[] parrProcIDs, UUID[] parrOpIDs, SQLServer pdb)
		throws BigBangJewelException
	{
		UUID lidAux;
		int i;
		ObjectBase lobjAux;

		if ( ((marrProcesses != null) && (marrProcesses.length > 0)) || 
				((marrOperations != null) && (marrOperations.length > 0)) )
			throw new BigBangJewelException("Erro: Não pode redefinir os processos de items de agenda pre-existentes.");
		if ( getKey() == null )
			throw new BigBangJewelException("Erro: Não pode definir o conjunto de processos para um item de agenda antes de gravar.");
		if ( parrProcIDs.length == 0 )
			throw new BigBangJewelException("Erro: Não pode definir um conjunto vazio de processos para um novo item de agenda.");
		if ( parrOpIDs.length == 0 )
			throw new BigBangJewelException("Erro: Não pode definir um conjunto vazio de operações para um novo item de agenda.");

		try
		{
			lidAux = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess);

			for ( i = 0; i < parrProcIDs.length; i++ )
			{
				lobjAux = Engine.GetWorkInstance(lidAux, (UUID)null);
				lobjAux.setAt(0, getKey());
				lobjAux.setAt(1, parrProcIDs[i]);
				lobjAux.SaveToDb(pdb);
			}

			lidAux = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaOp);

			for ( i = 0; i < parrOpIDs.length; i++ )
			{
				lobjAux = Engine.GetWorkInstance(lidAux, (UUID)null);
				lobjAux.setAt(0, getKey());
				lobjAux.setAt(1, parrOpIDs[i]);
				lobjAux.SaveToDb(pdb);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		marrProcesses = parrProcIDs;
		marrOperations = parrOpIDs;
	}

	public void ClearData(SQLServer pdb)
		throws BigBangJewelException
	{
		IEntity lrefAux;
		int i;

		if ( getKey() == null )
			throw new BigBangJewelException("Erro: Não pode limpar os dados de um item de agenda novo antes de gravar.");

		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
			for ( i = 0; i < marrProcesses.length; i++ )
				lrefAux.Delete(pdb, marrProcesses[i]);

			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaOp));
			for ( i = 0; i < marrOperations.length; i++ )
				lrefAux.Delete(pdb, marrOperations[i]);
		}
		catch (Throwable e)
		{
			marrProcesses = null;
			marrOperations = null;
			throw new BigBangJewelException(e.getMessage(), e);
		}

		marrProcesses = new UUID[0];
		marrOperations = new UUID[0];
	}
}
