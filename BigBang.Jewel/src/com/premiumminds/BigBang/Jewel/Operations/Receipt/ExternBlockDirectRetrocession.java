package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.SilentOperation;

public class ExternBlockDirectRetrocession
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public ExternBlockDirectRetrocession(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_ExternBlockDirectRetrocession;
	}
}
