package com.premiumminds.BigBang.Jewel.Operations.Expense;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoReceiveReturn
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoReceiveReturn(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Expense_UndoReceiveReturn;
	}

	public String ShortDesc()
	{
		return "Desfazer Recepção de Devolução";
	}
}
