package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoManageData
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoManageData(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_UndoManageData;
	}

	public String ShortDesc()
	{
		return "Desfazer Alteração de Dados";
	}
}
