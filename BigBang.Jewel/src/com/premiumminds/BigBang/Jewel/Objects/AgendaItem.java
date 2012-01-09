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
			MasterDB ldb;

			try
			{
				ldb = new MasterDB();
			}
			catch (Throwable e)
			{
				throw new JewelEngineException(e.getMessage(), e);
			}

			try
			{
				GetAgendaOpIDs(ldb);
				GetAgendaProcIDs(ldb);
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new JewelEngineException(e.getMessage(), e);
			}

			try
			{
				ldb.Disconnect();
			}
			catch (Throwable e)
			{
				throw new JewelEngineException(e.getMessage(), e);
			}
		}

	public UUID[] GetAgendaProcIDs()
		throws JewelEngineException
	{
		MasterDB ldb;

		if ( marrProcesses == null )
		{
			try
			{
				ldb = new MasterDB();
			}
			catch (Throwable e)
			{
				throw new JewelEngineException(e.getMessage(), e);
			}

			try
			{
				GetAgendaProcIDs(ldb);
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new JewelEngineException(e.getMessage(), e);
			}

			try
			{
				ldb.Disconnect();
			}
			catch (Throwable e)
			{
				throw new JewelEngineException(e.getMessage(), e);
			}
		}

		return marrProcesses;
	}

	public UUID[] GetAgendaOpIDs()
		throws JewelEngineException
	{
		MasterDB ldb;

		if ( marrOperations == null )
		{
			try
			{
				ldb = new MasterDB();
			}
			catch (Throwable e)
			{
				throw new JewelEngineException(e.getMessage(), e);
			}

			try
			{
				GetAgendaOpIDs(ldb);
			}
			catch (Throwable e)
			{
				try { ldb.Disconnect(); } catch (Throwable e1) {}
				throw new JewelEngineException(e.getMessage(), e);
			}

			try
			{
				ldb.Disconnect();
			}
			catch (Throwable e)
			{
				throw new JewelEngineException(e.getMessage(), e);
			}
		}

		return marrOperations;
	}

	public UUID[] GetAgendaProcIDs(SQLServer pdb)
		throws BigBangJewelException
	{
		IEntity lrefAux;
		ArrayList<UUID> larrAux;
		ResultSet lrs;
		ObjectBase lobjAux;

		if ( marrProcesses == null )
		{
			larrAux = new ArrayList<UUID>();
			try
			{
				lrefAux = Entity.GetInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_AgendaProcess));
				lrs = lrefAux.SelectByMembers(pdb, new int[] {0}, new java.lang.Object[] {getKey()}, new int[0]);
				while ( lrs.next() )
				{
					lobjAux = Engine.GetWorkInstance(lrefAux.getKey(), lrs);
					larrAux.add(lobjAux.getKey());
				}
				lrs.close();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
			marrProcesses = larrAux.toArray(new UUID[larrAux.size()]);
		}

		return marrProcesses;
	}

	public UUID[] GetAgendaOpIDs(SQLServer pdb)
		throws BigBangJewelException
	{
		IEntity lrefAux;
		ArrayList<UUID> larrAux;
		ResultSet lrs;
		ObjectBase lobjAux;

		if ( marrOperations == null )
		{
			larrAux = new ArrayList<UUID>();
			try
			{
				lrefAux = Entity.GetInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_AgendaOp));
				lrs = lrefAux.SelectByMembers(pdb, new int[] {0}, new java.lang.Object[] {getKey()}, new int[0]);
				while ( lrs.next() )
				{
					lobjAux = Engine.GetWorkInstance(lrefAux.getKey(), lrs);
					larrAux.add(lobjAux.getKey());
				}
				lrs.close();
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
			marrOperations = larrAux.toArray(new UUID[larrAux.size()]);
		}

		return marrOperations;
	}

	public void InitNew(UUID[] parrProcIDs, UUID[] parrOpIDs, SQLServer pdb)
		throws BigBangJewelException
	{
		UUID lidAux;
		int i;
		ObjectBase lobjAux;
		ArrayList<UUID> larrAux;

		GetAgendaOpIDs(pdb);
		GetAgendaProcIDs(pdb);
		if ( ((marrProcesses != null) && (marrProcesses.length > 0)) || 
				((marrOperations != null) && (marrOperations.length > 0)) )
			throw new BigBangJewelException("Erro: Não pode redefinir os processos de items de agenda pre-existentes.");
		if ( getKey() == null )
			throw new BigBangJewelException("Erro: Não pode definir o conjunto de processos para um item de agenda antes de gravar.");
		if ( parrProcIDs.length == 0 )
			throw new BigBangJewelException("Erro: Não pode definir um conjunto vazio de processos para um novo item de agenda.");
		if ( (parrOpIDs.length == 0) && !(Constants.UrgID_Completed.equals(getAt(5))) )
			throw new BigBangJewelException("Erro: Não pode definir um conjunto vazio de operações para um novo item de agenda, " +
					"se este não fôr uma notificação.");

		try
		{
			lidAux = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess);
			larrAux = new ArrayList<UUID>();
			for ( i = 0; i < parrProcIDs.length; i++ )
			{
				lobjAux = Engine.GetWorkInstance(lidAux, (UUID)null);
				lobjAux.setAt(0, getKey());
				lobjAux.setAt(1, parrProcIDs[i]);
				lobjAux.SaveToDb(pdb);
				larrAux.add(lobjAux.getKey());
			}
			marrProcesses = larrAux.toArray(new UUID[larrAux.size()]);

			lidAux = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaOp);
			larrAux = new ArrayList<UUID>();
			for ( i = 0; i < parrOpIDs.length; i++ )
			{
				lobjAux = Engine.GetWorkInstance(lidAux, (UUID)null);
				lobjAux.setAt(0, getKey());
				lobjAux.setAt(1, parrOpIDs[i]);
				lobjAux.SaveToDb(pdb);
				larrAux.add(lobjAux.getKey());
			}
			marrOperations = larrAux.toArray(new UUID[larrAux.size()]);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
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
			GetAgendaProcIDs(pdb);
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
			for ( i = 0; i < marrProcesses.length; i++ )
				lrefAux.Delete(pdb, marrProcesses[i]);

			GetAgendaOpIDs(pdb);
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

		marrProcesses = null;
		marrOperations = null;
	}
}
