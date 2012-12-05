package com.premiumminds.BigBang.Jewel.Operations.Conversation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Conversation;
import com.premiumminds.BigBang.Jewel.Objects.Message;
import com.premiumminds.BigBang.Jewel.Objects.MessageAddress;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public abstract class CreateConversationBase
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public ConversationData mobjData;
	private String mstrNewEmailID;

	public CreateConversationBase(UUID pidProcess)
	{
		super(pidProcess);
	}

	public String ShortDesc()
	{
		return "Criação de Sub-Processo: Troca de Mensagens";
	}

	public String LongDesc(String pstrLineBreak)
	{
		StringBuilder lstrBuffer;

		lstrBuffer = new StringBuilder();

		mobjData.Describe(lstrBuffer, pstrLineBreak);

		return lstrBuffer.toString();
	}

	public UUID GetExternalProcess()
	{
		return mobjData.midProcess;
	}

	protected void Run(SQLServer pdb)
		throws JewelPetriException
	{
		UUID lidUrgency;
		int i;
		Conversation lobjConv;
		Message lobjMessage;
		MessageAddress lobjAddr;
		HashSet<UUID> larrUsers;
		IScript lobjScript;
		IProcess lobjProc;
		AgendaItem lobjAgendaItem;

		if ( (mobjData.marrMessages == null) || (mobjData.marrMessages.length != 1) )
			throw new JewelPetriException("Erro: Tem que indicar a mensagem inicial.");

		mobjData.mstrSubject = mobjData.marrMessages[0].mstrSubject;
		mobjData.midStartDir = mobjData.marrMessages[0].midDirection;

		if ( mobjData.mdtDueDate == null )
		{
			mobjData.midPendingDir = null;
			lidUrgency = null;
		}
		else
		{
    		if ( Constants.MsgDir_Outgoing.equals(mobjData.midStartDir) )
    		{
    			mobjData.midPendingDir = Constants.MsgDir_Incoming;
    			lidUrgency = Constants.UrgID_Valid;
    		}
    		else
    		{
    			mobjData.midPendingDir = Constants.MsgDir_Outgoing;
    			lidUrgency = Constants.UrgID_Pending;
    		}
		}

		try
		{
			lobjConv = Conversation.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjConv);
			lobjConv.SaveToDb(pdb);
			mobjData.mid = lobjConv.getKey();

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_Conversation);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjConv.getKey(), GetProcess().getKey(),
					GetContext(), pdb);

			mobjData.midProcess = lobjProc.getKey();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mobjData.marrMessages[0].mobjDocOps != null )
			mobjData.marrMessages[0].mobjDocOps.RunSubOp(pdb, GetProcess().GetDataKey());
		if ( mobjData.marrMessages[0].mobjContactOps != null )
			mobjData.marrMessages[0].mobjContactOps.RunSubOp(pdb, GetProcess().GetDataKey());

		try
		{
			mobjData.marrMessages[0].midOwner = lobjConv.getKey();
			lobjMessage = Message.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.marrMessages[0].ToObject(lobjMessage);
			lobjMessage.SaveToDb(pdb);
			mobjData.marrMessages[0].mid = lobjMessage.getKey();

			if ( mobjData.marrMessages[0].marrAddresses != null )
			{
				for ( i = 0; i < mobjData.marrMessages[0].marrAddresses.length; i++ )
				{
					mobjData.marrMessages[0].marrAddresses[i].midOwner = lobjMessage.getKey();
					lobjAddr = MessageAddress.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrMessages[0].marrAddresses[i].ToObject(lobjAddr);
					lobjAddr.SaveToDb(pdb);
					mobjData.marrMessages[0].marrAddresses[i].mid = lobjAddr.getKey();
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		larrUsers = new HashSet<UUID>();
		larrUsers.add(Engine.getCurrentUser());
		if ( mobjData.marrMessages[0].marrAddresses != null )
		{
			for ( i = 0; i < mobjData.marrMessages[0].marrAddresses.length; i++ )
			{
				if ( mobjData.marrMessages[0].marrAddresses[i].midUser != null )
					larrUsers.add(mobjData.marrMessages[0].marrAddresses[i].midUser);
			}
		}

		if ( mobjData.mdtDueDate != null )
		{
	    	for ( UUID lidUser : larrUsers )
	    	{
				try
				{
					lobjAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAgendaItem.setAt(0, "Troca de Mensagens");
					lobjAgendaItem.setAt(1, lidUser);
					lobjAgendaItem.setAt(2, Constants.ProcID_Conversation);
					lobjAgendaItem.setAt(3, new Timestamp(new java.util.Date().getTime()));
					lobjAgendaItem.setAt(4, mobjData.mdtDueDate);
					lobjAgendaItem.setAt(5, lidUrgency);
					lobjAgendaItem.SaveToDb(pdb);
					lobjAgendaItem.InitNew(new UUID[] {mobjData.midProcess}, new UUID[] {Constants.OPID_Conversation_SendMessage,
							Constants.OPID_Conversation_ReSendMessage, Constants.OPID_Conversation_ReceiveMessage,
							Constants.OPID_Conversation_CloseProcess}, pdb);
				}
				catch (Throwable e)
				{
					throw new JewelPetriException(e.getMessage(), e);
				}
	    	}
		}

		if ( mobjData.mdtDueDate == null )
			TriggerOp(new TriggerAutoCloseProcess(mobjData.midProcess), pdb);
		else if ( Constants.MsgDir_Outgoing.equals(mobjData.midStartDir) )
			TriggerOp(new ExternInitialSend(mobjData.midProcess), pdb);

		if ( mobjData.marrMessages[0].mbIsEmail )
		{
			try
			{
				if ( Constants.MsgDir_Incoming.equals(mobjData.marrMessages[0].midDirection) )
				{
					mstrNewEmailID = MailConnector.DoProcessItem(mobjData.marrMessages[0].mstrEmailID).getId().getUniqueId();
				}
				else
				{
					MailConnector.SendFromData(mobjData.marrMessages[0]);
				}
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}

	public boolean LocalCanUndo()
	{
		return Constants.MsgDir_Incoming.equals(mobjData.marrMessages[0].midDirection);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("O processo criado será eliminado.");

		if ( mobjData.marrMessages[0].mbIsEmail && Constants.MsgDir_Incoming.equals(mobjData.marrMessages[0].midDirection) )
			lstrResult.append(" O email recebido será re-disponibilizado para outra utilização.");

		if ( mobjData.marrMessages[0].mobjDocOps != null )
			mobjData.marrMessages[0].mobjDocOps.UndoDesc(lstrResult, pstrLineBreak);

		if ( mobjData.marrMessages[0].mobjContactOps != null )
			mobjData.marrMessages[0].mobjContactOps.UndoDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	public String UndoLongDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("O processo criado foi eliminado.");

		if ( mobjData.marrMessages[0].mbIsEmail && Constants.MsgDir_Incoming.equals(mobjData.marrMessages[0].midDirection) )
			lstrResult.append(" O email recebido foi re-disponibilizado para outra utilização.");

		if ( mobjData.marrMessages[0].mobjDocOps != null )
			mobjData.marrMessages[0].mobjDocOps.UndoLongDesc(lstrResult, pstrLineBreak);

		if ( mobjData.marrMessages[0].mobjContactOps != null )
			mobjData.marrMessages[0].mobjContactOps.UndoLongDesc(lstrResult, pstrLineBreak);

		return lstrResult.toString();
	}

	protected void Undo(SQLServer pdb)
		throws JewelPetriException
	{
		ExternAbortProcess lopEAP;

		if ( mobjData.marrMessages[0].mobjDocOps != null )
			mobjData.marrMessages[0].mobjDocOps.UndoSubOp(pdb, GetProcess().GetDataKey());

		if ( mobjData.marrMessages[0].mobjContactOps != null )
			mobjData.marrMessages[0].mobjContactOps.UndoSubOp(pdb, GetProcess().GetDataKey());

		if (mobjData.mdtDueDate == null )
			PNProcess.GetInstance(Engine.getCurrentNameSpace(), mobjData.midProcess).Restart(pdb);

		lopEAP = new ExternAbortProcess(mobjData.midProcess);
		lopEAP.mobjData = mobjData;
		lopEAP.mstrReviveEmailID = mstrNewEmailID;

		TriggerOp(lopEAP, pdb);
	}

	public UndoSet[] GetSets()
	{
		UndoSet[] larrResult;
		UndoSet lobjContacts, lobjDocs;
		int llngSize;
		int i;

		lobjContacts = GetContactSet();
		lobjDocs = GetDocSet();

		llngSize = 0;
		if ( lobjContacts != null )
			llngSize++;
		if ( lobjDocs != null )
			llngSize++;

		larrResult = new UndoSet[llngSize];
		i = 0;

		if ( lobjContacts != null )
		{
			larrResult[i] = lobjContacts;
			i++;
		}

		if ( lobjDocs != null )
		{
			larrResult[i] = lobjDocs;
			i++;
		}

		return larrResult;
	}

	private UndoSet GetDocSet()
	{
		int llngCreates, llngModifies, llngDeletes;
		ArrayList<UndoSet> larrTally;
		UndoSet[] larrAux;
		UndoSet lobjResult;
		int i, j, iD, iM, iC;

		llngCreates = 0;
		llngModifies = 0;
		llngDeletes = 0;

		larrTally = new ArrayList<UndoSet>();

		if ( mobjData.marrMessages[0].mobjDocOps != null )
		{
			larrAux = mobjData.marrMessages[0].mobjDocOps.GetSubSet();
			for ( j = 0; j < larrAux.length; j++ )
			{
				if ( !Constants.ObjID_Document.equals(larrAux[j].midType) )
					continue;
				llngDeletes += larrAux[j].marrDeleted.length;
				llngModifies += larrAux[j].marrChanged.length;
				llngCreates += larrAux[j].marrCreated.length;
				larrTally.add(larrAux[j]);
			}
		}

		if ( llngDeletes + llngModifies + llngCreates == 0)
			return null;

		larrAux = larrTally.toArray(new UndoSet[larrTally.size()]);

		lobjResult = new UndoSet();
		lobjResult.midType = Constants.ObjID_Document;
		lobjResult.marrDeleted = new UUID[llngDeletes];
		lobjResult.marrChanged = new UUID[llngModifies];
		lobjResult.marrCreated = new UUID[llngCreates];

		iD = 0;
		iM = 0;
		iC = 0;

		for ( i = 0; i < larrAux.length; i++ )
		{
			for ( j = 0; j < larrAux[i].marrDeleted.length; j++ )
				lobjResult.marrDeleted[iD + j] = larrAux[i].marrDeleted[j];
			iD += larrAux[i].marrDeleted.length;

			for ( j = 0; j < larrAux[i].marrChanged.length; j++ )
				lobjResult.marrChanged[iM + j] = larrAux[i].marrChanged[j];
			iM += larrAux[i].marrChanged.length;

			for ( j = 0; j < larrAux[i].marrCreated.length; j++ )
				lobjResult.marrCreated[iC + j] = larrAux[i].marrCreated[j];
			iC += larrAux[i].marrCreated.length;
		}

		return lobjResult;
	}

	private UndoSet GetContactSet()
	{
		int llngCreates, llngModifies, llngDeletes;
		ArrayList<UndoSet> larrTally;
		UndoSet[] larrAux;
		UndoSet lobjResult;
		int i, j, iD, iM, iC;

		llngCreates = 0;
		llngModifies = 0;
		llngDeletes = 0;

		larrTally = new ArrayList<UndoSet>();

		if ( mobjData.marrMessages[0].mobjContactOps != null )
		{
			larrAux = mobjData.marrMessages[0].mobjContactOps.GetSubSet();
			for ( j = 0; j < larrAux.length; j++ )
			{
				if ( !Constants.ObjID_Contact.equals(larrAux[j].midType) )
					continue;
				llngDeletes += larrAux[j].marrDeleted.length;
				llngModifies += larrAux[j].marrChanged.length;
				llngCreates += larrAux[j].marrCreated.length;
				larrTally.add(larrAux[j]);
			}
		}

		if ( llngDeletes + llngModifies + llngCreates == 0)
			return null;

		larrAux = larrTally.toArray(new UndoSet[larrTally.size()]);

		lobjResult = new UndoSet();
		lobjResult.midType = Constants.ObjID_Contact;
		lobjResult.marrDeleted = new UUID[llngDeletes];
		lobjResult.marrChanged = new UUID[llngModifies];
		lobjResult.marrCreated = new UUID[llngCreates];

		iD = 0;
		iM = 0;
		iC = 0;

		for ( i = 0; i < larrAux.length; i++ )
		{
			for ( j = 0; j < larrAux[i].marrDeleted.length; j++ )
				lobjResult.marrDeleted[iD + j] = larrAux[i].marrDeleted[j];
			iD += larrAux[i].marrDeleted.length;

			for ( j = 0; j < larrAux[i].marrChanged.length; j++ )
				lobjResult.marrChanged[iM + j] = larrAux[i].marrChanged[j];
			iM += larrAux[i].marrChanged.length;

			for ( j = 0; j < larrAux[i].marrCreated.length; j++ )
				lobjResult.marrCreated[iC + j] = larrAux[i].marrCreated[j];
			iC += larrAux[i].marrCreated.length;
		}

		return lobjResult;
	}
}
