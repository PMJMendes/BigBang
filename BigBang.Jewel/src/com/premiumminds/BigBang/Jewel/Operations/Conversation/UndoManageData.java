package com.premiumminds.BigBang.Jewel.Operations.Conversation;

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
		return Constants.OPID_Conversation_UndoManageData;
	}

	public String ShortDesc()
	{
		return "Desfazer Fecho de Processo";
	}
}
