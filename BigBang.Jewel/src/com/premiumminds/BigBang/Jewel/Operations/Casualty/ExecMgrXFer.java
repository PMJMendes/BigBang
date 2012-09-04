package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.ExecMgrXFerBase;

public class ExecMgrXFer
	extends ExecMgrXFerBase
{
	private static final long serialVersionUID = 1L;

	public ExecMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_ExecMgrXFer;
	}

	public String getObjName()
	{
		return "sinistro";
	}

	public String getArticle()
	{
		return "o";
	}

	public UUID getSafeObjType()
	{
		return Constants.ObjID_Casualty;
	}

	public UUID getSafeProcType()
	{
		return Constants.ProcID_Casualty;
	}
}
