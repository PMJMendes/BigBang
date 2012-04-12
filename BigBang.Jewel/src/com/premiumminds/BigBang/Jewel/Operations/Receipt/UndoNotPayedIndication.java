package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoNotPayedIndication
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoNotPayedIndication(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_UndoNotPayedIndication;
	}

	public String ShortDesc()
	{
		return "Desfazer Indicação de Não Pagamento";
	}
}
