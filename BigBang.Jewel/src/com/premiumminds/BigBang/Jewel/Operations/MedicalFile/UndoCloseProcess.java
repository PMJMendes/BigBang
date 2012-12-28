package com.premiumminds.BigBang.Jewel.Operations.MedicalFile;

import java.util.UUID;

import Jewel.Petri.SysObjects.UndoOperation;

import com.premiumminds.BigBang.Jewel.Constants;

public class UndoCloseProcess
	extends UndoOperation
{
	private static final long serialVersionUID = 1L;

	public UndoCloseProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_MedicalFile_UndoCloseProcess;
	}

	public String ShortDesc()
	{
		return "Desfazer Fecho do Processo";
	}
}
