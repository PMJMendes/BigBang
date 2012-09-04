package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class ExternResumeClient
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midOtherClientProc;

	public ExternResumeClient(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Client_ExternResumeClient;
	}

	public String ShortDesc()
	{
		return "Reposição do Cliente";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Após ter sido eliminado ou fundido com outro cliente, este cliente foi reposto.";
	}

	public UUID GetExternalProcess()
	{
		return midOtherClientProc;
	}

	protected void Run(SQLServer pdb) throws JewelPetriException
	{
	}
}
