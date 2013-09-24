package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoReactivatePolicy
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoReactivatePolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_UndoReactivatePolicy;
	}

	public String ShortDesc()
	{
		return "Desfazer Reactivação de Apólice";
	}
}
