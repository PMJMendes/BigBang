package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class ExternUndoCancelDAS
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midDASRequestProcess;

	public ExternUndoCancelDAS(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_ExternUndoCancelDAS;
	}

	public String ShortDesc()
	{
		return "Desfazer Cancelamento de Declaração de Ausência de Sinistros";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A cancelamento da declaração foi retirado. O processo do recibo ficou novamente pendente da recepção de uma DAS.";
	}

	public UUID GetExternalProcess()
	{
		return midDASRequestProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Receipt lobjReceipt;

		lobjReceipt = (Receipt)GetProcess().GetData();

		try
		{
			lobjReceipt.setAt(Receipt.I.STATUS, Constants.StatusID_DASPending);
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
