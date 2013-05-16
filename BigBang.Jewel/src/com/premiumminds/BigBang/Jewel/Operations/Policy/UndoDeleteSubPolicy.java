package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoDeleteSubPolicy
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoDeleteSubPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_UndoDeleteSubPolicy;
	}

	public String ShortDesc()
	{
		return "Desfazer Eliminação de Apólice Adesão";
	}
}
