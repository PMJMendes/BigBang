package com.premiumminds.BigBang.Jewel.Operations.General;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoCoefficients
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoCoefficients(UUID pidProcess)
	{
		super(pidProcess);
	}

	public String ShortDesc()
	{
		return "Desfazer Gestão de Valores";
	}

	protected UUID OpID()
	{
		return Constants.OPID_General_UndoCoefficients;
	}
}
