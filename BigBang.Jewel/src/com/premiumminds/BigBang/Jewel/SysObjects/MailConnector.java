package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import microsoft.exchange.webservices.data.Attachment;
import microsoft.exchange.webservices.data.BasePropertySet;
import microsoft.exchange.webservices.data.EmailAddress;
import microsoft.exchange.webservices.data.EmailAddressCollection;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.FileAttachment;
import microsoft.exchange.webservices.data.Folder;
import microsoft.exchange.webservices.data.FolderView;
import microsoft.exchange.webservices.data.Item;
import microsoft.exchange.webservices.data.ItemId;
import microsoft.exchange.webservices.data.ItemSchema;
import microsoft.exchange.webservices.data.ItemView;
import microsoft.exchange.webservices.data.PropertySet;
import microsoft.exchange.webservices.data.SortDirection;
import microsoft.exchange.webservices.data.WellKnownFolderName;
import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.MessageAddressData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.OutgoingMessageData;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;

public class MailConnector
{
	private static Session GetMailSession()
	{
		return (Session)Engine.getUserData().get("MailSession");
	}

//	private static Store GetMailStore()
//		throws BigBangJewelException
//	{
//		Session lsession;
//		Store lstore;
//
//		lsession = GetMailSession();
//		try
//		{
//			lstore = lsession.getStore("imaps");
//			lstore.connect();
//		}
//		catch (Throwable e)
//		{
//			throw new BigBangJewelException(e.getMessage(), e);
//		}
//
//		return lstore;
//	}

	private static Transport GetMailTransport()
		throws BigBangJewelException
	{
		Session lsession;
		Transport lxport;

		lsession = GetMailSession();
		try
		{
			lxport = lsession.getTransport("smtp");
			lxport.connect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lxport;
	}

	private static ExchangeService GetService()
	{
		return (ExchangeService)Engine.getUserData().get("MailService");
	}

	private static Folder GetFolder()
		throws BigBangJewelException
	{
		Folder lobjFolder;

		lobjFolder = (Folder)Engine.getUserData().get("ExchangeFolder");

		if ( lobjFolder == null )
		{
			try
			{
				lobjFolder = Folder.bind(GetService(), WellKnownFolderName.Inbox);
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			Engine.getUserData().put("ExchangeFolder", lobjFolder);
		}

		return lobjFolder;
	}

	private static Folder GetBigBangFolder()
		throws BigBangJewelException
	{
		Folder lobjFolder;
		ArrayList<Folder> larrFolders;
		int i;

		lobjFolder = null;

		try
		{
			larrFolders = GetService().findFolders(WellKnownFolderName.MsgFolderRoot,
					new FolderView(Integer.MAX_VALUE)).getFolders();

			for ( i = 0; i < larrFolders.size(); i++ )
			{
				if ( "bigbang".equals(larrFolders.get(i).getDisplayName()) )
				{
					lobjFolder = larrFolders.get(i);
					break;
				}
			}

			if ( lobjFolder == null )
			{
				lobjFolder = new Folder(GetService());
				lobjFolder.setDisplayName("bigbang");
				lobjFolder.save(WellKnownFolderName.MsgFolderRoot);
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lobjFolder;
	}

	private static Folder GetProcessedFolder()
		throws BigBangJewelException
	{
		Folder lobjParent;
		Folder lobjFolder;
		ArrayList<Folder> larrFolders;
		int i;

		lobjFolder = (Folder)Engine.getUserData().get("ExchangeProcessedFolder");

		if ( lobjFolder == null )
		{
			lobjParent = GetBigBangFolder();

			try
			{
				larrFolders = GetService().findFolders(lobjParent.getId(), new FolderView(Integer.MAX_VALUE)).getFolders();
	
				for ( i = 0; i < larrFolders.size(); i++ )
				{
					if ( "tratados".equals(larrFolders.get(i).getDisplayName()) )
					{
						lobjFolder = larrFolders.get(i);
						break;
					}
				}

				if ( lobjFolder == null )
				{
					lobjFolder = new Folder(GetService());
					lobjFolder.setDisplayName("tratados");
					lobjFolder.save(lobjParent.getId());
				}
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}

			Engine.getUserData().put("ExchangeProcessedFolder", lobjFolder);
		}

		return lobjFolder;
	}

	public static void DoSendMail(OutgoingMessageData pobjMessage, SQLServer pdb)
		throws BigBangJewelException
	{
		String[] larrTos;
        ResultSet lrs;
		IEntity lrefDecos;
		String[] larrReplyTos;
		FileXfer[] larrFiles;
		Document lobjDoc;
		int i;

		if ( pobjMessage.marrContactInfos == null )
			larrTos = null;
		else
		{
			larrTos = new String[pobjMessage.marrContactInfos.length];
			for ( i = 0; i < pobjMessage.marrContactInfos.length; i++ )
			{
				larrTos[i] = (String)ContactInfo.GetInstance(Engine.getCurrentNameSpace(),
						pobjMessage.marrContactInfos[i]).getAt(2);
			}
		}

		if ( larrTos != null )
		{
	        lrs = null;
			try
			{
				lrefDecos = Entity.GetInstance(Engine.FindEntity(Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
				larrReplyTos = new String[pobjMessage.marrUsers.length];
				for ( i = 0; i < pobjMessage.marrUsers.length; i++ )
				{
					lrs = lrefDecos.SelectByMembers(pdb, new int[] {0}, new java.lang.Object[] {pobjMessage.marrUsers[i]}, new int[0]);
				    if (lrs.next())
				    	larrReplyTos[i] = (String)UserDecoration.GetInstance(Engine.getCurrentNameSpace(), lrs).getAt(1);
				    else
				    	larrReplyTos[i] = null;
				    lrs.close();
				}
			}
			catch (Throwable e)
			{
				if ( lrs != null ) try { lrs.close(); } catch (Throwable e1) {}
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if ( pobjMessage.marrAttachments == null )
				larrFiles = null;
			else
			{
				larrFiles = new FileXfer[pobjMessage.marrAttachments.length];
				for ( i = 0; i < larrFiles.length; i++ )
				{
					lobjDoc = Document.GetInstance(Engine.getCurrentNameSpace(), pobjMessage.marrAttachments[i]);
					larrFiles[i] = lobjDoc.getFile();
				}
			}
			DoSendMail(larrReplyTos, larrTos, pobjMessage.marrCCs, pobjMessage.marrBCCs,
					pobjMessage.mstrSubject, pobjMessage.mstrBody, larrFiles);
		}
	}

	public static void DoSendMail(String[] parrReplyTo, String[] parrTo, String[] parrCC, String[] parrBCC,
			String pstrSubject, String pstrBody, FileXfer[] parrAttachments)
		throws BigBangJewelException
	{
		Session lsession;
		Transport lxport;
		MimeMessage lmsg;
		Address[] larrAddr;
		MimeMultipart lmpMessage;
		MimeBodyPart lbp;
		int i;

		lsession = GetMailSession();
		lxport = GetMailTransport();

		lmsg = new MimeMessage(lsession);
		try
		{
			lmsg.setFrom(InternetAddress.getLocalAddress(lsession));
			if ( parrReplyTo != null )
			{
				larrAddr = new Address[parrReplyTo.length];
				for ( i = 0; i < parrReplyTo.length; i++ )
					if ( parrReplyTo[i] != null )
						larrAddr[i] = new InternetAddress(parrReplyTo[i]);
				lmsg.setReplyTo(larrAddr);
			}
			for ( i = 0; i < parrTo.length; i++ )
				lmsg.addRecipient(Message.RecipientType.TO, new InternetAddress(parrTo[i]));
			if ( parrCC != null )
				for ( i = 0; i < parrCC.length; i++ )
					lmsg.addRecipient(Message.RecipientType.CC, new InternetAddress(parrCC[i]));
			if ( parrBCC != null )
				for ( i = 0; i < parrBCC.length; i++ )
					lmsg.addRecipient(Message.RecipientType.BCC, new InternetAddress(parrBCC[i]));
			lmsg.setSubject(pstrSubject);

			if ( (parrAttachments == null) || (parrAttachments.length == 0) )
			{
				lmsg.setText(pstrBody, "UTF-8");
				lmsg.addHeader("Content-Type", "text/html");
			}
			else
			{
				lmpMessage = new MimeMultipart();

				lbp = new MimeBodyPart();
				lbp.setText(pstrBody, "UTF-8");
				lmpMessage.addBodyPart(lbp);

				for ( i = 0; i < parrAttachments.length; i++ )
				{
					if ( parrAttachments[i] == null )
						continue;

					lbp = new MimeBodyPart();
					lbp.setDataHandler(new DataHandler(new ByteArrayDataSource(parrAttachments[i].getData(),
							parrAttachments[i].getContentType())));
					lbp.setFileName(parrAttachments[i].getFileName());
					lmpMessage.addBodyPart(lbp);
				}

				lmsg.setContent(lmpMessage);
				lmsg.addHeader("Content-Type", lmpMessage.getContentType());
			}

			lmsg.addHeader("MIME-Version", "1.0");
			lmsg.saveChanges();
			lxport.sendMessage(lmsg, lmsg.getAllRecipients());
			lxport.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static Item[] DoGetMail()
		throws BigBangJewelException
	{
		Folder lobjFolder;
		ItemView lobjView;
		ArrayList<Item> larrTmp;

		lobjFolder = GetFolder();

		if ( lobjFolder == null )
			return null;

		lobjView = new ItemView(30);

		try
		{
			lobjView.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Descending);
			larrTmp = GetService().findItems(lobjFolder.getId(), lobjView).getItems();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrTmp.toArray(new Item[larrTmp.size()]);
	}

	public static Item[] DoGetMailAll()
		throws BigBangJewelException
	{
		Folder lobjFolder;
		ItemView lobjView;
		ArrayList<Item> larrTmp;

		lobjFolder = GetFolder();

		if ( lobjFolder == null )
			return null;

		lobjView = new ItemView(Integer.MAX_VALUE);

		try
		{
			lobjView.getOrderBy().add(ItemSchema.DateTimeReceived, SortDirection.Descending);
			larrTmp = GetService().findItems(lobjFolder.getId(), lobjView).getItems();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return larrTmp.toArray(new Item[larrTmp.size()]);
	}

	public static Item DoGetItem(String pstrUniqueID)
		throws BigBangJewelException
	{
		try
		{
			return Item.bind(GetService(), new ItemId(pstrUniqueID), new PropertySet(BasePropertySet.FirstClassProperties/*,
					ItemSchema.Id, ItemSchema.Subject,
					ItemSchema.Body, EmailMessageSchema.From, ItemSchema.DateTimeSent, ItemSchema.Attachments,
					ItemSchema.MimeContent*/));
		}
		catch (Exception e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static Item DoProcessItem(String pstrUniqueID)
		throws BigBangJewelException
	{
		try
		{
			return Item.bind(GetService(), new ItemId(pstrUniqueID)).move(GetProcessedFolder().getId());
		}
		catch (BigBangJewelException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static Item DoUnprocessItem(String pstrUniqueID)
		throws BigBangJewelException
	{
		try
		{
			return Item.bind(GetService(), new ItemId(pstrUniqueID)).move(GetFolder().getId());
		}
		catch (BigBangJewelException e)
		{
			throw e;
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static FileXfer DoGetAttachment(String pstrEmailId, String pstrAttachmentId)
		throws BigBangJewelException
	{
		Item lobjItem;
		byte[] larrBytes;
		FileXfer lobjFile;

		try
		{
			lobjItem = DoGetItem(pstrEmailId);
			lobjItem.load();

			for ( Attachment lobjAtt: lobjItem.getAttachments() )
			{
				if ( !lobjAtt.getId().equals(pstrAttachmentId) )
					continue;

				((FileAttachment)lobjAtt).load();

				larrBytes = ((FileAttachment)lobjAtt).getContent();
				lobjFile = new FileXfer(larrBytes.length, ( lobjAtt.getContentType() == null ? "application/octet-stream" : lobjAtt.getContentType() ),
						((FileAttachment)lobjAtt).getName(), new ByteArrayInputStream(larrBytes));

				return lobjFile;
			}
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		throw new BigBangJewelException("Erro: Anexo não encontrado na mensagem indicada.");
	}

	public static void SendFromData(MessageData pobjMessage)
		throws BigBangJewelException
	{
		int llngTo, llngCC, llngBCC, llngReplyTo;
		int i;
		InternetAddress[] larrTos;
		InternetAddress[] larrCCs;
		InternetAddress[] larrBCCs;
		InternetAddress[] larrReplyTos;
		FileXfer[] larrFiles;
		Document lobjDoc;

		if ( pobjMessage.marrAddresses == null )
			return;

		llngTo = 0;
		llngCC = 0;
		llngBCC = 0;
		llngReplyTo = 0;
		for ( i = 0; i < pobjMessage.marrAddresses.length; i++ )
		{
			if ( Constants.UsageID_To.equals(pobjMessage.marrAddresses[i].midUsage) )
				llngTo++;
			else if ( Constants.UsageID_CC.equals(pobjMessage.marrAddresses[i].midUsage) )
				llngCC++;
			else if ( Constants.UsageID_BCC.equals(pobjMessage.marrAddresses[i].midUsage) )
				llngBCC++;
			else if ( Constants.UsageID_ReplyTo.equals(pobjMessage.marrAddresses[i].midUsage) )
				llngReplyTo++;
		}
		larrTos = new InternetAddress[llngTo];
		larrCCs = new InternetAddress[llngCC];
		larrBCCs = new InternetAddress[llngBCC];
		larrReplyTos = new InternetAddress[llngReplyTo];
		llngTo = 0;
		llngCC = 0;
		llngBCC = 0;
		llngReplyTo = 0;
		for ( i = 0; i < pobjMessage.marrAddresses.length; i++ )
		{
			if ( Constants.UsageID_To.equals(pobjMessage.marrAddresses[i].midUsage) )
			{
				larrTos[llngTo] = BuildAddress(pobjMessage.marrAddresses[i]);
				llngTo++;
			}
			else if ( Constants.UsageID_CC.equals(pobjMessage.marrAddresses[i].midUsage) )
			{
				larrCCs[llngCC] = BuildAddress(pobjMessage.marrAddresses[i]);
				llngCC++;
			}
			else if ( Constants.UsageID_BCC.equals(pobjMessage.marrAddresses[i].midUsage) )
			{
				larrBCCs[llngBCC] = BuildAddress(pobjMessage.marrAddresses[i]);
				llngBCC++;
			}
			else if ( Constants.UsageID_ReplyTo.equals(pobjMessage.marrAddresses[i].midUsage) )
			{
				larrReplyTos[llngReplyTo] = BuildAddress(pobjMessage.marrAddresses[i]);
				llngReplyTo++;
			}
		}

		if ( pobjMessage.marrAttachments == null )
			larrFiles = null;
		else
		{
			larrFiles = new FileXfer[pobjMessage.marrAttachments.length];
			for ( i = 0; i < larrFiles.length; i++ )
			{
				lobjDoc = Document.GetInstance(Engine.getCurrentNameSpace(), pobjMessage.marrAttachments[i]);
				larrFiles[i] = lobjDoc.getFile();
			}
		}

		DoSendMail(larrReplyTos, larrTos, larrCCs, larrBCCs, pobjMessage.mstrSubject, pobjMessage.mstrBody, larrFiles);
	}

	public static void DoSendMail(InternetAddress[] parrReplyTo, InternetAddress[] parrTo, InternetAddress[] parrCC, InternetAddress[] parrBCC,
			String pstrSubject, String pstrBody, FileXfer[] parrAttachments)
		throws BigBangJewelException
	{
		Session lsession;
		Transport lxport;
		MimeMessage lmsg;
		MimeMultipart lmpMessage;
		MimeBodyPart lbp;
		int i;

		lsession = GetMailSession();
		lxport = GetMailTransport();

		lmsg = new MimeMessage(lsession);
		try
		{
			lmsg.setFrom(InternetAddress.getLocalAddress(lsession));

			if ( parrReplyTo.length > 0 )
				lmsg.setReplyTo(parrReplyTo);
			for ( i = 0; i < parrTo.length; i++ )
				lmsg.addRecipient(Message.RecipientType.TO, parrTo[i]);
			for ( i = 0; i < parrCC.length; i++ )
				lmsg.addRecipient(Message.RecipientType.CC, parrCC[i]);
			for ( i = 0; i < parrBCC.length; i++ )
				lmsg.addRecipient(Message.RecipientType.BCC, parrBCC[i]);

			lmsg.setSubject(pstrSubject);

			if ( (parrAttachments == null) || (parrAttachments.length == 0) )
			{
				lmsg.setText(pstrBody, "UTF-8");
				lmsg.addHeader("Content-Type", "text/html");
			}
			else
			{
				lmpMessage = new MimeMultipart();

				lbp = new MimeBodyPart();
				lbp.setText(pstrBody, "UTF-8");
				lmpMessage.addBodyPart(lbp);

				for ( i = 0; i < parrAttachments.length; i++ )
				{
					if ( parrAttachments[i] == null )
						continue;

					lbp = new MimeBodyPart();
					lbp.setDataHandler(new DataHandler(new ByteArrayDataSource(parrAttachments[i].getData(),
							parrAttachments[i].getContentType())));
					lbp.setFileName(parrAttachments[i].getFileName());
					lmpMessage.addBodyPart(lbp);
				}

				lmsg.setContent(lmpMessage);
				lmsg.addHeader("Content-Type", lmpMessage.getContentType());
			}

			lmsg.addHeader("MIME-Version", "1.0");
			lmsg.saveChanges();
			lxport.sendMessage(lmsg, lmsg.getAllRecipients());
			lxport.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	public static MessageData GetAsData(String pstrUniqueID)
		throws BigBangJewelException
	{
		Item lobjItem;
		EmailAddress lobjFrom;
		EmailAddressCollection larrTo;
		EmailAddressCollection larrReplyTo;
		EmailAddressCollection larrCC;
		EmailAddressCollection larrBCC;
		MessageData lobjResult;
		int llngLen;
		int i;

		lobjItem = DoGetItem(pstrUniqueID);

		lobjResult = new MessageData();

		try
		{
			lobjResult.mstrSubject = lobjItem.getSubject();
			lobjResult.midOwner = null;
			lobjResult.mlngNumber = -1;
			lobjResult.midDirection = Constants.MsgDir_Incoming;
			lobjResult.mbIsEmail = true;
			lobjResult.mdtDate = new Timestamp(lobjItem.getDateTimeReceived().getTime());
			lobjResult.mstrBody = lobjItem.getBody().toString();

			lobjFrom = ((EmailMessage)lobjItem).getFrom();
			larrTo = ((EmailMessage)lobjItem).getToRecipients();
			larrCC = ((EmailMessage)lobjItem).getCcRecipients();
			larrBCC = ((EmailMessage)lobjItem).getBccRecipients();
			larrReplyTo = ((EmailMessage)lobjItem).getReplyTo();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		llngLen = ( lobjFrom == null ? 0 : 1) + (larrTo == null ? 0 : larrTo.getCount()) + (larrCC == null ? 0 : larrCC.getCount()) +
				(larrBCC == null ? 0 : larrBCC.getCount()) + (larrReplyTo == null ? 0 : larrReplyTo.getCount());

		if ( llngLen < 1)
			lobjResult.marrAddresses = null;
		else
		{
			lobjResult.marrAddresses = new MessageAddressData[llngLen];
			i = 0;
			if ( lobjFrom != null )
			{
				lobjResult.marrAddresses[i] = GetAddress(lobjFrom, Constants.UsageID_From);
				i++;
			}
			if ( larrTo != null )
			{
				for ( EmailAddress laddr : larrTo )
				{
					lobjResult.marrAddresses[i] = GetAddress(laddr, Constants.UsageID_To);
					i++;
				}
			}
			if ( larrCC != null )
			{
				for ( EmailAddress laddr : larrCC )
				{
					lobjResult.marrAddresses[i] = GetAddress(laddr, Constants.UsageID_CC);
					i++;
				}
			}
			if ( larrBCC != null )
			{
				for ( EmailAddress laddr : larrBCC )
				{
					lobjResult.marrAddresses[i] = GetAddress(laddr, Constants.UsageID_BCC);
					i++;
				}
			}
			if ( larrReplyTo != null )
			{
				for ( EmailAddress laddr : larrReplyTo )
				{
					lobjResult.marrAddresses[i] = GetAddress(laddr, Constants.UsageID_ReplyTo);
					i++;
				}
			}
		}

		return lobjResult;
	}

	public static MessageAddressData GetAddress(EmailAddress pobjSource, UUID pidUsage)
	{
		MessageAddressData lobjResult;

		lobjResult = new MessageAddressData();
		lobjResult.mstrAddress = pobjSource.getAddress();
		lobjResult.midOwner = null;
		lobjResult.midUsage = pidUsage;
		lobjResult.midUser = null;
		lobjResult.midInfo = null;
		lobjResult.mstrDisplay = pobjSource.getName();

		return lobjResult;
	}

	public static InternetAddress BuildAddress(MessageAddressData pobjSource)
		throws BigBangJewelException
	{
		try
		{
			if ( pobjSource.mstrDisplay == null )
				return new InternetAddress(pobjSource.mstrAddress);
			else
				return new InternetAddress(pobjSource.mstrAddress, pobjSource.mstrDisplay);
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
