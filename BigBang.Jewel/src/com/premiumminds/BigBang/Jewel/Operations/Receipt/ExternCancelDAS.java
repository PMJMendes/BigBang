package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExternCancelDAS
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midDASRequestProcess;

	public ExternCancelDAS(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_ExternCancelDAS;
	}

	public String ShortDesc()
	{
		return "Cancelamento de Declaração de Ausência de Sinistros";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Foi cancelada uma declaração assinada pelo cliente. O recibo foi marcado para devolução do pagamento.";
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
