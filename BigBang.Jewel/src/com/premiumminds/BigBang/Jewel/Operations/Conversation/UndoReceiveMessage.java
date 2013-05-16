package com.premiumminds.BigBang.Jewel.Operations.Conversation;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoReceiveMessage
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoReceiveMessage(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Conversation_UndoReceiveMessage;
	}

	public String ShortDesc()
	{
		return "Dsefazer Recepção de Mensagem";
	}
}
