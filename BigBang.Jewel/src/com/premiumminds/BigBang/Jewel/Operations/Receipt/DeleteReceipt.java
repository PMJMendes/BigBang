package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteReceipt;

public class DeleteReceipt
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midReceipt;

	public DeleteReceipt(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_DeleteReceipt;
	}

	public String ShortDesc()
	{
		return "Eliminação de Recibo";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O recibo foi eliminado. A sua reposição foi/é possível a partir da apólice.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		ExternDeleteReceipt lobjOp;

		try
		{
			lobjOp = new ExternDeleteReceipt(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
			lobjOp.mobjData = new ReceiptData();
			lobjOp.mobjData.mid = midReceipt;
			TriggerOp(lobjOp);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}
}
