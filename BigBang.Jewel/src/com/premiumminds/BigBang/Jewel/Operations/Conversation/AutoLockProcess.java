package com.premiumminds.BigBang.Jewel.Operations.Conversation;

import java.util.UUID;

import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class AutoLockProcess
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public AutoLockProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Conversation_AutoLockProcess;
	}
}
