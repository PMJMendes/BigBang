package com.premiumminds.BigBang.Jewel.Operations.Policy;

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
		return Constants.OPID_Policy_AutoProcessSubProcs;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess[] larrSubs;
		boolean b;
		int i;

		larrSubs = GetProcess().GetCurrentSubProcesses(pdb);

		if ( larrSubs == null )
			return;

		b = false;
		for ( i = 0; i < larrSubs.length; i++ )
		{
//			if ( larrSubs[i].IsRunning() || !larrSubs[i].GetScript().IsTopLevel() )
//			{
				b = true;
				break;
//			}
		}

		if ( b )
			TriggerOp(new TriggerDisallowDelete(this.GetProcess().getKey()), pdb);
	}
}
