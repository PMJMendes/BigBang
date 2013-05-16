package com.premiumminds.BigBang.Jewel.Operations.Client;

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
		return Constants.OPID_Client_ExecMgrXFer;
	}

	public String getObjName()
	{
		return "cliente";
	}

	public String getArticle()
	{
		return "o";
	}

	public UUID getSafeObjType()
	{
		return Constants.ObjID_Client;
	}

	public UUID getSafeProcType()
	{
		return Constants.ProcID_Client;
	}
}
