package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;

public class CancelPaymentNotice
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public CancelPaymentNotice(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_CancelPaymentNotice;
	}

	public String ShortDesc()
	{
		return "Cancelamento do Aviso de Cobrança";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O aviso de cobrança foi cancelado.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Receipt lobjReceipt;

		lobjReceipt = (Receipt)GetProcess().GetData();
		try
		{
			lobjReceipt.setAt(Receipt.I.STATUS, Constants.StatusID_Initial);
			lobjReceipt.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
