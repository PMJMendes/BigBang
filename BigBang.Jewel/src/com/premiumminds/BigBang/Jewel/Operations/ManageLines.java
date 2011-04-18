package com.premiumminds.BigBang.Jewel.Operations;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class ManageLines
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ManageLines(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return null;
	}

	protected void Run(SQLServer pdb) throws JewelPetriException
	{
	}
}
