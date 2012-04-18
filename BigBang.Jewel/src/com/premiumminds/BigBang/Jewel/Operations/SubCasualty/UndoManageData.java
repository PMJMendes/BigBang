package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoManageData
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoManageData(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_UndoManageData;
	}

	public String ShortDesc()
	{
		return "Desfazer Aletração de Dados";
	}
}
