package com.premiumminds.BigBang.Jewel.Operations.SubPolicy;

import java.sql.Timestamp;
import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;

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
		return Constants.OPID_SubPolicy_ExternUndoAutoVoid;
	}

	public String ShortDesc()
	{
		return "Desfazer Anulação Automática da Apólice Adesão";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A apólice adesão foi reposta em vigor e o recibo foi novamente posto a pagamento.";
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

			lobjSubPolicy.setAt(7, midPrevStatus);
			lobjSubPolicy.setAt(4, mdtPrevEndDate);
			lobjSubPolicy.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
