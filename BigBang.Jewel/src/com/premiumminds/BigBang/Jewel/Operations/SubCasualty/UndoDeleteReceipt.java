package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoDeleteReceipt
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoDeleteReceipt(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_UndoDeleteReceipt;
	}

	public String ShortDesc()
	{
		return "Desfazer Eliminação de Recibo";
	}
}
