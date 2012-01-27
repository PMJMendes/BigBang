package com.premiumminds.BigBang.Jewel.Operations.MgrXFer;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoAcceptXFer
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoAcceptXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_MgrXFer_UndoAcceptXFer;
	}

	public String ShortDesc()
	{
		return "Desfazer Aceitação da Transferência";
	}
}
