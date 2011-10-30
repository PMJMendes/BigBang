package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class AutoProcessSubs
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public AutoProcessSubs(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_AutoProcessSubProcs;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess[] larrSubs;

		larrSubs = GetProcess().GetCurrentSubProcesses();

		if ( (larrSubs != null) && (larrSubs.length > 0) )
			TriggerOp(new TriggerDisallowPolicies(this.GetProcess().getKey()));
	}
}
