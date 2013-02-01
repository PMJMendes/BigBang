package com.premiumminds.BigBang.Jewel.Operations.MgrXFer;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoExecXFer
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoExecXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_MgrXFer_UndoExecXFer;
	}

	public String ShortDesc()
	{
		return "Desfazer Execução da Transferência";
	}
}
