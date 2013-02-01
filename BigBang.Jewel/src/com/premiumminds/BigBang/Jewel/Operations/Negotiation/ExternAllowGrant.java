package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.util.UUID;

import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExternAllowGrant
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public ExternAllowGrant(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_ExternAllowGrant;
	}
}
