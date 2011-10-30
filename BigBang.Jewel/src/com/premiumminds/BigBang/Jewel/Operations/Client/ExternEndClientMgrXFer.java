package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.ExternEndMgrXFerBase;

public class ExternEndClientMgrXFer
	extends ExternEndMgrXFerBase
{
	private static final long serialVersionUID = 1L;

	public ExternEndClientMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ExternEndMgrXFer;
	}
}
