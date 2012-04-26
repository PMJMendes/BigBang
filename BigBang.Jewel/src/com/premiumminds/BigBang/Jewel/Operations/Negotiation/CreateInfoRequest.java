package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.InfoRequest.CreateInfoRequestBase;

public class CreateInfoRequest
	extends CreateInfoRequestBase
{
	private static final long serialVersionUID = 1L;

	public CreateInfoRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_CreateInfoRequest;
	}
}
