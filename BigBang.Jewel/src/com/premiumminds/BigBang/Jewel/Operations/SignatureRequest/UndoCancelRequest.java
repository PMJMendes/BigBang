package com.premiumminds.BigBang.Jewel.Operations.SignatureRequest;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoCancelRequest
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoCancelRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SigReq_UndoCancelRequest;
	}

	public String ShortDesc()
	{
		return "Desfazer Cancelamento de Pedido";
	}
}
