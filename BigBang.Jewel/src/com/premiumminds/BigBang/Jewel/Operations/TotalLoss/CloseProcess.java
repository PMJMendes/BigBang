package com.premiumminds.BigBang.Jewel.Operations.TotalLoss;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;

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
		return Constants.OPID_TotalLoss_CloseProcess;
	}

	public String ShortDesc()
	{
		return "Fecho do Processo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O processo de gestão de perda total foi encerrado.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		GetProcess().Stop(pdb);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "O processo será reaberto.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "O processo de gestão de perda total foi reaberto.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		GetProcess().Restart(pdb);
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
