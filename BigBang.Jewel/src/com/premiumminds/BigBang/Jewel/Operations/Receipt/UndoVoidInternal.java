package com.premiumminds.BigBang.Jewel.Operations.Receipt;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoVoidInternal
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoVoidInternal(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Receipt_VoidInternal;
	}

	public String ShortDesc()
	{
		return "Desfazer Anulação de Nota de Débito";
	}
}
