package com.premiumminds.BigBang.Jewel.Operations.QuoteRequest;

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
		return Constants.OPID_QuoteRequest_ExecMgrXFer;
	}

	public String getObjName()
	{
		return "consutla de mercado";
	}

	public String getArticle()
	{
		return "a";
	}

	public UUID getSafeObjType()
	{
		return Constants.ObjID_QuoteRequest;
	}

	public UUID getSafeProcType()
	{
		return Constants.ProcID_QuoteRequest;
	}
}
