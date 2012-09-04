package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoCreateExternRequest
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoCreateExternRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_UndoCreateExternRequest;
	}

	public String ShortDesc()
	{
		return "Desfazer Criação de Pedido Externo";
	}
}
