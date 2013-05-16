package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class ExternResumeCasualty
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ExternResumeCasualty(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_ExternResumeCasualty;
	}

	public String ShortDesc()
	{
		return "Reposição do Sinistro";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Após ter sido eliminado, este sinistro foi reposto.";
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
