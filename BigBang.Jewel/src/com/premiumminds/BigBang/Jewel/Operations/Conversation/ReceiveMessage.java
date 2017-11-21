package com.premiumminds.BigBang.Jewel.Operations.Conversation;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.mail.BodyPart;
import javax.mail.internet.MimeMessage;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;
import Jewel.Engine.SysObjects.ObjectBase;
import Jewel.Petri.SysObjects.JewelPetriException;
import Jewel.Petri.SysObjects.UndoableOperation;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
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

public class ReceiveMessage
	extends UndoableOperation
{
	private static final long serialVersionUID = 1L;

	public MessageData mobjData;
	public Timestamp mdtDueDate;
	private UUID midPrevDir;
	private Timestamp mdtPrevLimit;
//	private String mstrNewEmailID;

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
		UUID lidContainer;
		AgendaItem lobjNewAgendaItem;
		Message lobjMessage;
		MessageAddress lobjAddr;
		MessageAttachment lobjAttachment;
		int i, j, k;
		boolean b;
		Map<String, String> larrAttTrans;
		UUID msgID = null;

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
			throw new JewelPetriException(e.getMessage() + " 114 ", e);
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
			throw new JewelPetriException(e.getMessage() + " 127 ", e);
		}

		lobjConv = (Conversation)GetProcess().GetData();
		mdtPrevLimit = (Timestamp)lobjConv.getAt(Conversation.I.DUEDATE);
		midPrevDir = (UUID)lobjConv.getAt(Conversation.I.PENDINGDIRECTION);

		try
		{
			lidContainer = lobjConv.getParentContainer();

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
			mobjData.mlngNumber = lobjConv.GetCurrentMessages(pdb).length;
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage() + "165 ", e);
		}

		if ( mobjData.mobjContactOps != null )
			mobjData.mobjContactOps.RunSubOp(pdb, lidContainer);

		try
		{
			if ( mobjData.mstrSubject == null )
				mobjData.mstrSubject = (String)lobjConv.getAt(Conversation.I.SUBJECT);
			mobjData.midOwner = lobjConv.getKey();
			lobjMessage = Message.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjMessage);
			lobjMessage.SaveToDb(pdb);
			mobjData.mid = lobjMessage.getKey();
			msgID = lobjMessage.getKey();

			if ( mobjData.marrAddresses != null )
			{
				for ( i = 0; i < mobjData.marrAddresses.length; i++ )
				{
					if ( (mobjData.marrAddresses[i].midInfo == null) &&
							Constants.UsageID_From.equals(mobjData.marrAddresses[i].midUsage) &&
							(mobjData.mobjContactOps != null) )
					{
						b = false;
						if ( mobjData.mobjContactOps.marrModify != null )
						{
							for ( j = 0; !b && j < mobjData.mobjContactOps.marrModify.length; j++ )
							{
								if ( mobjData.mobjContactOps.marrModify[j].marrInfo != null )
								{
									for ( k = 0; !b && k < mobjData.mobjContactOps.marrModify[j].marrInfo.length; k++ )
									{
										if ( Constants.CInfoID_Email.equals(
												mobjData.mobjContactOps.marrModify[j].marrInfo[k].midType) &&
												mobjData.marrAddresses[i].mstrAddress.equals(
												mobjData.mobjContactOps.marrModify[j].marrInfo[k].mstrValue) )
										{
											mobjData.marrAddresses[i].midInfo =
													mobjData.mobjContactOps.marrModify[j].marrInfo[k].mid;
											b = true;
										}
									}
								}
							}
						}
						if ( mobjData.mobjContactOps.marrCreate != null )
						{
							for ( j = 0; !b && j < mobjData.mobjContactOps.marrCreate.length; j++ )
							{
								if ( mobjData.mobjContactOps.marrCreate[j].marrInfo != null )
								{
									for ( k = 0; !b && k < mobjData.mobjContactOps.marrCreate[j].marrInfo.length; k++ )
									{
										if ( Constants.CInfoID_Email.equals(
												mobjData.mobjContactOps.marrCreate[j].marrInfo[k].midType) &&
												mobjData.marrAddresses[i].mstrAddress.equals(
												mobjData.mobjContactOps.marrCreate[j].marrInfo[k].mstrValue) )
										{
											mobjData.marrAddresses[i].midInfo =
													mobjData.mobjContactOps.marrCreate[j].marrInfo[k].mid;
											b = true;
										}
									}
								}
							}
						}
					}

					mobjData.marrAddresses[i].midOwner = lobjMessage.getKey();
					lobjAddr = MessageAddress.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					mobjData.marrAddresses[i].ToObject(lobjAddr);
					lobjAddr.SaveToDb(pdb);
					mobjData.marrAddresses[i].mid = lobjAddr.getKey();
				}
			}
			
			if ( mobjData.marrAttachments != null ) // ou cria um doc op falso,ou aqui tem q apanhar o correspondente tb pode "re-orde
			{
				for ( i = 0; i < mobjData.marrAttachments.length; i++ ) {
					
					boolean saveAtt = true;
					
					if (mobjData.marrAttachments[i].mstrAttId != null) {
						
						if ((mobjData.mobjDocOps != null) && 
								(mobjData.mobjDocOps.marrCreate2 != null)) {
							for (int u=0; u<mobjData.mobjDocOps.marrCreate2.length; u++) {
								if ((mobjData.mobjDocOps.marrCreate2[u].mstrText != null) &&
									(mobjData.mobjDocOps.marrCreate2[u].mstrText.equals(mobjData.marrAttachments[i].mstrAttId))	) {
									saveAtt = false;
									break;
								}
							}
						}
						
						if (saveAtt) {
							mobjData.marrAttachments[i].midOwner = lobjMessage.getKey();
							lobjAttachment = MessageAttachment.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
							mobjData.marrAttachments[i].ToObject(lobjAttachment);
							lobjAttachment.SaveToDb(pdb);
							mobjData.marrAttachments[i].mid = lobjAttachment.getKey();
						}
					}
				}
			}
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage() + " 265 ", e);
		}

		if ( mobjData.mstrEmailID != null )
		{
			try
			{
				javax.mail.Message mailMsg = MailConnector.conditionalGetMessage(mobjData.mstrFolderID, mobjData.mstrEmailID, null);
				
				LinkedHashMap<String, BodyPart> mailAttachments = MailConnector.conditionalGetAttachmentsMap((MimeMessage) mailMsg, null);
				String tmpBody = MailConnector.conditionalGetBody((MimeMessage) mailMsg, mailAttachments);
				
				larrAttTrans = MailConnector.processItem(mobjData.mstrEmailID, mobjData.mstrFolderID, mailMsg, mailAttachments);
				mobjData.mstrEmailID = larrAttTrans.get("_");
				mobjData.mstrBody = tmpBody;
				mobjData.ToObject(lobjMessage);
				lobjMessage.SaveToDb(pdb);
				
				// Calls the method responsble for updating the message to google storage.
				StorageConnector.threadedUpload(mailMsg, mobjData.mstrEmailID);
				
				// MailConnector.clearStoredValues(true, true, true, (MimeMessage) mailMsg);
			}
			catch (Throwable e)
			{
				throw new JewelPetriException(e.getMessage() + " 309 ", e);
			}
		}
		
		if ( mobjData.mobjDocOps != null ) {
			try {
				runThreadedCreateDocs(pdb, lidContainer, Engine.getCurrentNameSpace(), MailConnector.getUserEmail(), msgID);
			} catch (BigBangJewelException e) {
				// TODO Auto-generated catch block
				throw new JewelPetriException(e.getMessage(), e);
			}
		}
	}
	
	private void runThreadedCreateDocs(final SQLServer pdb, final UUID lidContainer, final UUID nmSpace, final String existingUserEmail, final UUID messageID) {
		Runnable r = new Runnable() {
			public void run() {
				MessageData messageData = mobjData;
				for (int i=0; i<messageData.marrAttachments.length; i++) {
					MessageAttachmentData messageAttachmentData = messageData.marrAttachments[i];
					if ( messageAttachmentData.mstrAttId != null )
					{
						FileXfer attachment = null;
						try
						{
							for (int u=0; u<messageData.mobjDocOps.marrCreate2.length; u++) {
								DocDataLight pobjData = messageData.mobjDocOps.marrCreate2[u];
								if (pobjData.mstrText!=null && pobjData.mstrText.equals(messageAttachmentData.mstrAttId)) {
									attachment = MailConnector.getAttachment(messageData.mstrEmailID,
											messageData.mstrFolderID, messageAttachmentData.mstrAttId, existingUserEmail);
									messageData.mobjDocOps.marrCreate2[u].mobjFile = attachment.GetVarData();
									Document lobjAux = Document.GetInstance(nmSpace, (UUID)null);
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
									
									// Now saves the corresponding attachment, already with the corresponding fkdoc
									MessageAttachment attachmentDb =  MessageAttachment.GetInstance(nmSpace, (UUID)null);
									mobjData.marrAttachments[i].midDocument = lobjAux.getKey();
									mobjData.marrAttachments[i].midOwner = messageID;
									mobjData.marrAttachments[i].ToObject(attachmentDb);	
									attachmentDb.SetReadonly();
									try
									{
										attachmentDb.SaveToDb(pdb);
									}
									catch (Throwable e)
									{
										throw new BigBangJewelException(e.getMessage(), e);
									}
								}
							}
						}
						catch (Throwable e)
						{
							if (attachment != null) {
								String saveUrl = "C:\\unSavedDocs\\";
								String fileName = messageAttachmentData.mstrAttId;
								String filePath = saveUrl + fileName;
								File file = new File(filePath);
								if (!file.exists() && !file.isDirectory()) {
									// Creates an output stream used to "write" to storage
									FileOutputStream outputStream;
									try {
										outputStream = new FileOutputStream(file);
										outputStream.write(attachment.getData());
										outputStream.close();
									} catch (Exception e1) {
										// Puff.
									}
								}
							}
						}
					}
				}
				MailConnector.clearAttsMap(existingUserEmail, messageData.mstrEmailID);
				MailConnector.clearStoredMessage(existingUserEmail, messageData.mstrEmailID);
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

	public String UndoDesc(String pstrLineBreak)
	{
		StringBuilder lstrResult;

		lstrResult = new StringBuilder("A informação será retirada.");

		if ( mobjData.mstrEmailID != null )
//		if ( mobjData.mbIsEmail )
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

		if ( mobjData.mstrEmailID != null )
//		if ( mobjData.mbIsEmail )
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
		UUID lidContainer;
		AgendaItem lobjNewAgendaItem;
		UUID lidUrgency;
		UUID[] larrUsers;
		int i;

		try
		{
			lobjConv = (Conversation)GetProcess().GetData();
			lidContainer = lobjConv.getParentContainer();
    		larrUsers = lobjConv.GetUsers(pdb);

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
			mobjData.mobjDocOps.UndoSubOp(pdb, lidContainer);
		if ( mobjData.mobjContactOps != null )
			mobjData.mobjContactOps.UndoSubOp(pdb, lidContainer);

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
			lobjConv.setAt(Conversation.I.DUEDATE, mdtPrevLimit);
			lobjConv.setAt(Conversation.I.PENDINGDIRECTION, midPrevDir);
			lobjConv.SaveToDb(pdb);

    		if ( Constants.MsgDir_Incoming.equals(midPrevDir) )
    			lidUrgency = Constants.UrgID_Valid;
    		else
    			lidUrgency = Constants.UrgID_Pending;

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

		if ( mobjData.mstrEmailID != null )
//		if ( mobjData.mbIsEmail )
		{
			try
			{
				MailConnector.DoUnprocessItem(mobjData.mstrEmailID);
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
