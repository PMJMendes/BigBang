package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoRejectClosing
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoRejectClosing(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_UndoRejectClosing;
	}

	public String ShortDesc()
	{
		return "Desfazer Rejeição de Fecho";
	}
}
