package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoDeletePolicy
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoDeletePolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_UndoDeletePolicy;
	}

	public String ShortDesc()
	{
		return "Desfazer Eliminação de Apólice";
	}
}
