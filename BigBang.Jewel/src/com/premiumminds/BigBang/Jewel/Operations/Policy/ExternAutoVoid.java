package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Policy;

public class ExternAutoVoid
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public Timestamp mdtEffectDate;
	public UUID midReceiptProc;

	public ExternAutoVoid(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_ExternAutoVoid;
	}

	public String ShortDesc()
	{
		return "Anulação Automática da Apólice";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A apólice foi automaticamente anulada por falta de pagamento de um recibo.";
	}

	public UUID GetExternalProcess()
	{
		return midReceiptProc;
	}

	protected void Run(SQLServer pdb)
			throws JewelPetriException
	{
		Policy lobjPolicy;

		try
		{
			lobjPolicy = (Policy)GetProcess().GetData();
			lobjPolicy.setAt(13, Constants.StatusID_AutoVoided);
			lobjPolicy.setAt(9, mdtEffectDate);
			lobjPolicy.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
