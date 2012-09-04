package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;

import Jewel.Petri.SysObjects.UndoOperation;

public class UndoMergeOtherHere
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoMergeOtherHere(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Client_UndoMergeOtherHere;
	}

	public String ShortDesc()
	{
		return "Desfazer Fusão de Clientes";
	}
}
