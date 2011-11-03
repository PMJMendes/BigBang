package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class TriggerAllowUndoClientMgrXFer
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public TriggerAllowUndoClientMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_TriggerAllowUndoClientMgrXFer;
	}
}
