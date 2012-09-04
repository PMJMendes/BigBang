package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoValidateReceipt
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoValidateReceipt(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_UndoValidateReceipt;
	}

	public String ShortDesc()
	{
		return "Desfazer Validação do Recibo";
	}
}
