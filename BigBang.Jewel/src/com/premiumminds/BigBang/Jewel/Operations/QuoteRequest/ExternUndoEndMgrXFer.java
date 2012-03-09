package com.premiumminds.BigBang.Jewel.Operations.QuoteRequest;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.ExternUndoEndMgrXFerBase;

public class ExternUndoEndMgrXFer
	extends ExternUndoEndMgrXFerBase
{
	private static final long serialVersionUID = 1L;

	public ExternUndoEndMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_QuoteRequest_ExternUndoEndMgrXFer;
	}
}
