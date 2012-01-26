package com.premiumminds.BigBang.Jewel.SysObjects;

import java.util.ArrayList;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.Folder;
import microsoft.exchange.webservices.data.FolderView;
import microsoft.exchange.webservices.data.Item;
import microsoft.exchange.webservices.data.ItemView;
import microsoft.exchange.webservices.data.WellKnownFolderName;
import Jewel.Engine.Engine;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

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
			}
			catch (Throwable e)
			{
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}

		return lobjFolder;
	}

	public static void DoSendMail(String[] parrReplyTo, String[] parrTo, String[] parrCC, String[] parrBCC,
			String pstrSubject, String pstrBody)
		throws BigBangJewelException
	{
		Session lsession;
		Transport lxport;
		MimeMessage lmsg;
		Address[] larrAddr;
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
			lmsg.setText(pstrBody, "UTF-8");
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
}
