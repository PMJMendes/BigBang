package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoReopenProcess
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoReopenProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_UndoReopenProcess;
	}

	public String ShortDesc()
	{
		return "Desfazer Reabertura do Processo";
	}
}
