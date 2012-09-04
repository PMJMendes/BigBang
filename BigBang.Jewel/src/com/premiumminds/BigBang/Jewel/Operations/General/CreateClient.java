package com.premiumminds.BigBang.Jewel.Operations.General;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ClientData;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;

public class CreateClient
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ClientData mobjData;
	public ContactOps mobjContactOps;
	public DocOps mobjDocOps;

	public CreateClient(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_General_CreateClient;
	}

	public String ShortDesc()
	{
		return "Criação de Cliente";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder();
		lstrResult.append("Foi criado o seguinte cliente:");
		lstrResult.append(pstrLineBreak);

		mobjData.Describe(lstrResult, pstrLineBreak);

		if ( mobjContactOps != null )
			mobjContactOps.LongDesc(lstrResult, pstrLineBreak);

		if ( mobjDocOps != null )
			mobjDocOps.LongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return mobjData.midProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Client lobjAux;
		IScript lobjScript;
		IProcess lobjProc; 

		try
		{
			mobjData.mlngNumber = GetNewClientNumber();

			lobjAux = Client.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, lobjAux.getKey());
			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, lobjAux.getKey());

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_Client);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjAux.getKey(), null,
					GetContext(), pdb);
			lobjProc.SetManagerID(mobjData.midManager, pdb);

			mobjData.mid = lobjAux.getKey();
			mobjData.midProcess = lobjProc.getKey();
			mobjData.mobjPrevValues = null;
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	private int GetNewClientNumber()
		throws BigBangJewelException
	{
		int[] larrSorts;
		IEntity lrefClients;
        MasterDB ldb;
        ResultSet lrsClients;
        int llngResult;

		larrSorts = new int[1];
		larrSorts[0] = -1;

		try
		{
			lrefClients = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Client)); 
			ldb = new MasterDB();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsClients = lrefClients.SelectByMembers(ldb, new int[0], new java.lang.Object[0], larrSorts);
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		llngResult = 1;
		try
		{
			if ( lrsClients.next() )
				llngResult = lrsClients.getInt(3) + 1;
		}
		catch (Throwable e)
		{
			try { lrsClients.close(); } catch (SQLException e1) {}
			try { ldb.Disconnect(); } catch (SQLException e1) {}
			throw new BigBangJewelException(e.getMessage(), e);
		}

		try
		{
			lrsClients.close();
		}
		catch (Throwable e)
		{
			try { ldb.Disconnect(); } catch (SQLException e1) {}
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

		return llngResult;
	}
}
