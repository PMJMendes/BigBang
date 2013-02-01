package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ExternDisallowUndoClose;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ExternReallowUndoClose;

public class CloseProcess
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public CloseProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_CloseProcess;
	}

	public String ShortDesc()
	{
		return "Fecho do Processo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O processo de sinistro foi encerrado.";
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

			if ( !larrSubProcs[i].IsRunning() )
				larrSubProcs[i].Restart(pdb);
			TriggerOp(new ExternDisallowUndoClose(larrSubProcs[i].getKey()), pdb);
		}

		GetProcess().Stop(pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O processo de sinistro será reaberto.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O processo de sinistro foi reaberto.";
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

			if ( !larrSubProcs[i].IsRunning() )
				larrSubProcs[i].Restart(pdb);
			TriggerOp(new ExternReallowUndoClose(larrSubProcs[i].getKey()), pdb);
		}

		GetProcess().Restart(pdb);
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
