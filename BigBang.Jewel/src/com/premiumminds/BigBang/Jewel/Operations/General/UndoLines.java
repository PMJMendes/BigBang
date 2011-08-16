package com.premiumminds.BigBang.Jewel.Operations.General;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoLines
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoLines(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_UndoLines;
	}

	public String ShortDesc()
	{
		return "Desfazer Gestão de Ramos, Modalidades e Coberturas";
	}
}
