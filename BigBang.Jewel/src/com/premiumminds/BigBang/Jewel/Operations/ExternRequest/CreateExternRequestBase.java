package com.premiumminds.BigBang.Jewel.Operations.ExternRequest;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

public abstract class CreateExternRequestBase
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midExternProcess;

	public CreateExternRequestBase(UUID pidProcess)
	{
		super(pidProcess);
	}

	public String ShortDesc()
	{
		return "Criação de Sub-Processo: Pedido Externo de Informação";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return null;
	}

	public UUID GetExternalProcess()
	{
		return midExternProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return null;
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return null;
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
	}

	public UndoSet[] GetSets()
	{
		return null;
	}
}
