package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Operations.SubCasualty.ExternReopenProcess;

public class ReopenSubCasualty
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public UUID midSubCasualty;
	public UUID midMotive;
	private UUID midProcess;
	private String mstrSubCasualty;
	private String mstrMotive;

	public ReopenSubCasualty(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_ReopenSubCasualty;
	}

	public String ShortDesc()
	{
		return null;
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O sub-sinistro n. " + mstrSubCasualty + " foi reaberto pelo seguinte motivo:" + pstrLineBreak + mstrMotive;
	}

	public UUID GetExternalProcess()
	{
		return midProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		SubCasualty lobjSubCasualty;
		ExternReopenProcess lopERP;

		try
		{
			lobjSubCasualty = SubCasualty.GetInstance(Engine.getCurrentNameSpace(), midSubCasualty);
			mstrMotive = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_CasualtyReopenMotives),
					midMotive).getLabel();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		PNProcess.GetInstance(Engine.getCurrentNameSpace(), lobjSubCasualty.GetProcessID()).Restart(pdb);

		lopERP = new ExternReopenProcess(lobjSubCasualty.GetProcessID());
		lopERP.mstrMotive = mstrMotive;

		TriggerOp(lopERP, pdb);
	}
}
