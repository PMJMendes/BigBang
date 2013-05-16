package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExternBlockEndProcessSend
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public ExternBlockEndProcessSend(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_ExternBlockEndProcessSend;
	}
}
