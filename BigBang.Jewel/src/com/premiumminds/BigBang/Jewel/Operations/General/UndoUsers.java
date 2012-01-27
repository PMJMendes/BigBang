package com.premiumminds.BigBang.Jewel.Operations.General;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoUsers
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoUsers(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_General_UndoUsers;
	}

	public String ShortDesc()
	{
		return "Desfazer Gestão de Utilizadores";
	}
}
