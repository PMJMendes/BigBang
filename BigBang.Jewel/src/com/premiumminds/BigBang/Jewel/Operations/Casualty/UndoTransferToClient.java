package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoTransferToClient
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoTransferToClient(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_UndoTransferToClient;
	}

	public String ShortDesc()
	{
		return "Desfazer Transferência de Cliente";
	}
}
