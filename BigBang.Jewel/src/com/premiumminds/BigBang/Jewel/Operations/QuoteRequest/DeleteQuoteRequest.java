package com.premiumminds.BigBang.Jewel.Operations.QuoteRequest;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.Client.ExternDeleteQuoteRequest;

public class DeleteQuoteRequest
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID midRequest;
	public transient String mstrReason;

	public DeleteQuoteRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_QuoteRequest_DeleteQuoteRequest;
	}

	public String ShortDesc()
	{
		return "Eliminação de Consulta de Mercado";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A consulta de mercado foi eliminada. A sua reposição foi/é possível a partir do cliente.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		ExternDeleteQuoteRequest lopDC;

		try
		{
			lopDC = new ExternDeleteQuoteRequest(GetProcess().GetParent().getKey());
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lopDC.midRequest = midRequest;
		lopDC.mstrReason = mstrReason;
		TriggerOp(lopDC, pdb);
	}
}
