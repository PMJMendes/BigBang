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
		Timestamp ldtNow;
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

		if ( mobjData.marrMessages[0].mobjDocOps != null )
			mobjData.marrMessages[0].mobjDocOps.RunSubOp(pdb, GetProcess().GetDataKey());
		if ( mobjData.marrMessages[0].mobjContactOps != null )
			mobjData.marrMessages[0].mobjContactOps.RunSubOp(pdb, GetProcess().GetDataKey());

		if ( mobjData.mdtDueDate == null )
			mobjData.midPendingDir = null;
		else
		{
			ldtNow = new Timestamp(new java.util.Date().getTime());

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

			larrUsers = new HashSet<UUID>();
			larrUsers.add(Engine.getCurrentUser());
			for ( i = 0; i < mobjData.marrMessages[0].marrAddresses.length; i++ )
			{
				if ( mobjData.marrMessages[0].marrAddresses[i].midUser != null )
					larrUsers.add(mobjData.marrMessages[0].marrAddresses[i].midUser);
			}

	    	for ( UUID lidUser : larrUsers )
	    	{
				try
				{
					lobjAgendaItem = AgendaItem.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
					lobjAgendaItem.setAt(0, "Troca de Mensagens");
					lobjAgendaItem.setAt(1, lidUser);
					lobjAgendaItem.setAt(2, Constants.ProcID_Conversation);
					lobjAgendaItem.setAt(3, ldtNow);
					lobjAgendaItem.setAt(4, mobjData.mdtDueDate);
					lobjAgendaItem.setAt(5, lidUrgency);
					lobjAgendaItem.SaveToDb(pdb);
					lobjAgendaItem.InitNew(new UUID[] {GetProcess().getKey()}, new UUID[] {Constants.OPID_Conversation_SendMessage,
							Constants.OPID_Conversation_ReSendMessage, Constants.OPID_Conversation_ReceiveMessage,
							Constants.OPID_Conversation_CloseProcess}, pdb);
				}
				catch (Throwable e)
				{
					throw new JewelPetriException(e.getMessage(), e);
				}
	    	}
		}

		try
		{
			lobjConv = Conversation.GetInstance(Engine.getCurrentNameSpace(), (UUID)null);
			mobjData.ToObject(lobjConv);
			lobjConv.SaveToDb(pdb);
			mobjData.mid = lobjConv.getKey();

			if ( (mobjData.marrMessages != null) && (mobjData.marrMessages.length > 0) )
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

			lobjScript = PNScript.GetInstance(Engine.getCurrentNameSpace(), Constants.ProcID_Conversation);
			lobjProc = lobjScript.CreateInstance(Engine.getCurrentNameSpace(), lobjConv.getKey(), GetProcess().getKey(),
					GetContext(), pdb);

			mobjData.midProcess = lobjProc.getKey();
		}
		catch (Throwable e)
		{
			throw new JewelPetriException(e.getMessage(), e);
		}

		if ( mobjData.mdtDueDate == null )
			TriggerOp(new TriggerAutoCloseProcess(mobjData.midProcess), pdb);
		else
		{
			if ( Constants.MsgDir_Outgoing.equals(mobjData.midStartDir) )
				TriggerOp(new ExternInitialSend(mobjData.midProcess), pdb);
		}

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

//	private UUID getInfoFromEmail(EmailAddress pobjAddress, SQLServer pdb)
//		throws BigBangJewelException, JewelPetriException
//	{
//		String lstrAddress;
//		String lstrName;
//		Contact[] larrContacts;
//		ContactInfo[] larrInfo;
//		int i, j;
//
//		if ( pobjAddress == null )
//			return null;
//
//		lstrAddress = pobjAddress.getAddress();
//		if ( (lstrAddress == null) || ("".equals(lstrAddress.trim())) )
//			return null;
//		lstrAddress = lstrAddress.trim();
//
//		lstrName = pobjAddress.getName();
//		if ( (lstrName == null) || ("".equals(lstrName.trim())) )
//			return null;
//		lstrName = lstrName.trim();
//
//		larrContacts = getParentContacts();
//		if ( larrContacts == null )
//			return null;
//
//		for ( i = 0; i < larrContacts.length; i++ )
//		{
//			larrInfo = larrContacts[i].getCurrentInfo(pdb);
//			for ( j = 0; j < larrInfo.length; j++ )
//			{
//				if ( Constants.CInfoID_Email.equals(larrInfo[j].getAt(ContactInfo.I.TYPE)) &&
//						(lstrAddress.equals(larrInfo[j].getAt(ContactInfo.I.VALUE))) )
//					return larrInfo[j].getKey();
//			}
//		}
//
//		mobjContactOps = new ContactOps();
//		mobjContactOps.marrDelete = null;
//
//		for ( i = 0; i < larrContacts.length; i++ )
//		{
//			if ( lstrName.equals(larrContacts[i].getLabel()) )
//			{
//				mobjContactOps.marrCreate = null;
//				mobjContactOps.marrModify = new ContactData[] {new ContactData()};
//				mobjContactOps.marrModify[0].FromObject(larrContacts[i]);
//				larrInfo = larrContacts[i].getCurrentInfo(pdb);
//				mobjContactOps.marrModify[0].marrInfo = new ContactInfoData[larrInfo.length + 1];
//				for ( j = 0; j < larrInfo.length; j++ )
//				{
//					mobjContactOps.marrModify[0].marrInfo[j] = new ContactInfoData();
//					mobjContactOps.marrModify[0].marrInfo[j].FromObject(larrInfo[j]);
//				}
//				mobjContactOps.marrModify[0].marrInfo[j] = new ContactInfoData();
//				mobjContactOps.marrModify[0].marrInfo[j].midOwner = mobjContactOps.marrModify[0].mid;
//				mobjContactOps.marrModify[0].marrInfo[j].midType = Constants.CInfoID_Email;
//				mobjContactOps.marrModify[0].marrInfo[j].mstrValue = lstrAddress;
//
//				mobjContactOps.RunSubOp(pdb, GetProcess().GetDataKey());
//
//				return mobjContactOps.marrModify[0].marrInfo[j].mid;
//			}
//		}
//
//		mobjContactOps.marrModify = null;
//		mobjContactOps.marrCreate = new ContactData[] {new ContactData()};
//		mobjContactOps.marrCreate[0].mstrName = lstrName;
//		mobjContactOps.marrCreate[0].midOwnerType = GetProcess().GetScript().GetDataType();
//		mobjContactOps.marrCreate[0].midOwnerId = GetProcess().GetDataKey();
//		mobjContactOps.marrCreate[0].mstrAddress1 = null;
//		mobjContactOps.marrCreate[0].mstrAddress2 = null;
//		mobjContactOps.marrCreate[0].midZipCode = null;
//		mobjContactOps.marrCreate[0].midContactType = Constants.CtTypeID_General;
//		mobjContactOps.marrCreate[0].marrSubContacts = null;
//		mobjContactOps.marrCreate[0].marrInfo = new ContactInfoData[] { new ContactInfoData() };
//		mobjContactOps.marrCreate[0].marrInfo[0].midOwner = null;
//		mobjContactOps.marrCreate[0].marrInfo[0].midType = Constants.CInfoID_Email;
//		mobjContactOps.marrCreate[0].marrInfo[0].mstrValue = lstrAddress;
//
//		mobjContactOps.RunSubOp(pdb, GetProcess().GetDataKey());
//
//		return mobjContactOps.marrCreate[0].marrInfo[0].mid;
//	}
//
//	private Contact[] getParentContacts()
//		throws BigBangJewelException
//	{
//		ObjectBase lobjAux;
//
//		try
//		{
//			lobjAux = GetProcess().GetData();
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
//
//		if ( lobjAux instanceof Client )
//			return ((Client)lobjAux).GetCurrentContacts();
//
//		if ( lobjAux instanceof QuoteRequest )
//			return ((QuoteRequest)lobjAux).GetCurrentContacts();
//
//		if ( lobjAux instanceof Negotiation )
//			return ((Negotiation)lobjAux).GetCurrentContacts();
//
//		if ( lobjAux instanceof Policy )
//			return ((Policy)lobjAux).GetCurrentContacts();
//
//		if ( lobjAux instanceof SubPolicy )
//			return ((SubPolicy)lobjAux).GetCurrentContacts();
//
//		if ( lobjAux instanceof Receipt )
//			return ((Receipt)lobjAux).GetCurrentContacts();
//
//		if ( lobjAux instanceof Casualty )
//			return ((Casualty)lobjAux).GetCurrentContacts();
//
//		if ( lobjAux instanceof SubCasualty )
//			return ((SubCasualty)lobjAux).GetCurrentContacts();
//
//		if ( lobjAux instanceof Expense )
//			return ((Expense)lobjAux).GetCurrentContacts();
//
//		return null;
//	}
}
