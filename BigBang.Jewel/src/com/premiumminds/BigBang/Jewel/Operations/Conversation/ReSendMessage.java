package com.premiumminds.BigBang.Jewel.Operations.Conversation;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

public class ReSendMessage
	extends SendMessage
{
	private static final long serialVersionUID = 1L;

	public ReSendMessage(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Conversation_ReSendMessage;
	}

	public String ShortDesc()
	{
		return "Re-Envio de Informação";
	}
}
