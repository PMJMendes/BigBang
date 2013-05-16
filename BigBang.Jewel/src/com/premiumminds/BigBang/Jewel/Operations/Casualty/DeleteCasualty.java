package com.premiumminds.BigBang.Jewel.Operations.Casualty;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Operations.Client.ExternDeleteCasualty;

public class DeleteCasualty
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID midCasualty;
	public transient String mstrReason;

	public DeleteCasualty(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Casualty_DeleteCasualty;
	}

	public String ShortDesc()
	{
		return "Eliminação de Sinistro";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O sinistro foi eliminado. A sua reposição foi/é possível a partir do processo do cliente.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		ExternDeleteCasualty lopDC;

		try
		{
			lopDC = new ExternDeleteCasualty(GetProcess().GetParent().getKey());
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lopDC.midCasualty = midCasualty;
		lopDC.mstrReason = mstrReason;
		TriggerOp(lopDC, pdb);
	}
}
