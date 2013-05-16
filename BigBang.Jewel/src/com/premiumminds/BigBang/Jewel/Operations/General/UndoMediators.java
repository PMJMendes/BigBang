package com.premiumminds.BigBang.Jewel.Operations.General;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoMediators
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoMediators(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_General_UndoMediators;
	}

	public String ShortDesc()
	{
		return "Desfazer Gestão de Mediadores";
	}
}
