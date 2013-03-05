package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public class ExternAllowSendPayment
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midSigRequestProc;

	public ExternAllowSendPayment(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_ExternAllowSendPayment;
	}

	public String ShortDesc()
	{
		return "Recepção do Recibo Assinado";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O cliente enviou o recibo assinado.";
	}

	public UUID GetExternalProcess()
	{
		return midSigRequestProc;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Receipt lobjReceipt;

		lobjReceipt = (Receipt)GetProcess().GetData();

		try
		{
			lobjReceipt.setAt(Receipt.I.STATUS, Constants.StatusID_Payable);
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
