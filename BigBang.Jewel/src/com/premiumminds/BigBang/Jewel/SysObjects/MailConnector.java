package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.httpclient.methods.GetMethod;

import Jewel.Engine.Engine;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Security.Password;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;

/**
 *	Class responsible for implementing the needed functionalities relating 
 *	with mails, namely the interaction between BigBang and Microsoft exchange.
 *	It provides the implementation to all the "common" functionalities (send, receive, etc) 
 *	as well as auxiliary methods.
 */
public class MailConnector {

	/**
	 *	This method sends an email, receiving all the "usual" content on an email message.
	 */
	public void sendMail(String[] to, String[] cc, String[] bcc, String subject, String body, 
			FileXfer[] attachments) throws BigBangJewelException {

		Session session;
		InternetAddress[] addresses;

		try {
			session = getSession();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		/*
		 *  Creates a message and sends the mail
		 */
		MimeMessage mailMsg = new MimeMessage(session);

		try {
			// Sets FROM
			mailMsg.setFrom(new InternetAddress(session.getProperty("mail.from")));

			// Sets TO
			addresses = buildAddresses(to);
			if (addresses != null) {
				mailMsg.setRecipients(Message.RecipientType.TO, addresses);
			}

			// Sets CC
			addresses = buildAddresses(cc);
			if (addresses != null) {
				mailMsg.setRecipients(Message.RecipientType.CC, addresses);
			}

			// Sets BCC
			addresses = buildAddresses(bcc);
			if (addresses != null) {
				mailMsg.setRecipients(Message.RecipientType.BCC, addresses);
			}

			// Sets SUBJECT
			mailMsg.setSubject(subject);

			// Sets SENT DATE
			mailMsg.setSentDate(new Date());

			// Sets the message's TEXT
			if ((attachments == null) || (attachments.length == 0)) {
				mailMsg.setText(body, "UTF-8");
				mailMsg.addHeader("Content-Type", "text/html");
			} else {
				// If it has attachments, must set a multipart mail
				MimeMultipart multipartMsg = new MimeMultipart();
				MimeBodyPart bodyPart = new MimeBodyPart();
				bodyPart.setText(body, "UTF-8");
				multipartMsg.addBodyPart(bodyPart);

				for (int i=0; i<attachments.length; i++) {
					if (attachments[i] == null) {
						continue;
					}

					bodyPart = new MimeBodyPart();
					bodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(attachments[i].getData(),
							attachments[i].getContentType())));
					bodyPart.setFileName(attachments[i].getFileName());
					multipartMsg.addBodyPart(bodyPart);
				}

				mailMsg.setContent(multipartMsg);
				mailMsg.addHeader("Content-Type", multipartMsg.getContentType());
			}

			mailMsg.addHeader("MIME-Version", "1.0");
			mailMsg.saveChanges();

			// Sends the message
			Transport.send(mailMsg);

		} catch (MessagingException e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	/**
	 *	This method lists all the folders inside a given folder, or inside the default folder
	 */
	private Folder[] listFolders(String parentFolder) throws BigBangJewelException {

		Session session;
		Store store;
		Folder[] listingFolders = null;
		Folder start;

		try {
			session = getSession();
			store = getStore(session);

			if (parentFolder != null && parentFolder.length() > 0) {
				start = store.getFolder(parentFolder);
			} else {
				start = store.getDefaultFolder();
			}

			start.open(Folder.READ_WRITE);

			if (start.getType() == Folder.HOLDS_FOLDERS) {
				listingFolders = start.list();
			} 

			store.close();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return listingFolders;
	}

	/**
	 *	This method gets all the mails in a given folder, or from the inbox folder if no folder is specified.
	 *	The mails may be filtered, returning only those which are unread.
	 */
	private Message[] getMails(String folderId, boolean filterUnseen) throws BigBangJewelException {

		Message[] fetchedMails = null;
		Session session;
		Store store;
		Folder folder = null;

		try {
			session = getSession();
			store = getStore(session);

			if (folderId != null && folderId.length() > 0) {
				folder = store.getFolder(folderId);
			} else {
				// Default - Get inbox
				folder = store.getFolder("inbox");
			}

			folder.open(Folder.READ_ONLY);

			if (filterUnseen) {
				// search for all "unseen" messages
				Flags seen = new Flags(Flags.Flag.SEEN);
				FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
				fetchedMails = folder.search(unseenFlagTerm);
			} else {
				fetchedMails = folder.getMessages();
			}

			folder.close(false);
			store.close();

		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return fetchedMails;		
	}

	/**
	 *	This method returns a message identified by a given number, inside a given folder
	 */
	private Message getMessage(int msgNumber, String folderId) throws BigBangJewelException {

		Message fetchedMessage = null;

		Session session;
		Store store;
		Folder folder = null;

		session = getSession();
		store = getStore(session);

		try {
			if (folderId != null && folderId.length() > 0) {
				folder = store.getFolder(folderId);
			} else {
				// Default - Get inbox
				folder = store.getFolder("inbox");
			}
			folder.open(Folder.READ_ONLY);
			fetchedMessage = folder.getMessage(msgNumber);
			folder.close(false);
			store.close();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return fetchedMessage;
	}

	/**
	 *	This method returns a message identified by a given ID, as it exists in the message header
	 */
	private static Message getMessage(String msgId, String folderId) throws BigBangJewelException {

		Message fetchedMessage = null;

		Session session;
		Store store;
		Folder folder = null;

		session = getSession();
		store = getStore(session);

		try {
			if (folderId != null && folderId.length() > 0) {
				folder = store.getFolder(folderId);
			} else {
				// Default - Get inbox
				folder = store.getFolder("inbox");
			}
			folder.open(Folder.READ_ONLY);

			for (int i = 1;i <= folder.getMessageCount(); i++) {

				folder.getMessage(i).getHeader("Message-Id"); 
				Enumeration<?> headers = folder.getMessage(i).getAllHeaders();

				boolean idFound = false;

				while (headers.hasMoreElements()) {
					Header h = (Header) headers.nextElement();         
					String mID = h.getName();                
					if(mID.contains("Message-ID") || mID.contains("Message-Id")) {
						if (h.getValue().equals(msgId)) {
							fetchedMessage = folder.getMessage(i);
							idFound = true;
							break;
						}
					}
				}

				if (idFound) {
					break;
				}
			}

			folder.close(false);
			store.close();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return fetchedMessage;
	}

	/**
	 *	This method gets all attachments on a message. It calls the method which returns
	 * 	a map with attachmentId / Attachment content, and extracts only the contents 
	 */
	private static List<InputStream> getAttachments(Message message) throws Exception {

		Map<String, InputStream> attachmentsMap = getAttachmentsMap(message);

		if (attachmentsMap.size() > 0) {
			List<InputStream> result = new ArrayList<InputStream>();
			for (String key: attachmentsMap.keySet()) {
				result.add(attachmentsMap.get(key));
			}
			return result;
		}

		return null;

	}

	/**
	 *	This method gets all attachments on a message. It calls a recursive method for all 
	 *	the body parts of the Message parameter, for parts can be encapsulated, with attachments
	 */
	private static Map<String, InputStream> getAttachmentsMap(Message message) throws Exception {
		Object content = message.getContent();
		if (content instanceof String)
			return null;        

		if (content instanceof Multipart) {
			Multipart multipart = (Multipart) content;
			Map<String, InputStream> result = new HashMap<String, InputStream>();

			for (int i = 0; i < multipart.getCount(); i++) {
				result.putAll(getAttachmentsMap(multipart.getBodyPart(i)));
			}
			return result;

		}
		return null;
	}

	/**
	 *	This is a recursive method to get all attachments in a body part. 
	 */
	private static Map<String, InputStream> getAttachmentsMap(BodyPart part) throws Exception {
		Map<String, InputStream> result = new HashMap<String, InputStream>();
		Object content = part.getContent();
		if (content instanceof InputStream || content instanceof String) {
			if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) || part.getFileName()!=null ) {
				Enumeration<?> headers = part.getAllHeaders();
				while (headers.hasMoreElements()) {
					Header h = (Header) headers.nextElement();
					if(h.getName().equals("id")) {
						result.put(h.getValue(), part.getInputStream());
						break;
					}
				}
				return result;
			} else {
				return new HashMap<String, InputStream>();
			}
		}

		if (content instanceof Multipart) {
			Multipart multipart = (Multipart) content;
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				result.putAll(getAttachmentsMap(bodyPart));
			}
		}
		return result;
	}

	public static Map<String, String> DoProcessItem(String pstrUniqueID, UUID pidTag, Date pdtRef) throws BigBangJewelException {

		Map<String, String> processed = null;
		Message fetchedItem = null; 

		fetchedItem = getMessage(pstrUniqueID, null);

		if (fetchedItem != null) {

			processed = new HashMap<String, String>();

			processed.put("_", pstrUniqueID);

			Map<String, InputStream> mailAttachments;

			try {
				mailAttachments = getAttachmentsMap(fetchedItem);
			} catch (Exception e) {
				throw new BigBangJewelException(e.getMessage(), e);
			}

			if (mailAttachments.size() > 0) {
				for (Map.Entry<String, InputStream> entry : mailAttachments.entrySet()) {
					processed.put(entry.getKey(), pstrUniqueID);
				}			
			}
		}

		// Se tem atts, vai colocando nos results


		return processed;
	}

	/**
		}
	 *	This method returns a Store initialized with the smtp protocol, with a connection already set
	 */
	private static Store getStore(Session session) throws BigBangJewelException {

		Store store = null; 

		try {
			store = session.getStore("smtp");
			store.connect(session.getProperty("mail.host"), 
					getUserName(), getUserPassword());
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return store;
	}

	/**
	 *	This method, giving an array of strings representing email addresses, builds an array
	 *	of Internet Addresses
	 */
	private InternetAddress[] buildAddresses(String[] mailArray) throws AddressException {

		InternetAddress[] addresses = null;

		if (mailArray != null) {
			addresses = new InternetAddress[mailArray.length];
			for (int i=0; i<mailArray.length; i++) {
				InternetAddress internetAddress = new InternetAddress(mailArray[i]);
				boolean isMailValid = true;
				try {
					internetAddress.validate();
				} catch (AddressException e) {
					isMailValid = false;
				}
				if (isMailValid) {
					addresses[i] = internetAddress;
				}
			}
		}

		return addresses;
	}

	/**
	 *	This method sets a Properties object, representing the set of properties used by javax's email,
	 * 	and creates and returns a javax's session
	 */
	private static Session getSession() throws BigBangJewelException {

		String mailServer;
		JewelAuthenticator authenticator;
		Properties mailProps = new Properties();

		mailServer = (String)Engine.getUserData().get("MailServer");
		authenticator = new JewelAuthenticator(getUserName(), getUserPassword());

		mailProps.put("mail.host", mailServer);
		mailProps.put("mail.from", getUserEmail());
		mailProps.put("mail.smtp.submitter", authenticator.getPasswordAuthentication().getUserName());
		mailProps.put("mail.smtp.auth", "true");
		mailProps.put("mail.smtp.host", mailServer);
		mailProps.put("mail.smtp.port", "25");
		mailProps.put("mail.mime.charset", "utf-8");

		return Session.getInstance(mailProps, authenticator);
	}

	/**
	 *	This method gets the user name for the user in session
	 */
	private static String getUserName() throws BigBangJewelException {

		User user;

		try {
			user = User.GetInstance(Engine.getCurrentNameSpace(), Engine.getCurrentUser());
			return user.getUserName();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	/**
	 *	This method gets the password for the user in session
	 */
	private static String getUserPassword() throws BigBangJewelException {

		User user;
		Password pass;

		try	{
			user = User.GetInstance(Engine.getCurrentNameSpace(), Engine.getCurrentUser());
			if ( user.getAt(2) instanceof Password ) {
				pass = (Password)user.getAt(2);
			} else {
				pass = new Password((String)user.getAt(2), true);
			}
			return pass.GetClear();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	/**
	 *	This method gets the email for the user in session
	 */
	public static String getUserEmail() throws BigBangJewelException {
		UserDecoration userDeco;

		userDeco = UserDecoration.GetByUserID(Engine.getCurrentNameSpace(), Engine.getCurrentUser());
		if ( userDeco == null ) {
			return null;
		}

		return (String) userDeco.getAt(UserDecoration.I.EMAIL);
	}
}
