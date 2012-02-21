package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoDeleteNegotiation
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoDeleteNegotiation(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_UndoDeleteNegotiation;
	}

	public String ShortDesc()
	{
		return "Desfazer Eliminação de Negociação";
	}
}
