package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoValidatePolicy
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoValidatePolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_UndoValidatePolicy;
	}

	public String ShortDesc()
	{
		return "Desfazer Validação da Apólice";
	}
}
