package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.ExternEndMgrXFerBase;

public class ExternEndPolicyMgrXFer
	extends ExternEndMgrXFerBase
{
	private static final long serialVersionUID = 1L;

	public ExternEndPolicyMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ExternEndPolicyMgrXFer;
	}
}
