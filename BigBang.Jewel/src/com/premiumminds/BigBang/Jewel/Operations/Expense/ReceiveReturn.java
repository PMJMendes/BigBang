package com.premiumminds.BigBang.Jewel.Operations.Expense;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ReceiveReturn
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public String mstrReason;
	private UUID midExpense;

	public ReceiveReturn(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Expense_ReceiveReturn;
	}

	public String ShortDesc()
	{
		return "Recepção de Devolução da Despesa";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A despesa foi devolvida sem reembolso, pelo seguinte motivo: " + mstrReason;
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		midExpense = GetProcess().GetDataKey();
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A devolução será retirada. A despesa ficará novamente pendente de indicação da seguradora.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A devolução foi retirada. A despesa ficou novamente pendente de indicação da seguradora.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
	}

	public UndoSet[] GetSets()
	{
		UndoSet lobjSet;

		lobjSet = new UndoSet();
		lobjSet.midType = Constants.ObjID_Expense;
		lobjSet.marrChanged = new UUID[] {midExpense};

		return new UndoSet[] {lobjSet};
	}
}
