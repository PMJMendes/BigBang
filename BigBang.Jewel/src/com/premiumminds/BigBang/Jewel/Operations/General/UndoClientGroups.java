package com.premiumminds.BigBang.Jewel.Operations.General;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoClientGroups
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoClientGroups(UUID pidProcess)
	{
		super(pidProcess);
	}

	public String ShortDesc()
	{
		return "Desfazer Gestão de Grupos de Clientes";
	}

	protected UUID OpID()
	{
		return Constants.OPID_UndoGroups;
	}
}
