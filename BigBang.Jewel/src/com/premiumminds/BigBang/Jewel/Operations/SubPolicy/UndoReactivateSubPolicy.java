package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoReactivateSubPolicy
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoReactivateSubPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_UndoReactivateSubPolicy;
	}

	public String ShortDesc()
	{
		return "Desfazer Reactivação de Adesão";
	}
}
