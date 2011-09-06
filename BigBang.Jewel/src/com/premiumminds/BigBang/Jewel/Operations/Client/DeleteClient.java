package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Operations.DataObjects.ClientData;
import com.premiumminds.BigBang.Jewel.Operations.General.TriggerDeleteClient;

public class DeleteClient
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midClient;

	public DeleteClient(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_DeleteClient;
	}

	public String ShortDesc()
	{
		return "Eliminação de Cliente (Placeholder)";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O cliente foi eliminado. A sua reposição foi/é possível a partir do sistema geral.";
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		TriggerDeleteClient lobjOp;

		try
		{
			lobjOp = new TriggerDeleteClient(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lobjOp.mobjData = new ClientData();
			lobjOp.mobjData.mid = midClient;
			TriggerOp(lobjOp);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
