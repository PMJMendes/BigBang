package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.Conversation.CreateConversationBase;

public class CreateConversation
	extends CreateConversationBase
{
	private static final long serialVersionUID = 1L;

	public CreateConversation(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_CreateConversation;
	}
}
