package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.SilentOperation;

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
		return Constants.OPID_TriggerDisallowDelete;
	}
}
