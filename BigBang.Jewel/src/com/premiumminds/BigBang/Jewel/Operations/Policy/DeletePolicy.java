package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.PolicyData;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Operations.Client.ExternDeletePolicy;

public class DeletePolicy
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midPolicy;

	public DeletePolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_DeletePolicy;
	}

	public String ShortDesc()
	{
		return "Eliminação de Apólice";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A apólice foi eliminada. A sua reposição foi/é possível a partir do cliente.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		ExternDeletePolicy lobjOp;

		try
		{
			lobjOp = new ExternDeletePolicy(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lobjOp.mobjData = new PolicyData();
			lobjOp.mobjData.mid = midPolicy;
			TriggerOp(lobjOp);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
