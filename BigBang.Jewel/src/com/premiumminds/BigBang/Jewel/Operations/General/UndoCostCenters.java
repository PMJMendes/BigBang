package com.premiumminds.BigBang.Jewel.Operations.General;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoCostCenters
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoCostCenters(UUID pidProcess)
	{
		super(pidProcess);
	}

	public String ShortDesc()
	{
		return "Desfazer Gestão de Centros de Custo";
	}

	protected UUID OpID()
	{
		return Constants.OPID_General_UndoCostCenters;
	}
}
