package com.premiumminds.BigBang.Jewel.Operations.SignatureRequest;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoReceiveReply
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoReceiveReply(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SigReq_UndoReceiveReply;
	}

	public String ShortDesc()
	{
		return "Desfazer Recepção de Resposta";
	}
}
