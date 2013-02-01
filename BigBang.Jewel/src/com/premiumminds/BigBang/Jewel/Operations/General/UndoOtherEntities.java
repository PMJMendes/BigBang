package com.premiumminds.BigBang.Jewel.Operations.General;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoOtherEntities
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoOtherEntities(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_General_UndoOtherEntities;
	}

	public String ShortDesc()
	{
		return "Desfazer Gestão de Outras Entidades";
	}
}
