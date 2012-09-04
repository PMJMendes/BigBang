package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExternResumeSubPolicy
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ExternResumeSubPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_ExternResumeSubPolicy;
	}

	public String ShortDesc()
	{
		return "Reposição da Apólice Adesão";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Após ter sido eliminada, esta apólice adesão foi reposta.";
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
