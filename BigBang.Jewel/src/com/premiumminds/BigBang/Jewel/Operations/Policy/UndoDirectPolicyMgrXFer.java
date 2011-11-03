package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoDirectPolicyMgrXFer
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoDirectPolicyMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_UndoDirectPolicyMgrXFer;
	}

	public String ShortDesc()
	{
		return "Desfazer Alteração de Gestor";
	}
}
