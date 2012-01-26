package com.premiumminds.BigBang.Jewel.Operations.Client;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.SysObjects.SendMail;

public class CreateInfoRequest
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public String mstrRequestSubject;
	public String mstrRequestBody;
	public UUID[] marrUserDecos;
	public UUID[] marrTos;
	public String[] marrCCs;
	public String[] marrBCCs;
	private UUID midExternProcess;

	public CreateInfoRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_CreateClientInfoRequest;
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
		String[] larrReplyTo;
		String[] larrTo;
		int i;

		try
		{
			if ( marrUserDecos == null )
				larrReplyTo = null;
			else
			{
				larrReplyTo = new String[marrUserDecos.length];
				for ( i = 0; i < marrUserDecos.length; i++ )
					larrReplyTo[i] = (String)UserDecoration.GetInstance(Engine.getCurrentNameSpace(), marrUserDecos[i]).getAt(1);
			}

			if ( marrTos == null )
				larrTo = null;
			else
			{
				larrTo = new String[marrTos.length];
				for ( i = 0; i < marrTos.length; i++ )
					larrTo[i] = (String)ContactInfo.GetInstance(Engine.getCurrentNameSpace(), marrTos[i]).getAt(2);
			}

			SendMail.DoSendMail(larrReplyTo, larrTo, marrCCs, marrBCCs, mstrRequestSubject, mstrRequestBody);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		throw new JewelPetriException("Incomplete implementation.");
	}
}
