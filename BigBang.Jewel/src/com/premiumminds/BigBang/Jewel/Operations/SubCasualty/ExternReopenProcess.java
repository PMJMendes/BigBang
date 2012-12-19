package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.ExternCloseSubCasualty;

public class ExternReopenProcess
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public String mstrMotive;

	public ExternReopenProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_ExternReopenProcess;
	}

	public String ShortDesc()
	{
		return "Reabertura do Processo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O processo foi reaberto pelo seguinte motivo:" + pstrLineBreak + mstrMotive;
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O sub-sinistro será re-encerrado.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O sub-sinistro foi re-encerrado após ter sido indevidamente reaberto.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		GetProcess().Stop(pdb);

		TriggerOp(new ExternCloseSubCasualty(GetProcess().GetParent().getKey()), pdb);
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
