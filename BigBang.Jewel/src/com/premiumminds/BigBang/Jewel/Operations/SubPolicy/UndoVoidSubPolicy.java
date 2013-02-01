package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoVoidSubPolicy
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoVoidSubPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_UndoVoidSubPolicy;
	}

	public String ShortDesc()
	{
		return "Desfazer Anulação da Apólice Adesão";
	}
}
