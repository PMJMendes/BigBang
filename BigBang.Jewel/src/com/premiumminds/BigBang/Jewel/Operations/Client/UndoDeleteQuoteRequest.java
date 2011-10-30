package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoDeleteQuoteRequest
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoDeleteQuoteRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_UndoDeleteQuoteRequest;
	}

	public String ShortDesc()
	{
		return "Desfazer Eliminação de Consulta de Mercado";
	}
}
