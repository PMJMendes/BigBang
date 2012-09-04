package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExternCloseSubCasualty
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public ExternCloseSubCasualty(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_ExternCloseSubCasualty;
	}
}
