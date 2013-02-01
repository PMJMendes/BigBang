package com.premiumminds.BigBang.Jewel.Operations.Expense;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoReceiveReception
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoReceiveReception(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Expense_UndoReceiveReception;
	}

	public String ShortDesc()
	{
		return "Desfazer Recepção do Comprovativo";
	}
}
