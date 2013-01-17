package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoSetInternational
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoSetInternational(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Client_UndoSetInternational;
	}

	public String ShortDesc()
	{
		return "Desfazer Indicação de Internacional";
	}
}
