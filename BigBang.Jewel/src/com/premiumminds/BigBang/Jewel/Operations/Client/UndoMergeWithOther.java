package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoMergeWithOther
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoMergeWithOther(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_UndoMergeOtherClient;
	}

	public String ShortDesc()
	{
		return "Desfazer Fusão de Clientes";
	}
}
