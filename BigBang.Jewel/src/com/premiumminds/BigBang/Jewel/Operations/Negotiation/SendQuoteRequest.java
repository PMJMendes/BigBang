package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.util.UUID;

import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class SendQuoteRequest
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public SendQuoteRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_SendQuoteRequest;
	}
}
