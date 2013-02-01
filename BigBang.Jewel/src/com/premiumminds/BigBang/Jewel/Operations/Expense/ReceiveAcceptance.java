package com.premiumminds.BigBang.Jewel.Operations.Expense;

import java.math.BigDecimal;
import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Expense;

public class ReceiveAcceptance
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public BigDecimal mdblSettlement;
	private UUID midExpense;
	private BigDecimal mdblPrevSettlement;
	private boolean mbPrevManual;

	public ReceiveAcceptance(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Expense_ReceiveAcceptance;
	}

	public String ShortDesc()
	{
		return "Recepção de Aceitação da Seguradora";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A seguradora aceitou o pedido de reembolso desta despesa, no valor de " + mdblSettlement.toPlainString() + ".";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Expense lobjExpense;

		lobjExpense = (Expense)GetProcess().GetData();

		midExpense = lobjExpense.getKey();
		mdblPrevSettlement = (BigDecimal)lobjExpense.getAt(Expense.I.SETTLEMENT);
		mbPrevManual = (Boolean)lobjExpense.getAt(Expense.I.MANUAL);

		try
		{
			lobjExpense.setAt(Expense.I.SETTLEMENT, mdblSettlement);
			lobjExpense.setAt(Expense.I.MANUAL, true);
			lobjExpense.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		return "A aceitação será retirada. A despesa ficará novamente pendente de indicação da seguradora.";
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		return "A aceitação foi retirada. A despesa ficou novamente pendente de indicação da seguradora.";
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		Expense lobjExpense;

		lobjExpense = (Expense)GetProcess().GetData();
		try
		{
			lobjExpense.setAt(Expense.I.SETTLEMENT, mdblPrevSettlement);
			lobjExpense.setAt(Expense.I.MANUAL, mbPrevManual);
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
