package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExternDisallowUndoClose
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public ExternDisallowUndoClose(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_ExternDisallowUndoClose;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		GetProcess().Stop(pdb);
	}
}
