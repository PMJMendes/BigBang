package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoDeleteRiskAnalysis
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoDeleteRiskAnalysis(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Policy_UndoDeleteRiskAnalysis;
	}

	public String ShortDesc()
	{
		return "Desfazer Eliminação de Análise de Risco";
	}
}
