package com.premiumminds.BigBang.Jewel.Operations.MedicalFile;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoCreateConversation
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoCreateConversation(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_MedicalFile_UndoCreateConversation;
	}

	public String ShortDesc()
	{
		return "Desfazer Criação de Troca de Mensagens";
	}
}
