package com.premiumminds.BigBang.Jewel.Operations.ExternRequest;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoReceiveAdditional
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoReceiveAdditional(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ExternReq_UndoReceiveAdditional;
	}

	public String ShortDesc()
	{
		return "Dsefazer Recepção Adicional";
	}
}
