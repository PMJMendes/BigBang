package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class AutoCloseProcess
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public AutoCloseProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_AutoCloseProcess;
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
		GetProcess().Restart(pdb);
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
