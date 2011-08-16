package com.premiumminds.BigBang.Jewel.Operations.General;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoDeleteClient
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoDeleteClient(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_UndoDeleteClient;
	}

	public String ShortDesc()
	{
		return "Desfazer Eliminação de Cliente";
	}
}
