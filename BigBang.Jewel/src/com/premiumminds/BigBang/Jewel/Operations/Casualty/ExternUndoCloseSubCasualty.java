package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.SilentOperation;

public class ExternUndoCloseSubCasualty
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public ExternUndoCloseSubCasualty(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_ExternUndoCloseSubCasualty;
	}
}
