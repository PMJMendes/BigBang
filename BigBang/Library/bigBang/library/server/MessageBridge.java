package bigBang.library.server;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.mail.internet.MimeUtility;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.ObjectBase;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.Message.Attachment;
import bigBang.definitions.shared.Message.MsgAddress;
import bigBang.definitions.shared.OutgoingMessage;
import bigBang.library.shared.BigBangException;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.ContactData;
import com.premiumminds.BigBang.Jewel.Data.ContactInfoData;
import com.premiumminds.BigBang.Jewel.Data.DocDataLight;
import com.premiumminds.BigBang.Jewel.Data.MessageAddressData;
import com.premiumminds.BigBang.Jewel.Data.MessageAttachmentData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.OutgoingMessageData;
import com.premiumminds.BigBang.Jewel.Objects.Casualty;
import com.premiumminds.BigBang.Jewel.Objects.Client;
import com.premiumminds.BigBang.Jewel.Objects.Contact;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.Expense;
import com.premiumminds.BigBang.Jewel.Objects.Negotiation;
import com.premiumminds.BigBang.Jewel.Objects.Policy;
import com.premiumminds.BigBang.Jewel.Objects.QuoteRequest;
import com.premiumminds.BigBang.Jewel.Objects.Receipt;
import com.premiumminds.BigBang.Jewel.Objects.SubCasualty;
import com.premiumminds.BigBang.Jewel.Objects.SubPolicy;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.Operations.ContactOps;
import com.premiumminds.BigBang.Jewel.Operations.DocOps;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class MessageBridge
{
	public static OutgoingMessageData outgoingToServer(OutgoingMessage pobjMessage)
		throws BigBangException
	{
		OutgoingMessageData lobjResult;
		MasterDB ldb;
		IEntity lrefUsers;
		ResultSet lrs;
		StringTokenizer lstrTok;
		int i;

		lobjResult = new OutgoingMessageData();

		if ( pobjMessage.forwardUserFullNames == null )
			lobjResult.marrUsers = new UUID[] {Engine.getCurrentUser()};
		else
		{
			try
			{
				lrefUsers = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(),
						Jewel.Engine.Constants.ObjectGUIDs.O_User));
				ldb = new MasterDB();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}

			lobjResult.marrUsers = new UUID[pobjMessage.forwardUserFullNames.length + 1];
			lobjResult.marrUsers[0] = Engine.getCurrentUser();

			for ( i = 0; i < pobjMessage.forwardUserFullNames.length; i++ )
			{
				try
				{
					lrs = lrefUsers.SelectByMembers(ldb, new int[] {0}, new java.lang.Object[] {pobjMessage.forwardUserFullNames[i]},
							new int[] {1});
				}
				catch (Throwable e)
				{
					try { ldb.Disconnect(); } catch (Throwable e1) {}
					throw new BigBangException(e.getMessage(), e);
				}

				try
				{
					if ( lrs.next() )
						lobjResult.marrUsers[i + 1] = User.GetInstance(Engine.getCurrentNameSpace(), lrs).getKey();
					else
						throw new BigBangException("Erro: Utilizador não existente.");
				}
				catch (BigBangException e)
				{
					try { lrs.close(); } catch (Throwable e1) {}
					try { ldb.Disconnect(); } catch (Throwable e1) {}
					throw new BigBangException(e.getMessage(), e);
				}
				catch (Throwable e)
				{
					try { lrs.close(); } catch (Throwable e1) {}
					try { ldb.Disconnect(); } catch (Throwable e1) {}
					throw new BigBangException(e.getMessage(), e);
				}

				try
				{
					lrs.close();
				}
				catch (Throwable e)
				{
					try { ldb.Disconnect(); } catch (Throwable e1) {}
					throw new BigBangException(e.getMessage(), e);
				}
			}

			try
			{
				ldb.Disconnect();
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
		}

		if ( pobjMessage.toContactInfoId == null )
			lobjResult.marrContactInfos = null;
		else
			lobjResult.marrContactInfos = new UUID[] {UUID.fromString(pobjMessage.toContactInfoId)};

		if ( pobjMessage.externalCCs == null )
			lobjResult.marrCCs = null;
		else
		{
			lstrTok = new StringTokenizer(pobjMessage.externalCCs, ",;");
			lobjResult.marrCCs = new String[lstrTok.countTokens()];
			for ( i = 0; i < lobjResult.marrCCs.length; i++ )
				lobjResult.marrCCs[i] = lstrTok.nextToken();
		}

		if ( pobjMessage.internalBCCs == null )
			lobjResult.marrBCCs = null;
		else
		{
			lstrTok = new StringTokenizer(pobjMessage.internalBCCs, ",;");
			lobjResult.marrBCCs = new String[lstrTok.countTokens()];
			for ( i = 0; i < lobjResult.marrBCCs.length; i++ )
				lobjResult.marrBCCs[i] = lstrTok.nextToken();
		}

		lobjResult.mstrSubject = pobjMessage.subject;
		lobjResult.mstrBody = pobjMessage.text;

		if ( pobjMessage.attachmentIds == null )
			lobjResult.marrAttachments = null;
		else
		{
			lobjResult.marrAttachments = new UUID[pobjMessage.attachmentIds.length];
			for ( i = 0; i < lobjResult.marrAttachments.length; i++ )
				lobjResult.marrAttachments[i] = UUID.fromString(pobjMessage.attachmentIds[i]);
		}

		return lobjResult;
	}
	
	public static MessageData clientToServer(Message pobjMessage, UUID pidParentType, UUID pidParentID, 
			UUID pidDefaultDir, javax.mail.Message existingMailMessage)
		throws BigBangException
	{
		MessageData lobjResult;
		MessageData lobjAux;
		int i;

		lobjResult = new MessageData();

		lobjResult.mid = ( pobjMessage.id == null ? null : UUID.fromString(pobjMessage.id) );

		lobjResult.midOwner = ( pobjMessage.conversationId == null ? null : UUID.fromString(pobjMessage.conversationId) );
		lobjResult.mlngNumber = ( pobjMessage.order == null ? -1 : pobjMessage.order );
		lobjResult.mdtDate = ( pobjMessage.date == null ? new Timestamp(new java.util.Date().getTime()) :
				Timestamp.valueOf(pobjMessage.date + " 00:00:00.0") );

		lobjResult.mbIsEmail = Message.Kind.EMAIL.equals(pobjMessage.kind);
		lobjResult.midDirection = ( pobjMessage.direction == null ? pidDefaultDir :
				(ConversationStub.Direction.OUTGOING.equals(pobjMessage.direction) ? Constants.MsgDir_Outgoing : Constants.MsgDir_Incoming) );
		if ( lobjResult.mbIsEmail )
		{
			lobjResult.mstrFolderID = pobjMessage.folderId;
			lobjResult.mstrEmailID = pobjMessage.emailId;
			if ( lobjResult.mstrEmailID == null && lobjResult.midDirection.equals(Constants.MsgDir_Incoming ))
			{
				lobjResult.mbIsEmail = false;
				lobjResult.marrAddresses = null;
			}
			else
			{
				if (lobjResult.midDirection.equals(Constants.MsgDir_Incoming )) {
					try
					{
						if (existingMailMessage == null) {
							lobjAux = MailConnector.getAsData(lobjResult.mstrEmailID, lobjResult.mstrFolderID);
						} else {
							lobjAux = MailConnector.messageToData(existingMailMessage, lobjResult.mstrEmailID);
						}
					}
					catch (Throwable e)
					{
						throw new BigBangException(e.getMessage(), e);
					}
					lobjResult.mstrSubject = lobjAux.mstrSubject;
					lobjResult.marrAddresses = lobjAux.marrAddresses;
					lobjResult.mstrBody = lobjAux.mstrBody;
				} else {
					lobjResult.mstrSubject = pobjMessage.subject;
					lobjResult.mstrBody = pobjMessage.text;
					if(pobjMessage.addresses!=null) {
						MessageAddressData[] addresses = new MessageAddressData[pobjMessage.addresses.length];
						for (int u=0; u<pobjMessage.addresses.length; u++) {
							MessageAddressData tmpData = new MessageAddressData();
							MsgAddress tmpAddr = pobjMessage.addresses[u];
							tmpData.mid = tmpAddr.id==null ? null : UUID.fromString(tmpAddr.id); 
							tmpData.mstrAddress = tmpAddr.address==null ? tmpAddr.address : tmpAddr.address;
							tmpData.midOwner    = tmpAddr.ownerId==null ? null : UUID.fromString(tmpAddr.ownerId);
							tmpData.midUsage    = sGetUsage(tmpAddr.usage);
							tmpData.midUser     = tmpAddr.userId== null ? null : UUID.fromString(tmpAddr.userId);
							tmpData.midInfo     = tmpAddr.contactInfoId==null ? null : UUID.fromString(tmpAddr.contactInfoId);
							try {
								tmpData.mstrDisplay = MimeUtility.decodeText(tmpAddr.display);
							} catch (Throwable e) {
								throw new BigBangException(e.getMessage(), e);
							}
							addresses[u] = tmpData;
						}
						lobjResult.marrAddresses = addresses;
					}
				}
				if ( (lobjResult.mstrSubject != null) && (lobjResult.mstrSubject.length() > 250) )
					lobjResult.mstrSubject = lobjResult.mstrSubject.substring(0, 250);
			}
		}
		else
		{
			lobjResult.mstrEmailID = null;
			lobjResult.mstrFolderID = null;
			lobjResult.mstrSubject = ( pobjMessage.subject == null ?
					(pobjMessage.text.length() > 100 ? pobjMessage.text.substring(0, 100) : pobjMessage.text) : pobjMessage.subject );
			lobjResult.mstrBody = pobjMessage.text;

			if ( !lobjResult.mbIsEmail || (pobjMessage.addresses == null) )
				lobjResult.marrAddresses = null;
			else
			{
				lobjResult.marrAddresses = new MessageAddressData[pobjMessage.addresses.length];
				for ( i = 0; i < pobjMessage.addresses.length; i++ )
				{
					lobjResult.marrAddresses[i] = new MessageAddressData();
					lobjResult.marrAddresses[i].mid = ( pobjMessage.addresses[i].id == null ? null :
							UUID.fromString(pobjMessage.addresses[i].id) );
					lobjResult.marrAddresses[i].mstrAddress = pobjMessage.addresses[i].address;
					lobjResult.marrAddresses[i].midOwner = lobjResult.mid;
					lobjResult.marrAddresses[i].midUsage = sGetUsage(pobjMessage.addresses[i].usage);
					lobjResult.marrAddresses[i].midUser = ( pobjMessage.addresses[i].userId == null ? null :
							UUID.fromString(pobjMessage.addresses[i].userId) );
					lobjResult.marrAddresses[i].midInfo = ( pobjMessage.addresses[i].contactInfoId == null ? null :
						UUID.fromString(pobjMessage.addresses[i].contactInfoId) );
					try {
						lobjResult.marrAddresses[i].mstrDisplay = pobjMessage.addresses[i].display;
					} catch (Throwable e) {
						throw new BigBangException(e.getMessage(), e);
					}
				}
			}
		}

		Attachment[] promotedAttachments = pobjMessage.attachments;
		
		if ( !lobjResult.mbIsEmail || pobjMessage.attachments == null || pobjMessage.attachments.length==0 )
		{
			lobjResult.marrAttachments = null;
		}
		else
		{
			lobjResult.marrAttachments = new MessageAttachmentData[promotedAttachments.length];
			for ( i = 0; i < promotedAttachments.length; i++ )
			{
				lobjResult.marrAttachments[i] = new MessageAttachmentData();
				lobjResult.marrAttachments[i].mstrAttId = pobjMessage.attachments[i].attachmentId;
				if (promotedAttachments[i].promote) {
					lobjResult.marrAttachments[i].midDocument = ( pobjMessage.attachments[i].docId == null ? null :
						UUID.fromString(pobjMessage.attachments[i].docId) );
				} else {
					lobjResult.marrAttachments[i].midDocument = null;
				}
			}
		}

		lobjResult.mobjContactOps = handleAddresses(lobjResult.marrAddresses, pidParentType, pidParentID);
		lobjResult.mobjDocOps = handleAttachments(lobjResult, promotedAttachments, pidParentType, pidParentID);

		return lobjResult;
	}

	private static ContactOps handleAddresses(MessageAddressData[] parrAddresses, UUID pidParentType, UUID pidParentID)
		throws BigBangException
	{
		Contact[] larrContacts;
		ArrayList<ContactData> larrCreates;
		ArrayList<ContactData> larrModifies;
		int i, j, k;
		boolean b;
		UserDecoration lobjDeco;
		ContactInfo lobjInfo;
		String lstrAddress;
		String lstrName;
		ContactData lobjAux;
		Contact lobjContact;
		ContactInfo[] larrInfo;
		ContactOps lopResult;

		if ( parrAddresses == null )
			return null;

		try
		{
			larrContacts = getParentContacts(pidParentType, pidParentID);
			if ( larrContacts == null )
				return null;

			larrCreates = new ArrayList<ContactData>();
			larrModifies = new ArrayList<ContactData>();

			for ( i = 0; i < parrAddresses.length; i++ )
			{
				if ( (parrAddresses[i].mstrAddress == null) && (parrAddresses[i].mstrDisplay != null))
				{
					lobjDeco = UserDecoration.GetByFullName(Engine.getCurrentNameSpace(), parrAddresses[i].mstrDisplay);
					parrAddresses[i].mstrAddress = ( lobjDeco == null ? null : (String)lobjDeco.getAt(UserDecoration.I.EMAIL) );
					if ( parrAddresses[i].mstrAddress == null )
						throw new BigBangException("Erro: O utilizador indicado não tem email definido.");
					parrAddresses[i].midUser = ( lobjDeco == null ? null : lobjDeco.getBaseUser().getKey() );
					continue;
				}

				if ( parrAddresses[i].midUser != null )
				{
					lobjDeco = UserDecoration.GetByUserID(Engine.getCurrentNameSpace(), parrAddresses[i].midUser);
					if ( (parrAddresses[i].mstrAddress == null) || "".equals(parrAddresses[i].mstrAddress) )
						parrAddresses[i].mstrAddress = ( lobjDeco == null ? null : (String)lobjDeco.getAt(UserDecoration.I.EMAIL) );
					if ( parrAddresses[i].mstrAddress == null )
						throw new BigBangException("Erro: O utilizador indicado não tem email definido.");
					if ( (parrAddresses[i].mstrDisplay == null) || "".equals(parrAddresses[i].mstrDisplay) )
						parrAddresses[i].mstrDisplay = lobjDeco.getBaseUser().getDisplayName();
					continue;
				}

				if ( parrAddresses[i].midInfo != null )
				{
					lobjInfo = ContactInfo.GetInstance(Engine.getCurrentNameSpace(), parrAddresses[i].midInfo);
					if ( !Constants.CInfoID_Email.equals(lobjInfo.getAt(ContactInfo.I.TYPE)) )
						throw new BigBangException("Inesperado: Tipo de informação não é endereço de email.");
					if ( (parrAddresses[i].mstrAddress == null) || "".equals(parrAddresses[i].mstrAddress) )
						parrAddresses[i].mstrAddress = (String)lobjInfo.getAt(ContactInfo.I.VALUE);
					if ( (parrAddresses[i].mstrDisplay == null) || "".equals(parrAddresses[i].mstrDisplay) )
						parrAddresses[i].mstrDisplay = lobjInfo.getOwner().getLabel();
					continue;
				}

				if ( (parrAddresses[i].mstrAddress == null) || "".equals(parrAddresses[i].mstrAddress) )
					throw new BigBangException("Inesperado: Endereço de email vazio.");
				lstrAddress = parrAddresses[i].mstrAddress.trim();

				b = false;
				for ( j = 0; !b && j < larrContacts.length; j++ )
				{
					lobjInfo = larrContacts[j].findAddress(lstrAddress);
					if ( lobjInfo == null )
						continue;

					parrAddresses[i].midInfo = lobjInfo.getKey();
					if ( (parrAddresses[i].mstrDisplay == null) || "".equals(parrAddresses[i].mstrDisplay) )
						parrAddresses[i].mstrDisplay = lobjInfo.getOwner().getLabel();
					b = true;
				}
				if ( b )
					continue;

				if ( !Constants.UsageID_From.equals(parrAddresses[i].midUsage) )
					continue;

				if ( (parrAddresses[i].mstrDisplay == null) || ("".equals(parrAddresses[i].mstrDisplay.trim())) )
					continue;
				lstrName = parrAddresses[i].mstrDisplay.trim();

				lobjAux = new ContactData();

				for ( j = 0; !b && j < larrContacts.length; j++ )
				{
					lobjContact = larrContacts[j].findName(lstrName);
					if ( lobjContact == null )
						continue;

					lobjAux.FromObject(lobjContact);
					larrInfo = larrContacts[j].getCurrentInfo();
					lobjAux.marrInfo = new ContactInfoData[larrInfo.length + 1];
					for ( k = 0; k < larrInfo.length; k++ )
					{
						lobjAux.marrInfo[k] = new ContactInfoData();
						lobjAux.marrInfo[k].FromObject(larrInfo[k]);
					}
					lobjAux.marrInfo[k] = new ContactInfoData();
					lobjAux.marrInfo[k].midOwner = lobjAux.mid;
					lobjAux.marrInfo[k].midType = Constants.CInfoID_Email;
					lobjAux.marrInfo[k].mstrValue = lstrAddress;
					larrModifies.add(lobjAux);
					b = true;
				}
				if ( b )
					continue;

				lobjAux.mstrName = lstrName;
				lobjAux.midOwnerType = pidParentType;
				lobjAux.midOwnerId = pidParentID;
				lobjAux.mstrAddress1 = null;
				lobjAux.mstrAddress2 = null;
				lobjAux.midZipCode = null;
				lobjAux.midContactType = Constants.CtTypeID_General;
				lobjAux.marrSubContacts = null;
				lobjAux.marrInfo = new ContactInfoData[] { new ContactInfoData() };
				lobjAux.marrInfo[0].midOwner = null;
				lobjAux.marrInfo[0].midType = Constants.CInfoID_Email;
				lobjAux.marrInfo[0].mstrValue = lstrAddress;
				larrCreates.add(lobjAux);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		if ( (larrModifies.size() == 0) && (larrCreates.size() == 0) )
			return null;

		lopResult = new ContactOps();
		lopResult.marrCreate = ( larrCreates.size() == 0 ? null : larrCreates.toArray(new ContactData[larrCreates.size()]) );
		lopResult.marrModify = ( larrModifies.size() == 0 ? null : larrModifies.toArray(new ContactData[larrModifies.size()]) );
		lopResult.marrDelete = null;

		return lopResult;
	}

	private static DocOps handleAttachments(MessageData pobjMsg, Message.Attachment[] parrAttachments, UUID pidParentType, UUID pidParentID)
		throws BigBangException
	{
		DocOps lobjResult;
		UUID lidFile;
		int i, j;

		if ( !pobjMsg.mbIsEmail || (parrAttachments == null) )
		{
			lobjResult = null;
		}
		else
		{
			j = 0;
			for ( i = 0; i < parrAttachments.length; i++ )
			{
				if ( parrAttachments[i].promote )
					j++;
			}

			if ( j == 0 )
			{
				lobjResult = null;
			}
			else
			{
				lobjResult = new DocOps();
				lobjResult.marrModify2 = null;
				lobjResult.marrDelete2 = null;
				lobjResult.marrCreate2 = new DocDataLight[j];
				j = 0;
				for ( i = 0; i < parrAttachments.length; i++ )
				{
					if ( parrAttachments[i].promote )
					{
						lobjResult.marrCreate2[j] = new DocDataLight();
						lobjResult.marrCreate2[j].mstrName = parrAttachments[i].name;
						lobjResult.marrCreate2[j].midOwnerType = pidParentType;
						lobjResult.marrCreate2[j].midOwnerId = pidParentID;
						lobjResult.marrCreate2[j].midDocType = UUID.fromString(parrAttachments[i].docTypeId);
						lobjResult.marrCreate2[j].mstrText = null;

						if ( parrAttachments[i].storageId != null )
						{
							lidFile = UUID.fromString(parrAttachments[i].storageId);
							lobjResult.marrCreate2[j].mobjFile = FileServiceImpl.GetFileXferStorage().get(lidFile).GetVarData();
							FileServiceImpl.GetFileXferStorage().remove(lidFile);
						}
						else if ( parrAttachments[i].attachmentId != null )
						{
							try
							{
								lobjResult.marrCreate2[j].mobjFile = MailConnector.getAttachment(pobjMsg.mstrEmailID,
										pobjMsg.mstrFolderID, parrAttachments[i].attachmentId).GetVarData();
							}
							catch (Throwable e)
							{
								throw new BigBangException(e.getMessage(), e);
							}
						}

						lobjResult.marrCreate2[j].marrInfo = null;

						j++;
					}
				}
			}
		}

		return lobjResult;
	}

	private static Contact[] getParentContacts(UUID pidParentType, UUID pidParentID)
		throws BigBangJewelException
	{
		ObjectBase lobjAux;

		try
		{
			lobjAux = Engine.GetWorkInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), pidParentType), pidParentID);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if ( lobjAux instanceof Client )
			return ((Client)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof QuoteRequest )
			return ((QuoteRequest)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof Negotiation )
			return ((Negotiation)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof Policy )
			return ((Policy)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof SubPolicy )
			return ((SubPolicy)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof Receipt )
			return ((Receipt)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof Casualty )
			return ((Casualty)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof SubCasualty )
			return ((SubCasualty)lobjAux).GetCurrentContacts();

		if ( lobjAux instanceof Expense )
			return ((Expense)lobjAux).GetCurrentContacts();

		return null;
	}

	private static UUID sGetUsage(Message.MsgAddress.Usage pobjUsage)
	{
		if ( Message.MsgAddress.Usage.FROM.equals(pobjUsage) )
			return Constants.UsageID_From;
		if ( Message.MsgAddress.Usage.TO.equals(pobjUsage) )
			return Constants.UsageID_To;
		if ( Message.MsgAddress.Usage.REPLYTO.equals(pobjUsage) )
			return Constants.UsageID_ReplyTo;
		if ( Message.MsgAddress.Usage.CC.equals(pobjUsage) )
			return Constants.UsageID_CC;
		if ( Message.MsgAddress.Usage.BCC.equals(pobjUsage) )
			return Constants.UsageID_BCC;
		return null;
	}
}
