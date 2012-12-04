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
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Conversation;
import com.premiumminds.BigBang.Jewel.Objects.Message;
import com.premiumminds.BigBang.Jewel.Objects.MessageAddress;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class ReceiveMessage
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public MessageData mobjData;
	public Timestamp mdtDueDate;
	private UUID midPrevDir;
	private Timestamp mdtPrevLimit;
	private String mstrNewEmailID;

	public ReceiveMessage(UUID pidProcess)
	{
		super(pidProcess);
	}

	protected UUID OpID()
	{
		return Constants.OPID_Conversation_ReceiveMessage;
	}

	public String ShortDesc()
	{
		return "Recepção de Mensagem";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A informação recebida foi a seguinte:");

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
		mdtPrevLimit = (Timestamp)lobjConv.getAt(Conversation.I.DUEDATE);
		midPrevDir = (UUID)lobjConv.getAt(Conversation.I.PENDINGDIRECTION);

		try
		{
			if ( mdtDueDate == null )
			{
				TriggerOp(new TriggerAutoCloseProcess(GetProcess().getKey()), pdb);
			}
			else
			{
				lobjConv.setAt(Conversation.I.PENDINGDIRECTION, Constants.MsgDir_Outgoing);
				lobjConv.setAt(Conversation.I.DUEDATE, mdtDueDate);

				lobjNewAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjNewAgendaItem.setAt(0, "Troca de Mensagens");
				lobjNewAgendaItem.setAt(1, Engine.getCurrentUser().toString());
				lobjNewAgendaItem.setAt(2, Constants.ProcID_Conversation);
				lobjNewAgendaItem.setAt(3, ldtNow);
				lobjNewAgendaItem.setAt(4, mdtDueDate);
				lobjNewAgendaItem.setAt(5, Constants.UrgID_Pending);
				lobjNewAgendaItem.SaveToDb(pdb);
				lobjNewAgendaItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_Conversation_SendMessage,
						Constants.OPID_Conversation_ReSendMessage, Constants.OPID_Conversation_ReceiveMessage,
						Constants.OPID_Conversation_CloseProcess}, pdb);
			}
			lobjConv.SaveToDb(pdb);
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mobjData.mobjDocOps != null )
			mobjData.mobjDocOps.RunSubOp(pdb, GetProcess().GetParent().GetDataKey());
		if ( mobjData.mobjContactOps != null )
			mobjData.mobjContactOps.RunSubOp(pdb, GetProcess().GetParent().GetDataKey());

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
				mstrNewEmailID = MailConnector.DoProcessItem(mobjData.mstrEmailID).getId().getUniqueId();
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A informação será retirada.");

		if ( mstrNewEmailID != null )
			lstrResult.append(" O email recebido será re-disponibilizado para outra utilização.");

		if ( mobjData.mobjDocOps != null )
			mobjData.mobjDocOps.UndoDesc(lstrResult, pstrLineBreak);
		if ( mobjData.mobjContactOps != null )
			mobjData.mobjContactOps.UndoDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A informação foi retirada.");

		if ( mstrNewEmailID != null )
			lstrResult.append(" O email recebido foi re-disponibilizado para outra utilização.");

		if ( mobjData.mobjDocOps != null )
			mobjData.mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);
		if ( mobjData.mobjContactOps != null )
			mobjData.mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		MessageAddress lobjAddr;
		Message lobjMsg;
		Timestamp ldtNow;
		HashMap<UUID, AgendaItem> larrItems;
		IEntity lrefAux;
		ResultSet lrs;
		ObjectBase lobjAgendaProc;
		Conversation lobjConv;
		AgendaItem lobjNewAgendaItem;
		UUID lidUrgency;
		UUID[] larrUsers;
		int i;

		try
		{
			if ( mobjData.marrAddresses != null )
			{
				for ( i = 0; i < mobjData.marrAddresses.length; i++ )
				{
					lobjAddr = MessageAddress.GetInstance(Engine.getCurrentNameSpace(),
							mobjData.marrAddresses[i].mid);
					lobjAddr.getDefinition().Delete(pdb, lobjAddr.getKey());
				}
			}
			lobjMsg = Message.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);
			lobjMsg.getDefinition().Delete(pdb, lobjMsg.getKey());
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mobjData.mobjDocOps != null )
			mobjData.mobjDocOps.UndoSubOp(pdb, GetProcess().GetParent().GetDataKey());
		if ( mobjData.mobjContactOps != null )
			mobjData.mobjContactOps.UndoSubOp(pdb, GetProcess().GetParent().GetDataKey());

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

		try
		{
			lobjConv = (Conversation)GetProcess().GetData();
			lobjConv.setAt(Conversation.I.DUEDATE, mdtPrevLimit);
			lobjConv.setAt(Conversation.I.PENDINGDIRECTION, midPrevDir);
			lobjConv.SaveToDb(pdb);

    		if ( Constants.MsgDir_Incoming.equals(midPrevDir) )
    			lidUrgency = Constants.UrgID_Valid;
    		else
    			lidUrgency = Constants.UrgID_Pending;

    		larrUsers = lobjConv.GetUsers(pdb);
			for ( i = 0; i < larrUsers.length; i++ )
			{
				lobjNewAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
				lobjNewAgendaItem.setAt(0, "Troca de Mensagens");
				lobjNewAgendaItem.setAt(1, Engine.getCurrentUser().toString());
				lobjNewAgendaItem.setAt(2, Constants.ProcID_Conversation);
				lobjNewAgendaItem.setAt(3, ldtNow);
				lobjNewAgendaItem.setAt(4, mdtPrevLimit);
				lobjNewAgendaItem.setAt(5, lidUrgency);
				lobjNewAgendaItem.SaveToDb(pdb);
				lobjNewAgendaItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_Conversation_SendMessage,
						Constants.OPID_Conversation_ReSendMessage, Constants.OPID_Conversation_ReceiveMessage,
						Constants.OPID_Conversation_CloseProcess}, pdb);
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mstrNewEmailID != null )
		{
			try
			{
				MailConnector.DoUnprocessItem(mstrNewEmailID);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}

	public UndoSet[] GetSets()
	{
		return new UndoSet[0];
	}
}
