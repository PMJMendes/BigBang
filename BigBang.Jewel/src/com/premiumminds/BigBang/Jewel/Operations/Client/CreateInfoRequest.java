package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class CreateInfoRequest
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public String mstrRequestSubject;
	public String mstrRequestBody;
	public String[] marrReplyTos;
	public String[] marrTos;
	public String[] marrCCs;
	public String[] marrBCCs;
	private UUID midExternProcess;

	public CreateInfoRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Client_CreateInfoRequest;
	}

	public String ShortDesc()
	{
		return "Criação de Sub-Processo: Pedido de Informação ou Documento";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("Foi enviado o seguinte pedido:").append(pstrLineBreak);
		lstrBuffer.append(mstrRequestSubject).append(pstrLineBreak);
		lstrBuffer.append(mstrRequestBody).append(pstrLineBreak);

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return midExternProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		try
		{
			MailConnector.DoSendMail(marrReplyTos, marrTos, marrCCs, marrBCCs, mstrRequestSubject, mstrRequestBody);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		throw new JewelPetriException("Incomplete implementation.");
	}
}
