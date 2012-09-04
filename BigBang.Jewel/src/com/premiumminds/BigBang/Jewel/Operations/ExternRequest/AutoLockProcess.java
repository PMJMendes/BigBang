package com.premiumminds.BigBang.Jewel.Operations.ExternRequest;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.SilentOperation;

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
		return Constants.OPID_ExternReq_AutoLockProcess;
	}
}
