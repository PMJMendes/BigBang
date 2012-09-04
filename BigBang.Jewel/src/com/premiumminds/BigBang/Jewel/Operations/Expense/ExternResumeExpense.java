package com.premiumminds.BigBang.Jewel.Operations.Expense;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExternResumeExpense
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ExternResumeExpense(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Expense_ExternResumeExpense;
	}

	public String ShortDesc()
	{
		return "Reposição da Despesa de Saúde";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Após ter sido eliminada, esta despesa foi reposta.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
	}
}
