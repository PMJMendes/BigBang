package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.Casualty.ExternDeleteSubCasualty;

public class DeleteSubCasualty
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID midSubCasualty;
	public transient String mstrReason;

	public DeleteSubCasualty(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_DeleteSubCasualty;
	}

	public String ShortDesc()
	{
		return "Eliminação de Sub-Sinistro";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O sub-sinistro foi eliminado. A sua reposição foi/é possível a partir do processo do sinistro.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		ExternDeleteSubCasualty lopDC;

		try
		{
			lopDC = new ExternDeleteSubCasualty(GetProcess().GetParent().getKey());
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lopDC.midSubCasualty = midSubCasualty;
		lopDC.mstrReason = mstrReason;
		TriggerOp(lopDC, pdb);
	}
}
