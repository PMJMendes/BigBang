package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoMarkForClosing
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoMarkForClosing(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_UndoMarkForClosing;
	}

	public String ShortDesc()
	{
		return "Desfazer Marcação para Encerramento";
	}
}
