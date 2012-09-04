package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoMediatorAccounting
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoMediatorAccounting(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_UndoMediatorAccounting;
	}

	public String ShortDesc()
	{
		return "Desfazer Retrocessão";
	}
}
