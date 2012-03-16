package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class AutoCheckDebitNote
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public AutoCheckDebitNote(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_AutoCheckDebitNote;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		boolean b;
		ResultSet lrsDebitNotes;
		IEntity lrefDebitNotes;
		TriggerDisallowDebitNote lopTDDN;

		b = false;
		lrsDebitNotes = null;
		try
		{
			lrefDebitNotes = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_DebitNote));
			lrsDebitNotes = lrefDebitNotes.SelectByMembers(pdb,
					new int[] {Constants.FKProcess_In_DebitNote, Constants.FKReceipt_In_DebitNote},
					new java.lang.Object[] {GetProcess().GetParent().getKey(), null}, null);
			if ( lrsDebitNotes.next() )
				b = true;
			lrsDebitNotes.close();
		}
		catch (Throwable e)
		{
			if ( lrsDebitNotes != null )
				try { lrsDebitNotes.close(); } catch (SQLException e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( !b )
		{
			lopTDDN = new TriggerDisallowDebitNote(GetProcess().getKey());
			TriggerOp(lopTDDN, pdb);
		}
	}
}
