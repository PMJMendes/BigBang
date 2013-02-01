package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoReceiveQuote
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoReceiveQuote(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_UndoReceiveQuote;
	}

	public String ShortDesc()
	{
		return "Desfazer Recepção de Cotação";
	}
}
