package com.premiumminds.BigBang.Jewel.Operations.QuoteRequest;

import java.util.UUID;

import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.MgrXFer.CreateMgrXfrBase;

public class CreateMgrXFer
	extends CreateMgrXfrBase
{
	private static final long serialVersionUID = 1L;

	public CreateMgrXFer(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_QuoteRequest_CreateMgrXFer;
	}

	public String getObjName()
	{
		return "consutla de mercado";
	}

	public String getArticle()
	{
		return "a";
	}

	public Operation getTriggeredOp(UUID pidProcess)
	{
		return new TriggerAllowUndoMgrXFer(pidProcess);
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
