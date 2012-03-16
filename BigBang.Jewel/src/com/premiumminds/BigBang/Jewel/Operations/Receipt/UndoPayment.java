package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoPayment
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoPayment(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_UndoPayment;
	}

	public String ShortDesc()
	{
		return "Desfazer Cobrança";
	}
}
