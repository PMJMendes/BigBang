package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class ExternReceiveDAS
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midDASRequestProcess;

	public ExternReceiveDAS(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_ExternReceiveDAS;
	}

	public String ShortDesc()
	{
		return "Recepção de Declaração de Ausência de Sinistros";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Foi recebida uma declaração assinada pelo cliente. O processo do recibo pode prosseguir normalmente.";
	}

	public UUID GetExternalProcess()
	{
		return midDASRequestProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
	}
}
