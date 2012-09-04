package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoDebitNote
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoDebitNote(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_UndoDebitNote;
	}

	public String ShortDesc()
	{
		return "Desfazer Criação de Nota de Débito";
	}
}
