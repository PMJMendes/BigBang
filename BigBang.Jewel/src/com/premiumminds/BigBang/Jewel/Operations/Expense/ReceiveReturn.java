package com.premiumminds.BigBang.Jewel.Operations.Expense;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Expense;

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
		Expense lobjExpense;

		midExpense = GetProcess().GetDataKey();

		try
		{
			lobjExpense = Expense.GetInstance(Engine.getCurrentNameSpace(), midExpense);
			lobjExpense.setAt(Expense.I.REJECTION, mstrReason);
			lobjExpense.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
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
		Expense lobjExpense;

		try
		{
			lobjExpense = Expense.GetInstance(Engine.getCurrentNameSpace(), midExpense);
			lobjExpense.setAt(Expense.I.REJECTION, null);
			lobjExpense.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
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
