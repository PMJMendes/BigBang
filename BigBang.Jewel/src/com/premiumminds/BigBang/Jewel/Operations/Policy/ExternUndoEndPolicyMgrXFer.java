package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.ExternUndoEndMgrXFerBase;

public class ExternUndoEndPolicyMgrXFer
	extends ExternUndoEndMgrXFerBase
{
	private static final long serialVersionUID = 1L;

	public ExternUndoEndPolicyMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ExternUndoEndPolicyMgrXFer;
	}
}
