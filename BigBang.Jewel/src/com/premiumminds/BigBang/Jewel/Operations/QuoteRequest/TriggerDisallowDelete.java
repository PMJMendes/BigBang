package com.premiumminds.BigBang.Jewel.Operations.QuoteRequest;

import java.util.UUID;

import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class TriggerDisallowDelete
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public TriggerDisallowDelete(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_QuoteRequest_TriggerDisallowDelete;
	}
}
