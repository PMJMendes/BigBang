package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

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
		return Constants.OPID_SubCasualty_UndoReopenProcess;
	}

	public String ShortDesc()
	{
		return "Desfazer Reabertura do Processo";
	}
}
