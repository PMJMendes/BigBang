package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.ExternUndoEndMgrXFerBase;

public class ExternUndoEndClientMgrXFer
	extends ExternUndoEndMgrXFerBase
{
	private static final long serialVersionUID = 1L;

	public ExternUndoEndClientMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_ExternUndoEndMgrXFer;
	}
}
