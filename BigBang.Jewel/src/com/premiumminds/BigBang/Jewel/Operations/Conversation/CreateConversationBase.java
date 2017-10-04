package com.premiumminds.BigBang.Jewel.Operations.Conversation;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.BodyPart;
import javax.mail.internet.MimeMessage;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.Interfaces.IProcess;
import Jewel.Petri.Interfaces.IScript;
import Jewel.Petri.Objects.PNProcess;
import Jewel.Petri.Objects.PNScript;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ConversationData;
import com.premiumminds.BigBang.Jewel.Data.DocDataLight;
import com.premiumminds.BigBang.Jewel.Data.MessageAttachmentData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Objects.AgendaItem;
import com.premiumminds.BigBang.Jewel.Objects.Conversation;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.Message;
import com.premiumminds.BigBang.Jewel.Objects.MessageAddress;
import com.premiumminds.BigBang.Jewel.Objects.MessageAttachment;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;
import com.premiumminds.BigBang.Jewel.SysObjects.StorageConnector;

public abstract class CreateConversationBase
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public ConversationData mobjData;

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
		int i, j, k;
		Conversation lobjConv;
		Message lobjMessage;
		MessageAddress lobjAddr;
		MessageAttachment lobjAttachment;
		HashSet<UUID> larrUsers;
		IScript lobjScript;
		IProcess lobjProc;
		UUID lidContainer;
		boolean b;
		AgendaItem lobjAgendaItem;
		Map<String, String> larrAttTrans;

		if ( (mobjData.marrMessages == null) || (mobjData.marrMessages.length != 1))
			throw new JewelPetriException("Erro: Tem que indicar a mensagem inicial.");

		if ( mobjData.mstrSubject == null )
			mobjData.mstrSubject = mobjData.marrMessages[0].mstrSubject;
		if ( mobjData.mstrSubject == null )
			mobjData.mstrSubject = "(sem assunto)";
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

			lidContainer = lobjConv.getParentContainer();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mobjData.marrMessages[0].mobjContactOps != null )
			mobjData.marrMessages[0].mobjContactOps.RunSubOp(pdb, lidContainer);

		try
		{
			if ( mobjData.marrMessages[0].mstrSubject == null )
				mobjData.marrMessages[0].mstrSubject = mobjData.mstrSubject;
			mobjData.marrMessages[0].midOwner = lobjConv.getKey();
			mobjData.marrMessages[0].mlngNumber = 0;
			lobjMessage = Message.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.marrMessages[0].ToObject(lobjMessage);
			lobjMessage.SaveToDb(pdb);
			mobjData.marrMessages[0].mid = lobjMessage.getKey();

			if ( mobjData.marrMessages[0].marrAddresses != null )
			{
				for ( i = 0; i < mobjData.marrMessages[0].marrAddresses.length; i++ )
				{
					if ( Constants.MsgDir_Incoming.equals(mobjData.midStartDir) &&
							(mobjData.marrMessages[0].marrAddresses[i].midInfo == null) &&
							Constants.UsageID_From.equals(mobjData.marrMessages[0].marrAddresses[i].midUsage) &&
							(mobjData.marrMessages[0].mobjContactOps != null) )
					{
						b = false;
						if ( mobjData.marrMessages[0].mobjContactOps.marrModify != null )
						{
							for ( j = 0; !b && j < mobjData.marrMessages[0].mobjContactOps.marrModify.length; j++ )
							{
								if ( mobjData.marrMessages[0].mobjContactOps.marrModify[j].marrInfo != null )
								{
									for ( k = 0; !b && k < mobjData.marrMessages[0].mobjContactOps.marrModify[j].marrInfo.length; k++ )
									{
										if ( Constants.CInfoID_Email.equals(
												mobjData.marrMessages[0].mobjContactOps.marrModify[j].marrInfo[k].midType) &&
												mobjData.marrMessages[0].marrAddresses[i].mstrAddress.equals(
												mobjData.marrMessages[0].mobjContactOps.marrModify[j].marrInfo[k].mstrValue) )
										{
											mobjData.marrMessages[0].marrAddresses[i].midInfo =
													mobjData.marrMessages[0].mobjContactOps.marrModify[j].marrInfo[k].mid;
											b = true;
										}
									}
								}
							}
						}
						if ( mobjData.marrMessages[0].mobjContactOps.marrCreate != null )
						{
							for ( j = 0; !b && j < mobjData.marrMessages[0].mobjContactOps.marrCreate.length; j++ )
							{
								if ( mobjData.marrMessages[0].mobjContactOps.marrCreate[j].marrInfo != null )
								{
									for ( k = 0; !b && k < mobjData.marrMessages[0].mobjContactOps.marrCreate[j].marrInfo.length; k++ )
									{
										if ( Constants.CInfoID_Email.equals(
												mobjData.marrMessages[0].mobjContactOps.marrCreate[j].marrInfo[k].midType) &&
												mobjData.marrMessages[0].marrAddresses[i].mstrAddress.equals(
												mobjData.marrMessages[0].mobjContactOps.marrCreate[j].marrInfo[k].mstrValue) )
										{
											mobjData.marrMessages[0].marrAddresses[i].midInfo =
													mobjData.marrMessages[0].mobjContactOps.marrCreate[j].marrInfo[k].mid;
											b = true;
										}
									}
								}
							}
						}
					}

					mobjData.marrMessages[0].marrAddresses[i].midOwner = lobjMessage.getKey();
					lobjAddr = MessageAddress.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrMessages[0].marrAddresses[i].ToObject(lobjAddr);
					lobjAddr.SaveToDb(pdb);
					mobjData.marrMessages[0].marrAddresses[i].mid = lobjAddr.getKey();
				}
			}

			if ( mobjData.marrMessages[0].marrAttachments != null ) // ou cria um doc op falso,ou aqui tem q apanhar o correspondente tb pode "re-orde
			{
				for ( i = 0; i < mobjData.marrMessages[0].marrAttachments.length; i++ )
				{
					if ( (mobjData.marrMessages[0].marrAttachments[i].midDocument == null) &&
							(mobjData.marrMessages[0].mobjDocOps != null) &&
							(mobjData.marrMessages[0].mobjDocOps.marrCreate2 != null) &&
							(mobjData.marrMessages[0].mobjDocOps.marrCreate2.length > 0))
						
						for (int u=0; u<mobjData.marrMessages[0].mobjDocOps.marrCreate2.length; u++) {
							if (mobjData.marrMessages[0].marrAttachments[i].mstrAttId.equals(mobjData.marrMessages[0].mobjDocOps.marrCreate2[u].mstrText)) {
								mobjData.marrMessages[0].marrAttachments[i].midDocument = mobjData.marrMessages[0].mobjDocOps.marrCreate2[u].mid;
								break;
							}
						}

					mobjData.marrMessages[0].marrAttachments[i].midOwner = lobjMessage.getKey();
					lobjAttachment = MessageAttachment.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrMessages[0].marrAttachments[i].ToObject(lobjAttachment);
					lobjAttachment.SaveToDb(pdb);
					mobjData.marrMessages[0].marrAttachments[i].mid = lobjAttachment.getKey();
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage() + " 233 ", e);
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
					throw new JewelPetriException(e.getMessage() + " 267 ", e);
				}
	    	}
		}

		if ( (mobjData.marrMessages[0].mstrEmailID == null) && Constants.MsgDir_Outgoing.equals(mobjData.midStartDir) )
			TriggerOp(new ExternInitialSend(mobjData.midProcess), pdb);

		if ( mobjData.mdtDueDate == null )
			TriggerOp(new TriggerAutoCloseProcess(mobjData.midProcess), pdb);

		if ( mobjData.marrMessages[0].mbIsEmail )
		{
			try
			{
				if ( mobjData.marrMessages[0].mstrEmailID != null ) {
					
					javax.mail.Message mailMsg = MailConnector.conditionalGetMessage(mobjData.marrMessages[0].mstrFolderID, mobjData.marrMessages[0].mstrEmailID, null);
					
					LinkedHashMap<String, BodyPart> mailAttachments = MailConnector.conditionalGetAttachmentsMap((MimeMessage) mailMsg, null);
					
					larrAttTrans = MailConnector.processItem(mobjData.marrMessages[0].mstrEmailID, mobjData.marrMessages[0].mstrFolderID,
							mailMsg, mailAttachments);
					mobjData.marrMessages[0].mstrEmailID = larrAttTrans.get("_");
					String tmpBody = MailConnector.conditionalGetBody((MimeMessage) mailMsg, mailAttachments);
					
					mobjData.marrMessages[0].mstrBody = tmpBody; // TODO é igual ao que já está... para que é que faço set?
					mobjData.marrMessages[0].ToObject(lobjMessage);
					lobjMessage.SaveToDb(pdb);

					if ( mobjData.marrMessages[0].marrAttachments != null )
					{
						for ( i = 0; i < mobjData.marrMessages[0].marrAttachments.length; i++ )
						{
							lobjAttachment = MessageAttachment.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrMessages[0].marrAttachments[i].mid);
							mobjData.marrMessages[0].marrAttachments[i].ToObject(lobjAttachment);
							lobjAttachment.SaveToDb(pdb);
						}
					}

					// Calls the method responsble for updating the message to google storage.
					StorageConnector.threadedUpload(mailMsg, mobjData.marrMessages[0].mstrEmailID);
					
					//MailConnector.clearStoredValues(true, true, true, (MimeMessage) mailMsg);
				}
				else
				{
					MailConnector.sendFromData(mobjData.marrMessages[0]);
				}
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
		
		if ( mobjData.marrMessages[0].mobjDocOps != null ) {
			try {
				runThreadedCreateDocs(pdb, lidContainer, Engine.getCurrentNameSpace(), MailConnector.getUserEmail());
			} catch (BigBangJewelException e) {
				// TODO Auto-generated catch block
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}

	private void runThreadedCreateDocs(final SQLServer pdb, final UUID lidContainer, final UUID nmSpace, final String existingUserEmail) {
		Runnable r = new Runnable() {
			public void run() {
				MessageData messageData = mobjData.marrMessages[0];
				for (int i=0; i<messageData.marrAttachments.length; i++) {
					MessageAttachmentData messageAttachmentData = messageData.marrAttachments[i];
					if ( messageAttachmentData.mstrAttId != null )
					{
						try
						{
							messageData.mobjDocOps.marrCreate2[i].mobjFile = MailConnector.getAttachment(messageData.mstrEmailID,
									messageData.mstrFolderID, messageAttachmentData.mstrAttId, existingUserEmail).GetVarData();
							//mobjData.marrMessages[0].mobjDocOps.RunSubOp(pdb, lidContainer);
							Document lobjAux = Document.GetInstance(nmSpace, (UUID)null);
							DocDataLight pobjData = messageData.mobjDocOps.marrCreate2[i];
							pobjData.midOwnerId = lidContainer;
							pobjData.mdtRefDate = new Timestamp(new java.util.Date().getTime());
							pobjData.ToObject(lobjAux);
							lobjAux.SetReadonly();
							try
							{
								lobjAux.SaveToDb(pdb);
							}
							catch (Throwable e)
							{
								throw new BigBangJewelException(e.getMessage(), e);
							}
							
						/*	TODO Cannot get this to work, because the session is lost, and used "in a lot of places" to get editable cache... etc e tal
						 * MessageAttachment lobjAttachment = MessageAttachment.GetInstance2(nmSpace, messageAttachmentData.mid);
							MessageAttachmentData data = new MessageAttachmentData();
							data.FromObject(lobjAttachment);
							data.midDocument = lobjAux.getKey();
							lobjAttachment.ForceReadable();
							data.ToObject(lobjAttachment);
							lobjAttachment.SaveToDb(pdb);*/
						}
						catch (Throwable e)
						{
							System.out.println(e.getMessage());
						}
					}
				}
				MailConnector.clearAttsMap(existingUserEmail, messageData.mstrEmailID);
				/*try {
					//mobjData.marrMessages[0].mobjDocOps.RunSubOp(pdb, lidContainer);
				} catch (Throwable e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} */
	        }
		};
		
		new Thread(r).start();
	}

	public boolean LocalCanUndo()
	{
		return (mobjData.marrMessages[0].mstrEmailID != null);
	}

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("O processo criado será eliminado.");

		if ( mobjData.marrMessages[0].mbIsEmail && (mobjData.marrMessages[0].mstrEmailID != null) /*&& Constants.MsgDir_Incoming.equals(mobjData.marrMessages[0].midDirection)*/ )
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

		if ( mobjData.marrMessages[0].mbIsEmail && (mobjData.marrMessages[0].mstrEmailID != null) /*&& Constants.MsgDir_Incoming.equals(mobjData.marrMessages[0].midDirection)*/ )
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
		HashMap<UUID, AgendaItem> larrItems;
		ResultSet lrs;
		IEntity lrefAux;
		ObjectBase lobjAgendaProc;
		MessageAttachment lobjAttachment;
		MessageAddress lobjAddr;
		Message lobjMsg;
		Conversation lobjConv;
		int i, j;
		UUID lidContainer;
		ExternAbortProcess lopEAP;

		larrItems = new HashMap<UUID, AgendaItem>();
		lrs = null;
		try
		{
			lrefAux = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_AgendaProcess));
			lrs = lrefAux.SelectByMembers(pdb, new int[] {1}, new java.lang.Object[] {mobjData.midProcess}, new int[0]);
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
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		lidContainer = null;
		try
		{
			if ( mobjData != null )
			{
				lobjConv = Conversation.GetInstance(Engine.getCurrentNameSpace(), mobjData.mid);
				lidContainer = lobjConv.getParentContainer();
				if ( mobjData.marrMessages != null )
				{
					for ( i = 0; i < mobjData.marrMessages.length; i++ )
					{
						lobjMsg = Message.GetInstance(Engine.getCurrentNameSpace(), mobjData.marrMessages[i].mid);
						if ( mobjData.marrMessages[i].marrAttachments != null )
						{
							for ( j = 0; j < mobjData.marrMessages[i].marrAttachments.length; j++ )
							{
								lobjAttachment = MessageAttachment.GetInstance(Engine.getCurrentNameSpace(),
										mobjData.marrMessages[i].marrAttachments[j].mid);
								lobjAttachment.getDefinition().Delete(pdb, lobjAttachment.getKey());
							}
						}
						if ( mobjData.marrMessages[i].marrAddresses != null )
						{
							for ( j = 0; j < mobjData.marrMessages[i].marrAddresses.length; j++ )
							{
								lobjAddr = MessageAddress.GetInstance(Engine.getCurrentNameSpace(),
										mobjData.marrMessages[i].marrAddresses[j].mid);
								lobjAddr.getDefinition().Delete(pdb, lobjAddr.getKey());
							}
						}
						lobjMsg.getDefinition().Delete(pdb, lobjMsg.getKey());
					}
				}
				lobjConv.getDefinition().Delete(pdb, lobjConv.getKey());
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( lidContainer != null )
		{
			if ( mobjData.marrMessages[0].mobjDocOps != null )
				mobjData.marrMessages[0].mobjDocOps.UndoSubOp(pdb, lidContainer);

			if ( mobjData.marrMessages[0].mobjContactOps != null )
				mobjData.marrMessages[0].mobjContactOps.UndoSubOp(pdb, lidContainer);
		}

		if (mobjData.mdtDueDate == null )
			PNProcess.GetInstance(Engine.getCurrentNameSpace(), mobjData.midProcess).Restart(pdb);
		//Needed to allow the ExternAbortProcess trigger to run

		lopEAP = new ExternAbortProcess(mobjData.midProcess);
		lopEAP.mstrReviveEmailID = mobjData.marrMessages[0].mstrEmailID;

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
