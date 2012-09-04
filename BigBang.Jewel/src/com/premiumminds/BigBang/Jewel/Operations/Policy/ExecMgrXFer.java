package com.premiumminds.BigBang.Jewel.Operations.Policy;

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
		return Constants.OPID_Policy_ExecMgrXFer;
	}

	public String getObjName()
	{
		return "apólice";
	}

	public String getArticle()
	{
		return "a";
	}

	public UUID getSafeObjType()
	{
		return Constants.ObjID_Policy;
	}

	public UUID getSafeProcType()
	{
		return Constants.ProcID_Policy;
	}
}
