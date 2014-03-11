package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SilentOperation;

public class TriggerUndoShortCircuit
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public TriggerUndoShortCircuit(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_TriggerUndoShortCircuit;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Receipt lobjReceipt;

		lobjReceipt = (Receipt)GetProcess().GetData();

		try
		{
			lobjReceipt.setAt(Receipt.I.STATUS, Constants.StatusID_Initial);
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
