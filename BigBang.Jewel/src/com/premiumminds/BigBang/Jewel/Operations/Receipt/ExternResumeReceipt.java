package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExternResumeReceipt
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ExternResumeReceipt(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_ExternResumeReceipt;
	}

	public String ShortDesc()
	{
		return "Reposição do Recibo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Após ter sido eliminado, este recibo foi reposto.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb) throws JewelPetriException
	{
	}
}
