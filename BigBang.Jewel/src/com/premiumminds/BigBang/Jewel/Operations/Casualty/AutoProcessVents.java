package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class AutoProcessVents
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public AutoProcessVents(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_AutoProcessVents;
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
			if ( larrSubs[i].GetScript().IsTopLevel() && larrSubs[i].IsRunning() )
			{
				b = true;
				break;
			}
		}

		if ( b )
			TriggerOp(new TriggerDisallowClose(GetProcess().getKey()), pdb);
		else
			TriggerOp(new AutoCloseProcess(GetProcess().getKey()), pdb);
	}
}
