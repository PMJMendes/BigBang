package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExternReallowUndoClose
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public ExternReallowUndoClose(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_ExternReallowUndoClose;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		GetProcess().Stop(pdb);
	}
}
