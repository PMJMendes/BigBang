package com.premiumminds.BigBang.Jewel.Operations.Conversation;

import java.sql.ResultSet;
import java.sql.Timestamp;
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
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Conversation;
import com.premiumminds.BigBang.Jewel.Objects.Message;
import com.premiumminds.BigBang.Jewel.Objects.MessageAddress;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class SendMessage
	extends Operation
{
	private static final long serialVersionUID = 1L;

	public MessageData mobjData;
	public Timestamp mdtDueDate;

	public SendMessage(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Conversation_SendMessage;
	}

	public String ShortDesc()
	{
		return "Envio de Informação";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A informação enviada foi a seguinte:");

		mobjData.Describe(lstrResult, pstrLineBreak);

		if ( mobjData.mobjDocOps != null )
			mobjData.mobjDocOps.LongDesc(lstrResult, pstrLineBreak);
		if ( mobjData.mobjContactOps != null )
			mobjData.mobjContactOps.LongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public UUID GetExternalProcess()
	{
		return null;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		Timestamp ldtNow;
		HashMap<UUID, AgendaItem> larrItems;
		IEntity lrefAux;
		ResultSet lrs;
		ObjectBase lobjAgendaProc;
		Conversation lobjConv;
		UUID lidContainer;
		AgendaItem lobjNewAgendaItem;
		Message lobjMessage;
		MessageAddress lobjAddr;
		int i;

		ldtNow = new Timestamp(new java.util.Date().getTime());

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
			for ( AgendaItem lobjAgendaItem: larrItems.values() )
			{
				lobjAgendaItem.ClearData(pdb);
				lobjAgendaItem.getDefinition().Delete(pdb, lobjAgendaItem.getKey());
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lobjConv = (Conversation)GetProcess().GetData();

		try
		{
			lidContainer = lobjConv.getParentContainer();

			if ( mdtDueDate == null )
			{
				TriggerOp(new TriggerAutoCloseProcess(GetProcess().getKey()), pdb);
			}
			else
			{
				lobjConv.setAt(Conversation.I.PENDINGDIRECTION, Constants.MsgDir_Incoming);
				lobjConv.setAt(Conversation.I.DUEDATE, mdtDueDate);

				lobjNewAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjNewAgendaItem.setAt(0, "Troca de Mensagens");
				lobjNewAgendaItem.setAt(1, Engine.getCurrentUser().toString());
				lobjNewAgendaItem.setAt(2, Constants.ProcID_Conversation);
				lobjNewAgendaItem.setAt(3, ldtNow);
				lobjNewAgendaItem.setAt(4, mdtDueDate);
				lobjNewAgendaItem.setAt(5, Constants.UrgID_Valid);
				lobjNewAgendaItem.SaveToDb(pdb);
				lobjNewAgendaItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_Conversation_SendMessage,
						Constants.OPID_Conversation_ReSendMessage, Constants.OPID_Conversation_ReceiveMessage,
						Constants.OPID_Conversation_CloseProcess}, pdb);
			}

			lobjConv.SaveToDb(pdb);
			mobjData.mlngNumber = lobjConv.GetCurrentMessages(pdb).length;
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mobjData.mobjDocOps != null )
			mobjData.mobjDocOps.RunSubOp(pdb, lidContainer);
		if ( mobjData.mobjContactOps != null )
			mobjData.mobjContactOps.RunSubOp(pdb, lidContainer);

		try
		{
			mobjData.midOwner = lobjConv.getKey();
			lobjMessage = Message.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjMessage);
			lobjMessage.SaveToDb(pdb);
			mobjData.mid = lobjMessage.getKey();

			if ( mobjData.marrAddresses != null )
			{
				for ( i = 0; i < mobjData.marrAddresses.length; i++ )
				{
					mobjData.marrAddresses[i].midOwner = lobjMessage.getKey();
					lobjAddr = MessageAddress.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrAddresses[i].ToObject(lobjAddr);
					lobjAddr.SaveToDb(pdb);
					mobjData.marrAddresses[i].mid = lobjAddr.getKey();
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mobjData.mbIsEmail )
		{
			try
			{
				MailConnector.SendFromData(mobjData);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}
}
