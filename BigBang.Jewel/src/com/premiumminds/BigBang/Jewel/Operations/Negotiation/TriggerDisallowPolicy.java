package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SilentOperation;

public class TriggerDisallowPolicy
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public TriggerDisallowPolicy(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_TriggerDisallowPolicies;
	}

	protected void Run(SQLServer pdb) throws JewelPetriException
	{
		GetProcess().Stop(pdb);
	}
}
