package com.premiumminds.BigBang.Jewel.Operations.General;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.Operations.DataObjects.ClientData;

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

	protected UUID OpID()
	{
		return Constants.OPID_CreateClient;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Client lobjAux;
		PNProcess lobjProcess;

		try
		{
			lobjAux = Client.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjAux);
			lobjAux.SaveToDb(pdb);

			lobjProcess = (PNProcess)Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
					Jewel.Petri.Constants.ObjID_PNProcess), (UUID)null);
			lobjProcess.setAt(0, Constants.ProcID_Client);
			lobjProcess.setAt(1, lobjAux.getKey());
			lobjProcess.setAt(2, mobjData.midManager);
			lobjProcess.setAt(4, true);
			lobjProcess.SaveToDb(pdb);

			lobjAux.setAt(22, lobjProcess.getKey());
			lobjAux.SaveToDb(pdb);

			if ( mobjContactOps != null )
				mobjContactOps.RunSubOp(pdb, lobjAux.getKey());
			if ( mobjDocOps != null )
				mobjDocOps.RunSubOp(pdb, lobjAux.getKey());

			mobjData.mid = lobjAux.getKey();
			mobjData.mobjPrevValues = null;
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
