package com.premiumminds.BigBang.Jewel.Operations.QuoteRequest;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoCloseProcess
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoCloseProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_QuoteRequest_UndoCloseProcess;
	}

	public String ShortDesc()
	{
		return "Reabertura do Processo de Consulta";
	}
}
