package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class CancelPaymentNotice
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public CancelPaymentNotice(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_CancelPaymentNotice;
	}

	public String ShortDesc()
	{
		return "Cancelamento do Aviso de Cobrança";
	}
}
