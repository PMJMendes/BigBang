package com.premiumminds.BigBang.Jewel.Operations.Policy;

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
		return Constants.OPID_Policy_CreateMgrXFer;
	}

	public String getObjName()
	{
		return "apólice";
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
		return Constants.ObjID_Policy;
	}

	public UUID getSafeProcType()
	{
		return Constants.ProcID_Policy;
	}
}
