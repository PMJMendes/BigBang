package com.premiumminds.BigBang.Jewel.Library;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Jewel.Engine.Engine;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;

public class SendMail
{
	static Session GetMailSession()
	{
		return (Session)Engine.getUserData().get("MailSession");
	}

	static Store GetMailStore()
		throws BigBangJewelException
	{
		Session lsession;
		Store lstore;

		lsession = GetMailSession();
		try
		{
			lstore = lsession.getStore("imaps");
			lstore.connect();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return lstore;
	}

	static Transport GetMailTransport()
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

	public static void DoSendMail(String[] larrReplyTo, String[] larrTo, String[] larrCC, String[] larrBCC,
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
			if ( larrReplyTo != null )
			{
				larrAddr = new Address[larrReplyTo.length];
				for ( i = 0; i < larrReplyTo.length; i++ )
					larrAddr[i] = new InternetAddress(larrReplyTo[i]);
				lmsg.setReplyTo(larrAddr);
			}
			for ( i = 0; i < larrTo.length; i++ )
				lmsg.addRecipient(Message.RecipientType.TO, new InternetAddress(larrTo[i]));
			if ( larrCC != null )
				for ( i = 0; i < larrCC.length; i++ )
					lmsg.addRecipient(Message.RecipientType.CC, new InternetAddress(larrCC[i]));
			if ( larrBCC != null )
				for ( i = 0; i < larrBCC.length; i++ )
					lmsg.addRecipient(Message.RecipientType.BCC, new InternetAddress(larrBCC[i]));
			lmsg.setSubject(pstrSubject);
			lmsg.setText(pstrBody, "text/html;charset=\"UTF-8\"");
			lmsg.saveChanges();
			lxport.sendMessage(lmsg, lmsg.getAllRecipients());
			lxport.close();
		}
		catch (Throwable e)
		{
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}
}
