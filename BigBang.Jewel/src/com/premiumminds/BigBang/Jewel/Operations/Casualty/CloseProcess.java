package com.premiumminds.BigBang.Jewel.Operations.Casualty;

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
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
