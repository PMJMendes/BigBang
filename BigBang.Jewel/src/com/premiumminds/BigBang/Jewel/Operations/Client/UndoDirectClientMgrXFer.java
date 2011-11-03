package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoDirectClientMgrXFer
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoDirectClientMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_UndoDirectClientMgrXFer;
	}

	public String ShortDesc()
	{
		return "Desfazer Alteração de Gestor";
	}
}
