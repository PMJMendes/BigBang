package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExternResumeSubCasualty
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ExternResumeSubCasualty(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_ExternResumeSubCasualty;
	}

	public String ShortDesc()
	{
		return "Reposição do Sub-Sinistro";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Após ter sido eliminado, este sub-sinistro foi reposto.";
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
