package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

public class DeleteNegotiation
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID midNegotiation;
	public transient String mstrReason;
	private boolean mbFromPolicy;

	public DeleteNegotiation(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_DeleteNegotiation;
	}

	public String ShortDesc()
	{
		return "Eliminação da Negociação";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A negociação foi eliminada. A sua reposição foi/é possível a partir d" +
				(mbFromPolicy ? "a apólice negociada" : "o pedido de cotação a que pertence") + ".";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteNegotiation lopPDN;
//		com.premiumminds.BigBang.Jewel.Operations.QuoteRequest.ExternDeleteNegotiation lopQRDN;

		if ( Constants.ProcID_Policy.equals(GetProcess().GetParent().GetScriptID()) )
		{
			mbFromPolicy = true;
			lopPDN = new com.premiumminds.BigBang.Jewel.Operations.Policy.ExternDeleteNegotiation(GetProcess().GetParent().getKey());
			lopPDN.midNegotiation = midNegotiation;
			lopPDN.mstrReason = mstrReason;
			TriggerOp(lopPDN, pdb);
		}
		else
		{
			mbFromPolicy = false;
		}
	}
}
