package com.premiumminds.BigBang.Jewel.Operations.Policy;

import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class CreateInfoRequest
	extends Operation
{
	private static final long serialVersionUID = 1L;

	private String mstrRequestHeader;
	private String mstrRequestBody;
	private UUID[] marrUserDecos;
	private UUID[] marrTos;
	private UUID[] marrCCs;
	private UUID[] marrBCCs;
	private UUID midExternProcess;

	public CreateInfoRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_CreatePolicyInfoRequest;
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
		lstrBuffer.append(mstrRequestHeader).append(pstrLineBreak);
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
		String[] larrCC;
		String[] larrBCC;
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
					larrTo[i] = EmailFromContactInfo(marrTos[i]);
			}

			if ( marrCCs == null )
				larrCC = null;
			else
			{
				larrCC = new String[marrCCs.length];
				for ( i = 0; i < marrCCs.length; i++ )
					larrCC[i] = EmailFromContactInfo(marrCCs[i]);
			}

			if ( marrBCCs == null )
				larrBCC = null;
			else
			{
				larrBCC = new String[marrBCCs.length];
				for ( i = 0; i < marrBCCs.length; i++ )
					larrBCC[i] = EmailFromContactInfo(marrBCCs[i]);
			}

			MailConnector.DoSendMail(larrReplyTo, larrTo, larrCC, larrBCC, mstrRequestHeader, mstrRequestBody);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}
	}

	private String EmailFromContactInfo(UUID pidInfo)
		throws BigBangJewelException
	{
		return (String)ContactInfo.GetInstance(Engine.getCurrentNameSpace(), pidInfo).getAt(2);
	}
}
