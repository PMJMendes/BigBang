package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.util.ArrayList;

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
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.FileAttachment;
import microsoft.exchange.webservices.data.Folder;
import microsoft.exchange.webservices.data.FolderView;
import microsoft.exchange.webservices.data.Item;
import microsoft.exchange.webservices.data.ItemId;
import microsoft.exchange.webservices.data.ItemView;
import microsoft.exchange.webservices.data.PropertySet;
import microsoft.exchange.webservices.data.WellKnownFolderName;
import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
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
		ArrayList<Folder> larrFolders;
		int i;

		lobjFolder = (Folder)Engine.getUserData().get("ExchangeFolder");

		if ( lobjFolder == null )
		{
			try
			{
				larrFolders = GetService().findFolders(WellKnownFolderName.MsgFolderRoot,
						new FolderView(Integer.MAX_VALUE)).getFolders();
	
				for ( i = 0; i < larrFolders.size(); i++ )
				{
					if ( "bigbang".equals(larrFolders.get(i).getDisplayName()) )
					{
						lobjFolder = larrFolders.get(i);
						Engine.getUserData().put("ExchangeFolder", lobjFolder);
						break;
					}
				}

				if ( lobjFolder == null )
				{
					lobjFolder = new Folder(GetService());
					lobjFolder.setDisplayName("bigbang");
					lobjFolder.save(WellKnownFolderName.MsgFolderRoot);
					Engine.getUserData().put("ExchangeFolder", lobjFolder);
				}
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
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
			lobjParent = GetFolder();

			try
			{
				larrFolders = GetService().findFolders(lobjParent.getId(), new FolderView(Integer.MAX_VALUE)).getFolders();
	
				for ( i = 0; i < larrFolders.size(); i++ )
				{
					if ( "tratados".equals(larrFolders.get(i).getDisplayName()) )
					{
						lobjFolder = larrFolders.get(i);
						Engine.getUserData().put("ExchangeProcessedFolder", lobjFolder);
						break;
					}
				}

				if ( lobjFolder == null )
				{
					lobjFolder = new Folder(GetService());
					lobjFolder.setDisplayName("tratados");
					lobjFolder.save(lobjParent.getId());
					Engine.getUserData().put("ExchangeProcessedFolder", lobjFolder);
				}
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
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
		ArrayList<Item> larrTmp;

		lobjFolder = GetFolder();

		if ( lobjFolder == null )
			return null;

		try
		{
			larrTmp = GetService().findItems(lobjFolder.getId(), new ItemView(Integer.MAX_VALUE)).getItems();
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
}
