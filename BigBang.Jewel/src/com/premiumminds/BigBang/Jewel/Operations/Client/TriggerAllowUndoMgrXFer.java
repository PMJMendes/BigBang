package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import Jewel.Petri.SysObjects.SilentOperation;

public class TriggerAllowUndoMgrXFer
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public TriggerAllowUndoMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return null;
	}
}
