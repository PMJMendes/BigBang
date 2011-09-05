package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class TriggerAllowPolicies
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public TriggerAllowPolicies(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_TriggerAllowPolicies;
	}

	public String ShortDesc()
	{
		return "Activação de Criação de Apólices (Trigger)";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Após análise dos dados submetidos, a criação de apólices neste cliente foi activada.";
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		// Null Op. Node count manipulation only.
	}
}
