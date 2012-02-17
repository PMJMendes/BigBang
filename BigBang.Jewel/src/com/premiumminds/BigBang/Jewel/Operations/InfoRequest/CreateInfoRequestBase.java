package com.premiumminds.BigBang.Jewel.Operations.InfoRequest;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.InfoRequest;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public abstract class CreateInfoRequestBase
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public int mlngDays;
	public UUID midRequestType;
	public String mstrSubject;
	public String mstrBody;
	public UUID[] marrUsers;
	public UUID[] marrContactInfos;
	public String[] marrCCs;
	public String[] marrBCCs;
	public UUID midRequestObject;
	private UUID midExternProcess;

	public CreateInfoRequestBase(UUID pidProcess)
	{
		super(pidProcess);
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
		lstrBuffer.append(mstrSubject).append(pstrLineBreak);
		lstrBuffer.append(mstrBody).append(pstrLineBreak).append(pstrLineBreak);
		lstrBuffer.append("Prazo limite de resposta: ").append(mlngDays).append(" dias.").append(pstrLineBreak);

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return midExternProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Timestamp ldtNow;
		Calendar ldtAux;
		Timestamp ldtLimit;
		IEntity lrefDecos;
		String[] larrReplyTos;
		String[] larrTos;
		int i;
        ResultSet lrs;
        InfoRequest lobjRequest;
		IScript lobjScript;
		IProcess lobjProc;
		AgendaItem lobjItem;

		ldtNow = new Timestamp(new java.util.Date().getTime());
    	ldtAux = Calendar.getInstance();
    	ldtAux.setTimeInMillis(ldtNow.getTime());
    	ldtAux.add(Calendar.DAY_OF_MONTH, mlngDays);
    	ldtLimit = new Timestamp(ldtAux.getTimeInMillis());

        lrs = null;
		try
		{
			lrefDecos = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
			larrReplyTos = new String[marrUsers.length];
			for ( i = 0; i < marrUsers.length; i++ )
			{
				lrs = lrefDecos.SelectByMembers(pdb, new int[] {0}, new java.lang.Object[] {marrUsers[i]}, new int[0]);
			    if (lrs.next())
			    	larrReplyTos[i] = (String)UserDecoration.GetInstance(Engine.getCurrentNameSpace(), lrs).getAt(1);
			    else
			    	larrReplyTos[i] = null;
			    lrs.close();
			}

			larrTos = new String[marrContactInfos.length];
			for ( i = 0; i < marrContactInfos.length; i++ )
			{
				larrTos[i] = (String)ContactInfo.GetInstance(Engine.getCurrentNameSpace(), marrContactInfos[i]).getAt(2); 
			}
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			lobjRequest = InfoRequest.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			lobjRequest.setAt(1, midRequestType);
			lobjRequest.setText(mstrBody);
			lobjRequest.setAt(3, larrReplyTos[0]);
			lobjRequest.setAt(4, mstrSubject);
			lobjRequest.setAt(5, ldtLimit);
			lobjRequest.SaveToDb(pdb);
			lobjRequest.InitNew(marrUsers, marrContactInfos, marrCCs, marrBCCs, pdb);

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_InfoRequest);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjRequest.getKey(), GetProcess().getKey(),
					GetContext(), pdb);

			for ( i = 0; i < marrUsers.length; i++ )
			{
				lobjItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjItem.setAt(0, "Pedido de Informação ou Documento");
				lobjItem.setAt(1, marrUsers[i]);
				lobjItem.setAt(2, Constants.ProcID_InfoRequest);
				lobjItem.setAt(3, ldtNow);
				lobjItem.setAt(4, ldtLimit);
				lobjItem.setAt(5, Constants.UrgID_Valid);
				lobjItem.SaveToDb(pdb);
				lobjItem.InitNew(new UUID[] {lobjProc.getKey()}, new UUID[] {Constants.OPID_InfoReq_ReceiveReply,
						Constants.OPID_InfoReq_RepeatRequest, Constants.OPID_InfoReq_CancelRequest}, pdb);
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			MailConnector.DoSendMail(larrReplyTos, larrTos, marrCCs, marrBCCs, mstrSubject, mstrBody);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		midRequestObject = lobjRequest.getKey();
		midExternProcess = lobjProc.getKey();
	}
}
