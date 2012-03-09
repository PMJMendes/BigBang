package com.premiumminds.BigBang.Jewel.Operations.QuoteRequest;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;

public class ExternResumeQuoteRequest
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public ExternResumeQuoteRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_QuoteRequest_ExternResumeQuoteRequest;
	}

	public String ShortDesc()
	{
		return "Reposição da Consulta de Mercado";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "Após ter sido eliminada, esta consulta foi reposta.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
	}
}
