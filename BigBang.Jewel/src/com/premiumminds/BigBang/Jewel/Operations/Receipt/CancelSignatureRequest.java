package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class CancelSignatureRequest
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public CancelSignatureRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_CancelSignatureRequest;
	}

	public String ShortDesc()
	{
		return "Cancelamento do Pedido de Assinatura";
	}
}
