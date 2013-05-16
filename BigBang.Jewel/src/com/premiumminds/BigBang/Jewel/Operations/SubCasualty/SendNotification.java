package com.premiumminds.BigBang.Jewel.Operations.SubCasualty;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;

public class SendNotification
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public SendNotification(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SubCasualty_SendNotification;
	}

	public String ShortDesc()
	{
		return "Envio da Participação";
	}

	public String LongDesc(String pstrLineBreak)
	{
		return "A participação foi enviada à seguradora.";
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
	}
}
