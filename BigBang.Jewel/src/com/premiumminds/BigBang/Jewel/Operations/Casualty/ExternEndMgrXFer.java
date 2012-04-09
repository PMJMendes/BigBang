package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.ExternEndMgrXFerBase;

public class ExternEndMgrXFer
	extends ExternEndMgrXFerBase
{
	private static final long serialVersionUID = 1L;

	public ExternEndMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_ExternEndMgrXFer;
	}
}
