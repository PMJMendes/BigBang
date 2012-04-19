package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoDeleteExpense
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoDeleteExpense(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_UndoDeleteHealthExpense;
	}

	public String ShortDesc()
	{
		return "Desfazer Eliminação de Despesa de Saúde";
	}
}
