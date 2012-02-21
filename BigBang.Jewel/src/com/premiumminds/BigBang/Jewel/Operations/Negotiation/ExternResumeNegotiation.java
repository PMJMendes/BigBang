package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class ExternResumeNegotiation
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ExternResumeNegotiation(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_ExternResumeNegotiation;
	}

	public String ShortDesc()
	{
		return "Reposição da Negociação";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Após ter sido eliminada, esta negociação foi reposta.";
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
