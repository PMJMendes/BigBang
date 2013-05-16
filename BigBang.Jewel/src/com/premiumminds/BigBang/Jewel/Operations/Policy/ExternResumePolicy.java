package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExternResumePolicy
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ExternResumePolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_ExternResumePolicy;
	}

	public String ShortDesc()
	{
		return "Reposição da Apólice";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Após ter sido eliminada, esta apólice foi reposta.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
	}
}
