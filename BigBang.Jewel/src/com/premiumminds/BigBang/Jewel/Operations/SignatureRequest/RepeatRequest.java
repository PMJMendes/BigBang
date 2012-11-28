package com.premiumminds.BigBang.Jewel.Operations.SignatureRequest;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.Operation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.SignatureRequest;

public class RepeatRequest
	extends Operation
{
	private static final long serialVersionUID = 1L;

//	public OutgoingMessageData mobjMessage;
	public int mlngDays;

	public RepeatRequest(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_SigReq_RepeatRequest;
	}

	public String ShortDesc()
	{
		return "Repetição do Pedido";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		lstrBuffer.append("Foi feita uma insistência").append(pstrLineBreak);
//		lstrBuffer.append("O pedido foi re-enviado com o seguinte texto:").append(pstrLineBreak);
//		lstrBuffer.append(mobjMessage.mstrSubject).append(pstrLineBreak);
//		lstrBuffer.append(mobjMessage.mstrBody).append(pstrLineBreak).append(pstrLineBreak);
		lstrBuffer.append("Novo prazo limite de resposta: ").append(mlngDays).append(" dias.").append(pstrLineBreak);

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Timestamp ldtNow;
		Calendar ldtAux;
		Timestamp ldtLimit;
		SignatureRequest lobjRequest;
//		RequestAddress[] larrAddresses;
//		ArrayList<String> larrTos;
//		ArrayList<String> larrCCs;
//		ArrayList<String> larrBCCs;
//		ArrayList<String> larrReplyTos;
//		ArrayList<UUID> larrUsers;
//		int i;
		HashMap<UUID, AgendaItem> larrItems;
		IEntity lrefAux;
		ResultSet lrs;
		ObjectBase lobjAgendaProc;
		AgendaItem lobjNewItem;

		ldtNow = new Timestamp(new java.util.Date().getTime());
    	ldtAux = Calendar.getInstance();
    	ldtAux.setTimeInMillis(ldtNow.getTime());
    	ldtAux.add(Calendar.DAY_OF_MONTH, mlngDays);
    	ldtLimit = new Timestamp(ldtAux.getTimeInMillis());

		try
		{
			lobjRequest = (SignatureRequest)GetProcess().GetData();
//			larrAddresses = lobjRequest.GetAddresses(pdb);
//			larrReplyTos = new ArrayList<String>();
//			larrTos = new ArrayList<String>();
//			larrCCs = new ArrayList<String>();
//			larrBCCs = new ArrayList<String>();
//			larrUsers = new ArrayList<UUID>();
//			for ( i = 0; i < larrAddresses.length; i++ )
//			{
//				if ( Constants.UsageID_To.equals(larrAddresses[i].getAt(2)) )
//					larrTos.add(larrAddresses[i].getLabel());
//				if ( Constants.UsageID_CC.equals(larrAddresses[i].getAt(2)) )
//					larrCCs.add(larrAddresses[i].getLabel());
//				if ( Constants.UsageID_BCC.equals(larrAddresses[i].getAt(2)) )
//					larrCCs.add(larrAddresses[i].getLabel());
//				if ( Constants.UsageID_ReplyTo.equals(larrAddresses[i].getAt(2)) )
//				{
//					larrReplyTos.add(larrAddresses[i].getLabel());
//					larrUsers.add((UUID)larrAddresses[i].getAt(3));
//				}
//			}

			lobjRequest.setAt(1, ldtLimit);
			lobjRequest.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		larrItems = new HashMap<UUID, AgendaItem>();
		lrs = null;
		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
			lrs = lrefAux.SelectByMembers(pdb, new int[] {1}, new java.lang.Object[] {GetProcess().getKey()}, new int[0]);
			while ( lrs.next() )
			{
				lobjAgendaProc = Engine.GetWorkInstance(lrefAux.getKey(), lrs);
				larrItems.put((UUID)lobjAgendaProc.getAt(0),
						AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)lobjAgendaProc.getAt(0)));
			}
			lrs.close();
		}
		catch (Throwable e)
		{
			if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
			throw new JewelPetriException(e.getMessage(), e);
		}

		try
		{
			for ( AgendaItem lobjItem: larrItems.values() )
			{
				lobjItem.ClearData(pdb);
				lobjItem.getDefinition().Delete(pdb, lobjItem.getKey());
			}

//			for ( i = 0; i < larrUsers.size(); i++ )
//			{
				lobjNewItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjNewItem.setAt(0, "Pedido de Assinatura de Recibos");
				lobjNewItem.setAt(1, Engine.getCurrentUser()/*larrUsers.get(i)*/);
				lobjNewItem.setAt(2, Constants.ProcID_SignatureRequest);
				lobjNewItem.setAt(3, ldtNow);
				lobjNewItem.setAt(4, ldtLimit);
				lobjNewItem.setAt(5, Constants.UrgID_Valid);
				lobjNewItem.SaveToDb(pdb);
				lobjNewItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_SigReq_ReceiveReply,
						Constants.OPID_SigReq_RepeatRequest, Constants.OPID_SigReq_CancelRequest}, pdb);
//			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

//		try
//		{
//			MailConnector.DoSendMail(larrReplyTos.toArray(new String[larrReplyTos.size()]),
//					larrTos.toArray(new String[larrTos.size()]), larrCCs.toArray(new String[larrCCs.size()]),
//					larrBCCs.toArray(new String[larrBCCs.size()]), mobjMessage.mstrSubject, mobjMessage.mstrBody);
//		}
//		catch (Throwable e)
//		{
//			throw new JewelPetriException(e.getMessage(), e);
//		}
	}
}
