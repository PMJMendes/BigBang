package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoInsurerAccounting
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoInsurerAccounting(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_UndoInsurerAccounting;
	}

	public String ShortDesc()
	{
		return "Desfazer Prestação de Contas";
	}
}
