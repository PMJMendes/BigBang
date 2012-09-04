package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteSubPolicy;

public class DeleteSubPolicy
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID midSubPolicy;
	public transient String mstrReason;

	public DeleteSubPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_DeleteSubPolicy;
	}

	public String ShortDesc()
	{
		return "Eliminação de Apólice Adesão";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A apólice adesão foi eliminada. A sua reposição foi/é possível a partir da apólice-mãe.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		ExternDeleteSubPolicy lobjOp;

		try
		{
			lobjOp = new ExternDeleteSubPolicy(GetProcess().GetParent().getKey());
			lobjOp.midSubPolicy = midSubPolicy;
			lobjOp.mstrReason = mstrReason;
			TriggerOp(lobjOp, pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
