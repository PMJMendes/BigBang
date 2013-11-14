package com.premiumminds.BigBang.Jewel.Operations.Conversation;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.SilentOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class ExternAbortProcess
	extends SilentOperation
{
	private static final long serialVersionUID = 1L;

	public String mstrReviveEmailID;

	public ExternAbortProcess(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Conversation_ExternAbortProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		IProcess lobjProcess;

		lobjProcess = GetProcess();
		lobjProcess.Stop(pdb);
		lobjProcess.SetDataObjectID(null, pdb);

		if ( mstrReviveEmailID != null )
		{
			try
			{
				MailConnector.DoUnprocessItem(mstrReviveEmailID);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}
}
