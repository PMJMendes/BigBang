package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoCreateConversation
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoCreateConversation(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_UndoCreateConversation;
	}

	public String ShortDesc()
	{
		return "Desfazer Criação de Pedido Externo";
	}
}
