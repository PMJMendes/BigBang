package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;

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

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		super.Run(pdb);

		for ( IProcess lobjProc : GetProcess().GetCurrentSubProcesses(pdb) )
		{
			if ( Constants.ProcID_SubCasualty.equals(lobjProc.GetScriptID()) )
				lobjProc.SetManagerID(midNewManager, pdb);
		}
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		if ( midNewManager.equals(midOldManager) )
			return;
		super.Undo(pdb);

		for ( IProcess lobjProc : GetProcess().GetCurrentSubProcesses(pdb) )
		{
			if ( Constants.ProcID_SubCasualty.equals(lobjProc.GetScriptID()) )
				lobjProc.SetManagerID(midOldManager, pdb);
		}
	}
}
