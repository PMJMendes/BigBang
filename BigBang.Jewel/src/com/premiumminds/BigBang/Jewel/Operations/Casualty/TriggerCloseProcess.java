package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ExternDisallowUndoClose;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ExternReallowUndoClose;

public class TriggerCloseProcess
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public TriggerCloseProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_TriggerCloseProcess;
	}

	public String ShortDesc()
	{
		return "Fecho Automático do Processo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O processo de sinistro foi encerrado automaticamente, após todos os seus sub-sinitros terem sido encerrados.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess[] larrSubProcs;
		int i;

		larrSubProcs = GetProcess().GetCurrentSubProcesses(pdb);
		for ( i = 0; i < larrSubProcs.length; i++ )
		{
			if ( !Constants.ProcID_SubCasualty.equals(larrSubProcs[i].GetScriptID()) )
				continue;

// JMMM - ExternDisallowUndoClose is now OverrideRunning
//			if ( !larrSubProcs[i].IsRunning() )
//				larrSubProcs[i].Restart(pdb);
			TriggerOp(new ExternDisallowUndoClose(larrSubProcs[i].getKey()), pdb);
		}

		GetProcess().Stop(pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O processo de sinistro será reaberto e não tornará a ser encerrado automaticamente.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O processo de sinistro foi reaberto. Pode ser novamente encerrado manualmente, mas não tornará a ser encerrado automaticamente.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess[] larrSubProcs;
		int i;

		larrSubProcs = GetProcess().GetCurrentSubProcesses(pdb);
		for ( i = 0; i < larrSubProcs.length; i++ )
		{
			if ( !Constants.ProcID_SubCasualty.equals(larrSubProcs[i].GetScriptID()) )
				continue;

// JMMM - ExternReallowUndoClose is now OverrideRunning
//			if ( !larrSubProcs[i].IsRunning() )
//				larrSubProcs[i].Restart(pdb);
			TriggerOp(new ExternReallowUndoClose(larrSubProcs[i].getKey()), pdb);
		}

		GetProcess().Restart(pdb);
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
