package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.ExternRequest.CreateExternRequestBase;

public class CreateExternRequest
	extends CreateExternRequestBase
{
	private static final long serialVersionUID = 1L;

	public CreateExternRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_CreateExternRequest;
	}
}
