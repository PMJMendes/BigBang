package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ReopenProcess
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public UUID midMotive;
	private String mstrMotive;

	public ReopenProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_ReopenProcess;
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
		try
		{
			mstrMotive = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_CasualtyReopenMotives),
					midMotive).getLabel();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		GetProcess().Restart(pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O processo será re-encerrado.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O processo foi re-encerrado após ter sido indevidamente reaberto.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		GetProcess().Stop(pdb);
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
