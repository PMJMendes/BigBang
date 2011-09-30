package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoDirectMgrXFer
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoDirectMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_UndoDirectMgrXFer;
	}

	public String ShortDesc()
	{
		return "Desfazer Alteração de Gestor";
	}
}
