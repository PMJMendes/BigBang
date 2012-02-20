package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoValidateSubPolicy
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoValidateSubPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_UndoValidateSubPolicy;
	}

	public String ShortDesc()
	{
		return "Desfazer Validação da Apólice Adesão";
	}
}
