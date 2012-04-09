package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoDeleteSubCasualty
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoDeleteSubCasualty(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_UndoDeleteSubCasualty;
	}

	public String ShortDesc()
	{
		return "Desfazer Eliminação de Sub-Sinistro";
	}
}
