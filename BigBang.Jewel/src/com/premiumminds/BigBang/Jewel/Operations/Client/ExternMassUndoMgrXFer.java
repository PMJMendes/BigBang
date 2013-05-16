package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.ExternMassUndoMgrXFerBase;

public class ExternMassUndoMgrXFer
	extends ExternMassUndoMgrXFerBase
{
	private static final long serialVersionUID = 1L;

	public ExternMassUndoMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Client_ExternMassUndoMgrXFer;
	}
}
