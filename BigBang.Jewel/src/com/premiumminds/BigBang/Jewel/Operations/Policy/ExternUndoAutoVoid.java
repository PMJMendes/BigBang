package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.sql.Timestamp;
import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Policy;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class ExternUndoAutoVoid
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public Timestamp mdtPrevEndDate;
	public UUID midPrevStatus;
	public UUID midReceiptProc;

	public ExternUndoAutoVoid(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_ExternUndoAutoVoid;
	}

	public String ShortDesc()
	{
		return "Desfazer Anulação Automática da Apólice";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A apólice foi reposta em vigor e o recibo foi novamente posto a pagamento.";
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

			lobjPolicy.setAt(13, midPrevStatus);
			lobjPolicy.setAt(9, mdtPrevEndDate);
			lobjPolicy.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
