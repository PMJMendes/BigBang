package com.premiumminds.BigBang.Jewel.Operations.Expense;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;

public class DeleteExpense
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID midExpense;
	public transient String mstrReason;
	private boolean mbIsPolicy;

	public DeleteExpense(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Expense_DeleteExpense;
	}

	public String ShortDesc()
	{
		return "Eliminação de Despesa de Saúde";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A despesa foi eliminada. A sua reposição foi/é possível a partir do processo da apólice" +
				( mbIsPolicy ? "" : " adesão" ) + ".";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteExpense lopPEDE;
		com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternDeleteExpense lopSPEDE;
		Operation lop;

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
		{
			mbIsPolicy = true;
			try
			{
				lopPEDE = new com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteExpense(GetProcess().GetParent().getKey());
				lopPEDE.midExpense = midExpense;
				lopPEDE.mstrReason = mstrReason;
				lop = lopPEDE;
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
		else
		{
			mbIsPolicy = false;
			try
			{
				lopSPEDE = new com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternDeleteExpense(GetProcess().GetParent().getKey());
				lopSPEDE.midExpense = midExpense;
				lopSPEDE.mstrReason = mstrReason;
				lop = lopSPEDE;
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}

		TriggerOp(lop, pdb);
	}
}
