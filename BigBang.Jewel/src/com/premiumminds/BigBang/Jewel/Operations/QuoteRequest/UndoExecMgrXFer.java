package com.premiumminds.BigBang.Jewel.Operations.QuoteRequest;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoExecMgrXFer
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoExecMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_QuoteRequest_UndoExecMgrXFer;
	}

	public String ShortDesc()
	{
		return "Desfazer Transferência de Gestor";
	}
}
