package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.GeneralSystem;
import com.premiumminds.BigBang.Jewel.Operations.General.ExternDeleteClient;

public class DeleteClient
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public transient UUID midClient;
	public transient String mstrReason;

	public DeleteClient(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_DeleteClient;
	}

	public String ShortDesc()
	{
		return "Eliminação de Cliente";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "O cliente foi eliminado. A sua reposição foi/é possível a partir do sistema geral.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		ExternDeleteClient lopDC;

		try
		{
			lopDC = new ExternDeleteClient(GeneralSystem.GetAnyInstance(Engine.getCurrentNameSpace()).GetProcessID());
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lopDC.midClient = midClient;
		lopDC.mstrReason = mstrReason;
		TriggerOp(lopDC);
	}
}
