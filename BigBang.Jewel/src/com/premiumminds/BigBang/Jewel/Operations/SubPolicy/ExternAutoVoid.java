package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;

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
		return Constants.OPID_SubPolicy_ExternAutoVoid;
	}

	public String ShortDesc()
	{
		return "Anulação Automática da Apólice Adesão";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A apólice adesão foi automaticamente anulada por falta de pagamento de um recibo.";
	}

	public UUID GetExternalProcess()
	{
		return midReceiptProc;
	}

	protected void Run(SQLServer pdb)
			throws JewelPetriException
	{
		SubPolicy lobjSubPolicy;

		try
		{
			lobjSubPolicy = (SubPolicy)GetProcess().GetData();
			lobjSubPolicy.setAt(7, Constants.StatusID_Voided);
			lobjSubPolicy.setAt(4, mdtEffectDate);
			lobjSubPolicy.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
