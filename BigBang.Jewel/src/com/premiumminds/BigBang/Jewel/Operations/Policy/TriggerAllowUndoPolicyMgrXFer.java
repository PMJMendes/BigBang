package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class TriggerAllowUndoPolicyMgrXFer
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public TriggerAllowUndoPolicyMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_TriggerAllowUndoPolicyMgrXFer;
	}
}
