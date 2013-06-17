package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ReceiptData;

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
		return Constants.OPID_Receipt_DeleteReceipt;
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
		UUID lidScript;
		com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteReceipt lobjPOp;
		com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternDeleteReceipt lobjSPOp;
		com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ExternDeleteReceipt lobjSCOp;
		Operation lobjOp;

		lidScript = GetProcess().GetParent().GetScriptID();

		lobjOp = null;
		if ( Constants.ProcID_Policy.equals(lidScript) )
		{
			lobjPOp = new com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteReceipt(GetProcess().GetParent().getKey());
			lobjPOp.mobjData = new ReceiptData();
			lobjPOp.mobjData.mid = midReceipt;
			lobjOp = lobjPOp;
		}
		if ( Constants.ProcID_SubPolicy.equals(lidScript) )
		{
			lobjSPOp = new com.premiumminds.BigBang.Jewel.Operations.SubPolicy.ExternDeleteReceipt(GetProcess().GetParent().getKey());
			lobjSPOp.mobjData = new ReceiptData();
			lobjSPOp.mobjData.mid = midReceipt;
			lobjOp = lobjSPOp;
		}
		if ( Constants.ProcID_SubCasualty.equals(lidScript) )
		{
			lobjSCOp = new com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ExternDeleteReceipt(GetProcess().GetParent().getKey());
			lobjSCOp.mobjData = new ReceiptData();
			lobjSCOp.mobjData.mid = midReceipt;
			lobjOp = lobjSCOp;
		}
		if ( lobjOp == null )
			throw new JewelPetriException("Unexpected: Invalid parent process for recipt.");

		TriggerOp(lobjOp, pdb);
	}
}
