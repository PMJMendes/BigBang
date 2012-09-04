package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

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
		return Constants.OPID_Policy_UndoTransferToClient;
	}

	public String ShortDesc()
	{
		return "Desfazer Transferência de Cliente";
	}
}
