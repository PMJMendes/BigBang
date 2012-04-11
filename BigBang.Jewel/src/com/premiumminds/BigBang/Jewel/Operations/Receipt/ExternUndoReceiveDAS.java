package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExternUndoReceiveDAS
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midDASRequestProcess;

	public ExternUndoReceiveDAS(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_ExternUndoReceiveDAS;
	}

	public String ShortDesc()
	{
		return "Desfazer Recepção de Declaração de Ausência de Sinistros";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A recepção da declaração assinada foi retirada. O processo do recibo ficou novamente pendente da recepção de uma DAS.";
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
