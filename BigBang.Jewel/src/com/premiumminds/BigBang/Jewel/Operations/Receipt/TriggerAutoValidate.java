package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class TriggerAutoValidate
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	private UUID midReceipt;

	public TriggerAutoValidate(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_TriggerAutoValidate;
	}

	public String ShortDesc()
	{
		return "Validação Automática";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O recibo foi validado automaticamente pelo sistema.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb) throws JewelPetriException
	{
		midReceipt = GetProcess().GetDataKey();
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A validação será retirada.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A validação foi retirada.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjSet;

		lobjSet = new UndoSet();
		lobjSet.midType = Constants.ObjID_Receipt;
		lobjSet.marrChanged = new UUID[]{midReceipt};

		return new UndoSet[]{lobjSet};
	}
}
