package com.premiumminds.BigBang.Jewel.Objects;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.JewelEngineException;
import Jewel.Petri.SysObjects.ProcessData;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;

public class MgrXFer
	extends ProcessData
{
    public static MgrXFer GetInstance(UUID pidNameSpace, UUID pidKey)
		throws BigBangJewelException
	{
	    try
	    {
			return (MgrXFer)Engine.GetWorkInstance(Engine.FindEntity(pidNameSpace, Constants.ObjID_MgrXFer), pidKey);
		}
	    catch (Throwable e)
	    {
	    	throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	private MgrXFerProcHolder[] marrProcessHolders;

	public void Initialize()
		throws JewelEngineException
	{
		int[] larrMembers;
		java.lang.Object[] larrParams;
		IEntity lrefAux;
		MasterDB ldb;
		ArrayList<MgrXFerProcHolder> larrAux;
		ResultSet lrs;

		larrMembers = new int[1];
		larrMembers[0] = 0;
		larrParams = new java.lang.Object[1];
		larrParams[0] = getKey();

		larrAux = new ArrayList<MgrXFerProcHolder>();
		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(getNameSpace(), Constants.ObjID_MgrXFerProc));
			ldb = new MasterDB();
			lrs = lrefAux.SelectByMembers(ldb, larrMembers, larrParams, new int[0]);
			while ( lrs.next() )
				larrAux.add(MgrXFerProcHolder.GetInstance(getNameSpace(), lrs));
			lrs.close();
			ldb.Disconnect();
		}
		catch (Throwable e)
		{
			throw new JewelEngineException(e.getMessage(), e);
		}

		marrProcessHolders = larrAux.toArray(new MgrXFerProcHolder[larrAux.size()]);
	}

    public String getLabel()
    {
    	User lobjUser;

    	try
    	{
			lobjUser = User.GetInstance(Engine.getCurrentNameSpace(), (UUID)getAt(2));
		}
    	catch (JewelEngineException e)
    	{
    		return "(Erro)";
		}

    	return lobjUser.getDisplayName();
    }

	public UUID[] GetProcessIDs()
	{
		UUID[] larrResult;
		int i;

		larrResult = new UUID[marrProcessHolders.length];
		for ( i = 0; i < larrResult.length; i++ )
			larrResult[i] = (UUID) marrProcessHolders[i].getAt(1);

		return larrResult;
	}

	public void InitNew(UUID[] parrProcIDs, SQLServer pdb)
		throws BigBangJewelException
	{
		ArrayList<MgrXFerProcHolder> larrAux;
		UUID lidAux;
		int i;
		MgrXFerProcHolder lobjAux;

		if ( (marrProcessHolders != null) && (marrProcessHolders.length > 0) )
			throw new BigBangJewelException("Erro: Não pode redefinir os processos de transferência pre-existentes.");
		if ( getKey() == null )
			throw new BigBangJewelException("Erro: Não pode definir o conjunto de processos a transferir antes de gravar.");
		if ( parrProcIDs.length == 0 )
			throw new BigBangJewelException("Erro: Não pode definir um conjunto vazio de processos para uma nova transferência.");

		larrAux = new ArrayList<MgrXFerProcHolder>();

		try
		{
			lidAux = Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_MgrXFerProc);

			for ( i = 0; i < parrProcIDs.length; i++ )
			{
				lobjAux = (MgrXFerProcHolder)Engine.GetWorkInstance(lidAux, (UUID)null);
				lobjAux.setAt(0, getKey());
				lobjAux.setAt(1, parrProcIDs[i]);
				lobjAux.SaveToDb(pdb);

				larrAux.add(lobjAux);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		marrProcessHolders = larrAux.toArray(new MgrXFerProcHolder[larrAux.size()]);
	}

	public void ClearData(SQLServer pdb)
		throws BigBangJewelException
	{
		IEntity lrefAux;
		int i;

		if ( getKey() == null )
			throw new BigBangJewelException("Erro: Não pode limpar os dados de uma transferência nova antes de gravar.");

		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_MgrXFerProc));
			for ( i = 0; i < marrProcessHolders.length; i++ )
				lrefAux.Delete(pdb, marrProcessHolders[i].getKey());
		}
		catch (Throwable e)
		{
			marrProcessHolders = null;
			throw new BigBangJewelException(e.getMessage(), e);
		}

		marrProcessHolders = new MgrXFerProcHolder[0];
	}

	public UUID GetProcessID()
	{
		return (UUID)getAt(0);
	}

	public void SetProcessID(UUID pidProcess)
	{
		internalSetAt(0, pidProcess);
	}

	public UUID GetOldManagerID()
	{
		return (UUID)getAt(1);
	}

	public UUID GetNewManagerID()
	{
		return (UUID)getAt(2);
	}

	public String GetTag()
	{
		return (String)getAt(3);
	}

	public UUID GetRequestingUser()
	{
		return (UUID)getAt(4);
	}

	public boolean IsMassTransfer()
	{
		return (Boolean)getAt(5);
	}

	public UUID GetOuterObjectType()
	{
		return (UUID)getAt(6);
	}
}
