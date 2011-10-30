package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoManageClientData
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoManageClientData(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_UndoManageClientData;
	}

	public String ShortDesc()
	{
		return "Desfazer Alteração de Dados";
	}
}
