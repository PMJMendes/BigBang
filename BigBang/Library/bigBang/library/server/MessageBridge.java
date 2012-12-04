package bigBang.library.server;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.StringTokenizer;
import java.util.UUID;

import microsoft.exchange.webservices.data.EmailAddress;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.Item;
import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.MasterDB;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.IncomingMessage;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.OutgoingMessage;
import bigBang.library.shared.BigBangException;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.DocumentData;
import com.premiumminds.BigBang.Jewel.Data.IncomingMessageData;
import com.premiumminds.BigBang.Jewel.Data.MessageAddressData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.OutgoingMessageData;
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

	public static IncomingMessageData incomingToServer(IncomingMessage lobMessage, UUID pidDocOwnerType)
		throws BigBangException
	{
		IncomingMessageData lobjResult;
		int i;
		UUID lidFile;

		lobjResult = new IncomingMessageData();

		lobjResult.mstrSubject = null;
		lobjResult.mstrBody = lobMessage.notes;
		lobjResult.mstrEmailID = lobMessage.emailId;

		if ( lobMessage.upgrades == null )
			lobjResult.mobjDocOps = null;
		else
		{
			lobjResult.mobjDocOps = new DocOps();
			lobjResult.mobjDocOps.marrModify = null;
			lobjResult.mobjDocOps.marrDelete = null;
			lobjResult.mobjDocOps.marrCreate = new DocumentData[lobMessage.upgrades.length];
			for ( i = 0; i < lobMessage.upgrades.length; i++ )
			{
				lobjResult.mobjDocOps.marrCreate[i] = new DocumentData();
				lobjResult.mobjDocOps.marrCreate[i].mstrName = lobMessage.upgrades[i].name;
				lobjResult.mobjDocOps.marrCreate[i].midOwnerType = pidDocOwnerType;
				lobjResult.mobjDocOps.marrCreate[i].midOwnerId = null;
				lobjResult.mobjDocOps.marrCreate[i].midDocType = UUID.fromString(lobMessage.upgrades[i].docTypeId);
				lobjResult.mobjDocOps.marrCreate[i].mstrText = null;

				if ( lobMessage.upgrades[i].storageId != null )
				{
					lidFile = lobMessage.upgrades[i].storageId == null ? null : UUID.fromString(lobMessage.upgrades[i].storageId);
					lobjResult.mobjDocOps.marrCreate[i].mobjFile = FileServiceImpl.GetFileXferStorage().get(lidFile).GetVarData();
					FileServiceImpl.GetFileXferStorage().remove(lidFile);
				}
				else if ( lobMessage.upgrades[i].attachmentId != null )
				{
					try
					{
						lobjResult.mobjDocOps.marrCreate[i].mobjFile = MailConnector.DoGetAttachment(lobMessage.emailId,
								lobMessage.upgrades[i].attachmentId).GetVarData();
					}
					catch (Throwable e)
					{
						throw new BigBangException(e.getMessage(), e);
					}
				}

				lobjResult.mobjDocOps.marrCreate[i].marrInfo = null;
				lobjResult.mobjDocOps.marrCreate[i].mobjPrevValues = null;
			}
		}

		return lobjResult;
	}

	public static MessageData clientToServer(Message pobjMessage)
		throws BigBangException
	{
		MessageData lobjResult;
		Item lobjItem;
		EmailAddress lobjFrom;
		UUID lidFile;
		int i;

		lobjResult = new MessageData();

		lobjResult.mid = ( pobjMessage.id == null ? null : UUID.fromString(pobjMessage.id) );

		lobjResult.midOwner = UUID.fromString(pobjMessage.conversationId);
		lobjResult.mlngNumber = ( pobjMessage.order == null ? -1 : pobjMessage.order );
		lobjResult.mdtDate = ( pobjMessage.date == null ? new Timestamp(new java.util.Date().getTime()) :
				Timestamp.valueOf(pobjMessage.date + " 00:00:00.0") );

		lobjResult.mbIsEmail = Message.Kind.EMAIL.equals(pobjMessage.kind);
		lobjResult.midDirection = ( Conversation.Direction.OUTGOING.equals(pobjMessage.direction) ?
				Constants.MsgDir_Outgoing : Constants.MsgDir_Incoming );
		if ( lobjResult.mbIsEmail && Constants.MsgDir_Incoming.equals(lobjResult.midDirection) )
		{
			lobjResult.mstrEmailID = pobjMessage.emailId;
			if ( lobjResult.mstrEmailID == null )
			{
				lobjResult.mbIsEmail = false;
				lobjResult.marrAddresses = null;
			}
			else
			{
				lobjFrom = null;
				try
				{
					lobjItem = MailConnector.DoGetItem(lobjResult.mstrEmailID);
					lobjResult.mstrSubject = lobjItem.getSubject();
					lobjResult.mstrBody = lobjItem.getBody().toString();
					if ( lobjItem instanceof EmailMessage )
					{
						lobjFrom = ((EmailMessage)lobjItem).getFrom();
						lobjResult.marrAddresses = new MessageAddressData[1];
						lobjResult.marrAddresses[0] = new MessageAddressData();
						lobjResult.marrAddresses[0].mid = null;
						lobjResult.marrAddresses[0].mstrAddress = lobjFrom.getAddress();
						lobjResult.marrAddresses[0].midOwner = lobjResult.mid;
						lobjResult.marrAddresses[0].midUsage = Constants.UsageID_From;
						lobjResult.marrAddresses[0].midUser = null;
						lobjResult.marrAddresses[0].midInfo = null;
						lobjResult.marrAddresses[0].mstrDisplay = lobjFrom.getName();
					}
					else
						lobjResult.marrAddresses = null;
				}
				catch (Throwable e)
				{
					throw new BigBangException(e.getMessage(), e);
				}
			}
		}
		else
		{
			lobjResult.mstrEmailID = null;
			lobjResult.mstrSubject = pobjMessage.subject;
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
					lobjResult.marrAddresses[i].mstrDisplay = pobjMessage.addresses[i].display;
				}
			}
		}

		if ( Constants.MsgDir_Incoming.equals(lobjResult.midDirection) )
		{
			lobjResult.marrAttachments = null;

			if ( !lobjResult.mbIsEmail || (pobjMessage.incomingAttachments == null) )
				lobjResult.mobjDocOps = null;
			else
			{
				lobjResult.mobjDocOps = new DocOps();
				lobjResult.mobjDocOps.marrModify = null;
				lobjResult.mobjDocOps.marrDelete = null;
				lobjResult.mobjDocOps.marrCreate = new DocumentData[pobjMessage.incomingAttachments.length];
				for ( i = 0; i < pobjMessage.incomingAttachments.length; i++ )
				{
					lobjResult.mobjDocOps.marrCreate[i] = new DocumentData();
					lobjResult.mobjDocOps.marrCreate[i].mstrName = pobjMessage.incomingAttachments[i].name;
					lobjResult.mobjDocOps.marrCreate[i].midOwnerType = null;
					lobjResult.mobjDocOps.marrCreate[i].midOwnerId = null;
					lobjResult.mobjDocOps.marrCreate[i].midDocType = UUID.fromString(pobjMessage.incomingAttachments[i].docTypeId);
					lobjResult.mobjDocOps.marrCreate[i].mstrText = null;

					if ( pobjMessage.incomingAttachments[i].storageId != null )
					{
						lidFile = pobjMessage.incomingAttachments[i].storageId == null ? null :
								UUID.fromString(pobjMessage.incomingAttachments[i].storageId);
						lobjResult.mobjDocOps.marrCreate[i].mobjFile = FileServiceImpl.GetFileXferStorage().get(lidFile).GetVarData();
						FileServiceImpl.GetFileXferStorage().remove(lidFile);
					}
					else if ( pobjMessage.incomingAttachments[i].attachmentId != null )
					{
						try
						{
							lobjResult.mobjDocOps.marrCreate[i].mobjFile = MailConnector.DoGetAttachment(lobjResult.mstrEmailID,
									pobjMessage.incomingAttachments[i].attachmentId).GetVarData();
						}
						catch (Throwable e)
						{
							throw new BigBangException(e.getMessage(), e);
						}
					}

					lobjResult.mobjDocOps.marrCreate[i].marrInfo = null;
					lobjResult.mobjDocOps.marrCreate[i].mobjPrevValues = null;
				}
			}
		}
		else
		{
			lobjResult.mobjDocOps = null;

			if ( pobjMessage.outgoingAttachmentIds == null )
				lobjResult.marrAttachments = null;
			else
			{
				lobjResult.marrAttachments = new UUID[pobjMessage.outgoingAttachmentIds.length];
				for ( i = 0; i < lobjResult.marrAttachments.length; i++ )
					lobjResult.marrAttachments[i] = UUID.fromString(pobjMessage.outgoingAttachmentIds[i]);
			}
		}

		lobjResult.mobjContactOps = null;

		return lobjResult;
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
