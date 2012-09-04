package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.util.UUID;

import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class AutoUnblockOnUndoAutoVoid
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public AutoUnblockOnUndoAutoVoid(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubPolicy_AutoUnblockOnUndoAutoVoid;
	}
}
