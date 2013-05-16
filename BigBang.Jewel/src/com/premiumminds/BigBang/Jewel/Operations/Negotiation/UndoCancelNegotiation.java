package com.premiumminds.BigBang.Jewel.Operations.Negotiation;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoCancelNegotiation
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoCancelNegotiation(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Negotiation_UndoCancelNegotiation;
	}

	public String ShortDesc()
	{
		return "Desfazer Cancelamento da Negociação";
	}
}
