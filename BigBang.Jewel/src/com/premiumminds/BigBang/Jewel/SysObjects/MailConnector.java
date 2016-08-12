package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
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

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.util.IOUtils;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Implementation.User;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.Security.Password;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.MessageAddressData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.OutgoingMessageData;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;

import com.premiumminds.BigBang.Jewel.Security.OAuthHandler;

/**
 *	Class responsible for implementing the needed functionalities relating 
 *	with mails, namely the interaction between BigBang and the mail server.
 *	It provides the implementation to all the "common" functionalities (send, receive, etc) 
 *	as well as auxiliary methods.
 */
public class MailConnector {

	/**
	 *	This method sends an email, receiving all the "usual" content on an email message.
	 */
	public static void sendMail(String[] replyTo, String[] to, String[] cc, String[] bcc, 
			String subject, String body, FileXfer[] attachments) throws BigBangJewelException {

		Session session;
		InternetAddress[] addresses;

		try {
			session = getSession();
			session.setDebug(true);
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

			// Sets REPLY TO
			addresses = buildAddresses(replyTo);
			if (addresses != null && addresses.length != 0) {
				mailMsg.setReplyTo(addresses);
			}

			// Sets TO
			addresses = buildAddresses(to);
			if (addresses != null && addresses.length != 0) {
				mailMsg.setRecipients(Message.RecipientType.TO, addresses);
			}

			// Sets CC
			addresses = buildAddresses(cc);
			if (addresses != null && addresses.length != 0) {
				mailMsg.setRecipients(Message.RecipientType.CC, addresses);
			}

			// Sets BCC
			addresses = buildAddresses(bcc);
			if (addresses != null && addresses.length != 0) {
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
	 *	This method sends an email with info in the OutgoingMessageData object and
	 *  info stored in the DB
	 */
	public static void sendMail(OutgoingMessageData message, SQLServer sqlServer) 
			throws BigBangJewelException {

		String[] to;
		String[] replyTo;
		ResultSet usersSet;
		IEntity decorationEntity;
		FileXfer[] attachments;
		Document document;

		// Gets the message's 'TOs' if they exist
		if (message.marrContactInfos == null) {
			to = null;
		} else {
			to = new String[message.marrContactInfos.length];
			for (int i=0; i<message.marrContactInfos.length; i++) {
				to[i] = (String)ContactInfo.GetInstance(Engine.getCurrentNameSpace(),
						message.marrContactInfos[i]).getAt(2);
			}
		}

		if (to != null) {
			usersSet = null;
			
			// Gets the message's 'REPLYTOs' if they exist
			try {
				decorationEntity = Entity.GetInstance(Engine.FindEntity(
						Engine.getCurrentNameSpace(), Constants.ObjID_Decorations));
				replyTo = new String[message.marrUsers.length];
				for (int i=0; i<message.marrUsers.length; i++) {
					usersSet = decorationEntity.SelectByMembers(sqlServer, 
							new int[] {0}, 
							new java.lang.Object[] {message.marrUsers[i]}, new int[0]);
					if (usersSet.next())
						replyTo[i] = (String)UserDecoration.GetInstance(Engine.getCurrentNameSpace(), usersSet).getAt(1);
					else
						replyTo[i] = null;
					usersSet.close();
				}
			} catch (Throwable e) {
				if (usersSet != null) {
					try { 
						usersSet.close(); 
					} catch (Throwable e1) {
						// do nothing
					}
				}
				throw new BigBangJewelException(e.getMessage(), e);
			}

			// Gets the message's Attachments
			if (message.marrAttachments == null) {
				attachments = null;
			} else {
				attachments = new FileXfer[message.marrAttachments.length];
				for (int i=0; i<attachments.length; i++) {
					document = Document.GetInstance(Engine.getCurrentNameSpace(), message.marrAttachments[i]);
					attachments[i] = document.getFile();
				}
			}
			
			// Calls the method to send the message
			sendMail(replyTo, to, message.marrCCs, message.marrBCCs,
					message.mstrSubject, message.mstrBody, attachments);
		}
	}

	/**
	 *	This method lists all the folders inside a given folder, or inside the default folder
	 */
	private static Folder[] listFolders(String parentFolder) throws BigBangJewelException {

		Store store;
		Folder[] listingFolders = null;
		Folder start;

		try {
			OAuthHandler.initialize();
			store = OAuthHandler.getImapStore(getUserEmail()); 

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
	public static Message[] getMails(String folderId, boolean filterUnseen) throws BigBangJewelException {

		Message[] fetchedMails = null;
		Store store;
		Folder folder = null;
		
		try {
			OAuthHandler.initialize();
			store = OAuthHandler.getImapStore(getUserEmail()); 

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

		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		Collections.reverse(Arrays.asList(fetchedMails));
		
		return fetchedMails;		
	}

	/**
	 *	This method returns a message identified by a given number, inside a given folder
	 */
	@SuppressWarnings("unused")
	private Message getMessage(int msgNumber, String folderId) throws BigBangJewelException {

		Message fetchedMessage = null;

		Store store;
		Folder folder = null;

		OAuthHandler.initialize();

		try {
			store = OAuthHandler.getImapStore(getUserEmail()); 
			
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
	public static Message getMessage(String msgId, String folderId) throws BigBangJewelException {

		Message fetchedMessage = null;

		Store store;
		Folder folder = null;

		OAuthHandler.initialize();

		try {
			store = OAuthHandler.getImapStore(getUserEmail()); 
			
			if (folderId != null && folderId.length() > 0) {
				folder = store.getFolder(folderId);
			} else {
				// Default - Get inbox
				folder = store.getFolder("inbox");
			}
			folder.open(Folder.READ_ONLY);

			for (int i=1; i<=folder.getMessageCount(); i++) {

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

		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return fetchedMessage;
	}

	/**
	 *	This method gets all attachments on a message. It calls the method which returns
	 * 	a map with attachmentId / Attachment content, and extracts only the contents 
	 */
	public static List<BodyPart> getAttachments(Message message) throws Exception {

		Map<String, BodyPart> attachmentsMap = getAttachmentsMap(message);

		if (attachmentsMap != null && attachmentsMap.size() > 0) {
			List<BodyPart> result = new ArrayList<BodyPart>();
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
	public static Map<String, BodyPart> getAttachmentsMap(Message message) throws BigBangJewelException {
		Object content;
		try {
			content = message.getContent();
			if (content instanceof String)
				return null;        

			if (content instanceof Multipart) {
				Multipart multipart = (Multipart) content;
				Map<String, BodyPart> result = new HashMap<String, BodyPart>();

				for (int i = 0; i < multipart.getCount(); i++) {
					result.putAll(getAttachmentsMap(multipart.getBodyPart(i)));
				}
				return result;

			}
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
		return null;
	}

	/**
	 *	This is a recursive method to get all attachments in a body part. 
	 */
	private static Map<String, BodyPart> getAttachmentsMap(BodyPart part) throws Exception {
		
		Map<String, BodyPart> result = new HashMap<String, BodyPart>();
		Object content = part.getContent();
		
		// If it is an attachment, gets its id from the header and inserts it in the result's map
		if (content instanceof InputStream || content instanceof String) {
			if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) || part.getFileName()!=null ) {
				Enumeration<?> headers = part.getAllHeaders();
				
				while (headers.hasMoreElements()) {
					
					String attId = "";
					
					if(part.getHeader("Content-Id") != null) {
						attId = part.getHeader("Content-Id")[0];
					} else if(part.getHeader("Content-Description") != null) {
						attId = part.getHeader("Content-Description")[0];
					} else {
						attId = part.getFileName();
					}
					
					if(attId != null) { // TODO: e se nao tiver ID?
						result.put(attId, part);
						break;
					}
				}
				return result;
			} else if (part.getContentType().contains("TEXT/PLAIN") || part.getContentType().contains("TEXT/HTML")) { 
				result.put("main", part);
			} else {
				return new HashMap<String, BodyPart>();
			}
		}

		// If the part is a Multipart, the method recursively calls itself
		if (content instanceof Multipart) {
			Multipart multipart = (Multipart) content;
			for (int i = 0; i < multipart.getCount(); i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				result.putAll(getAttachmentsMap(bodyPart));
			}
		}
		return result;
	}

	/**
	 *	This method fetches a given email and creates (and returns) a Map with 
	 *	{(<_>, <mail_Id>), (<attachmentId_1>, <mail_Id>), (...), (<attachmentId_n>, <mail_Id>)}  
	 */
	public static Map<String, String> processItem(String pstrUniqueID, Message fetchedItem, Map<String, BodyPart> mailAttachments) throws BigBangJewelException {

		Map<String, String> processed = null;

		if (fetchedItem == null) {
			fetchedItem = getMessage(pstrUniqueID, null);
		}

		processed = new HashMap<String, String>();

		processed.put("_", pstrUniqueID);

		try {
			if (mailAttachments == null) {
				mailAttachments = getAttachmentsMap(fetchedItem);
			}
		} catch (Exception e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if (mailAttachments!=null && mailAttachments.size() > 0) {
			for (Map.Entry<String, BodyPart> entry : mailAttachments.entrySet()) {
				processed.put(entry.getKey(), pstrUniqueID);
			}			
		}

		return processed;
	}

	public static void DoUnprocessItem(String pstrUniqueID) 
			throws BigBangJewelException {
		// TODO Dummy method, it would change EWS's ITEM's changes made in processItem... not being made now...
	}

	/**
	 *	This methods gets a MessageData object, and manipulates it, extracting
	 *	the needed information to call the sendData's method 
	 */
	public static void sendFromData(MessageData message) throws BigBangJewelException {

		int countTo = 0;
		int countCC = 0;
		int countBCC = 0;
		int countReplyTo = 0;

		String[] to;
		String[] cc;
		String[] bcc;
		String[] replyTo;

		Document document;
		FileXfer[] attachments;

		if (message.marrAddresses == null) {
			return;
		}

		// Counts numbers of addresses of different types
		for (int i=0; i<message.marrAddresses.length; i++) {
			if (Constants.UsageID_To.equals(message.marrAddresses[i].midUsage)) {
				countTo++;
			} else if (Constants.UsageID_CC.equals(message.marrAddresses[i].midUsage)) {
				countCC++;
			} else if ( Constants.UsageID_BCC.equals(message.marrAddresses[i].midUsage)) {
				countBCC++;
			} else if ( Constants.UsageID_ReplyTo.equals(message.marrAddresses[i].midUsage)) {
				countReplyTo++;
			}
		}

		to = new String[countTo];
		cc = new String[countCC];
		bcc = new String[countBCC];
		replyTo = new String[countReplyTo];

		countTo = 0;
		countCC = 0;
		countBCC = 0;
		countReplyTo = 0;

		// Builds the arrays with the different types of addresses to send the message to
		for (int i=0; i<message.marrAddresses.length; i++) {
			if (Constants.UsageID_To.equals(message.marrAddresses[i].midUsage)) {
				to[countTo] = message.marrAddresses[i].mstrAddress;
				countTo++;
			} else if (Constants.UsageID_CC.equals(message.marrAddresses[i].midUsage)) {
				cc[countCC] = message.marrAddresses[i].mstrAddress;
				countCC++;
			} else if (Constants.UsageID_BCC.equals(message.marrAddresses[i].midUsage)) {
				bcc[countBCC] = message.marrAddresses[i].mstrAddress;
				countBCC++;
			} else if (Constants.UsageID_ReplyTo.equals(message.marrAddresses[i].midUsage)) {
				replyTo[countReplyTo] = message.marrAddresses[i].mstrAddress;
				countReplyTo++;
			}
		}

		// Gets the attachments
		if (message.marrAttachments == null) {
			attachments = null;
		} else {
			attachments = new FileXfer[message.marrAttachments.length];
			for (int i=0; i<attachments.length; i++) {
				document = Document.GetInstance(Engine.getCurrentNameSpace(), message.marrAttachments[i].midDocument);
				attachments[i] = document.getFile();
			}
		}

		sendMail(replyTo, to, cc, bcc, message.mstrSubject, message.mstrBody, attachments);
	}

	/**
	 *	This methods gets a Message, and creates (and returns) a MessageData object with that 
	 *	Message's information
	 */
	public static MessageData getAsData(String pstrUniqueID, String folderId) throws BigBangJewelException {

		MessageData result = new MessageData();

		String from;
		Address[] to, cc, bcc, replyTo;
		int addLength;
		int i;

		// Gets the message with a given ID
		Message fetchedMessage = getMessage(pstrUniqueID, folderId);

		if (fetchedMessage != null) {
			
			InternetAddress fromAddress = null;

			// Builds the MessageData with the existing Message's fields
			try {
				result.mstrSubject = fetchedMessage.getSubject();
				result.midOwner = null;
				result.mlngNumber = -1;
				result.mbIsEmail = true;
				result.mdtDate = new Timestamp(fetchedMessage.getSentDate().getTime());
				
				Object content = fetchedMessage.getContent();
				
				if (content instanceof Multipart) {
					
					Map<String, BodyPart> attachmentsMap = getAttachmentsMap(fetchedMessage);
					
					result.mstrBody = attachmentsMap.get("main").getContent().toString();
					result.mstrBody = prepareBodyInline(result.mstrBody, attachmentsMap);
				} else {
					result.mstrBody = content.toString();
					result.mstrBody = prepareSimpleBody(result.mstrBody);
				}
				
				fromAddress = (InternetAddress) (fetchedMessage.getFrom() == null ? null : fetchedMessage.getFrom()[0]);
				from = fromAddress == null ? "" : fromAddress.getAddress();
				
				to = fetchedMessage.getRecipients(RecipientType.TO);
				cc = fetchedMessage.getRecipients(RecipientType.CC);
				bcc = fetchedMessage.getRecipients(RecipientType.BCC);
				replyTo = fetchedMessage.getReplyTo();
			} catch (Throwable e) {
				throw new BigBangJewelException(e.getMessage(), e);
			}

			result.midDirection = ( getUserEmail().equalsIgnoreCase(from) ?
					Constants.MsgDir_Outgoing : Constants.MsgDir_Incoming );

			addLength = ( from == null ? 0 : 1) + (to == null ? 0 : to.length) + (cc == null ? 0 : cc.length) +
					(bcc == null ? 0 : bcc.length) + (replyTo == null ? 0 : replyTo.length);

			if ( addLength < 1) {
				result.marrAddresses = null;
			} else {
				result.marrAddresses = new MessageAddressData[addLength];
				i = 0;
				if (from != null) {
					result.marrAddresses[i] = getAddress(null, fromAddress, Constants.UsageID_From);
					i++;
				}
				if (to != null) {
					for (Address addr : to) {
						result.marrAddresses[i] = getAddress(null, addr, Constants.UsageID_To);
						i++;
					}
				}
				if (cc != null) {
					for (Address addr : cc) {
						result.marrAddresses[i] = getAddress(null, addr, Constants.UsageID_CC);
						i++;
					}
				}
				if (bcc != null) {
					for (Address addr : bcc) {
						result.marrAddresses[i] = getAddress(null, addr, Constants.UsageID_BCC);
						i++;
					}
				}
				if (replyTo != null) {
					for (Address addr : replyTo) {
						result.marrAddresses[i] = getAddress(null, addr, Constants.UsageID_ReplyTo);
						i++;
					}
				}
			}
		}

		return result;
	}

	/** 
	 * This method changes "string only" mail bodies to html in order to display
	 * them with the correct formatting.
	 */
	public static String prepareSimpleBody(String string) {
		String result = "<div>";
		result = result + string.replaceAll("(\r\n|\n)", "<br />");
		result = result + "</div>";
		return result;
	}
	
	/** 
	 * This method replaces the images in the body of an email with the equivalent
	 * Base-64 converted string. Removes those images and the main text from the
	 * mail attachments
	 */
	public static String prepareBodyInline(String lstrBody, Map<String, BodyPart> attachmentsMap) throws BigBangJewelException {
		
		// Uses a Regular Expression to find "IMG" tags in html
		final String regex = "src\\s*=\\s*([\"'])?([^\"']*)";
		final Pattern p = Pattern.compile(regex);
        final Matcher m = p.matcher(lstrBody);
		
        // Iterates the found images
        while (m.find()) {
        	// Gets the image's name from html, and adapts it to use as a key to fetch the 
        	// image from the attachments' map
        	String originalHtml = m.group();
			String prevImg = "<" + originalHtml.substring(9, originalHtml.length()) + ">";
			BodyPart imageBodyPart = attachmentsMap.get(prevImg);
			if (imageBodyPart!=null) {
				
				// Gets the encoded base-64 String, ready to be used in HTML's IMG's SRC
				try {
					byte[] binaryData = IOUtils.toByteArray(imageBodyPart.getInputStream());
					String encodedString = Base64.encodeBase64String(binaryData);
					String dataType = imageBodyPart.getContentType();
					String[] splittedType = dataType.split(";");
					dataType = "data:" + splittedType[0] + ";base64,"; 
					
					// Changes HTML's IMG tag
					lstrBody = lstrBody.replaceAll(originalHtml, "src=\"" + dataType + encodedString);
					
					// Removes the inline image from the attachments
					attachmentsMap.remove(prevImg);
										
				} catch (Throwable e) {
					throw new BigBangJewelException(e.getMessage(), e);
				}
			}
        }
		
		return lstrBody;
	}
	
	/** 
	 * This method removes html tags to display the mail's body preview
	 * correctly.
	 */
	public static String removeHtml(String lstrBody) {
		return lstrBody.replaceAll("\\<.*?>","");
	}
	
	/**
	 *	This method creates a MessageAddressData given a userName, a user email and a usageGuid (income/outcome)
	 */
	public static MessageAddressData getAddress(String displayName, Address address, UUID usageGuid) {

		MessageAddressData result;

		result = new MessageAddressData();
		result.mstrAddress = (address instanceof InternetAddress) ? ((InternetAddress) address).getAddress() : address.toString();
		result.midOwner = null;
		result.midUsage = usageGuid;
		result.midUser = null;
		result.midInfo = null;
		result.mstrDisplay = displayName==null ? address.toString() : displayName;

		return result;
	}

	/**
	 *	This method returns an attachment, giving a message ID and an attachment ID
	 *	It calls the methods to retrieve a message, and that message's attachments. 
	 */
	public static FileXfer getAttachment(String msgId, String folderId, String attachmentId) throws BigBangJewelException {

		// Gets a message from a given folder, with a given ID
		Message message = getMessage(msgId, folderId);

		Map<String, BodyPart> messageAttachments = getAttachmentsMap(message);
		BodyPart attachment; 

		InputStream attachmentStream;
		try {
			attachment = messageAttachments.get(attachmentId);
			attachmentStream = attachment.getInputStream();
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		if (attachmentStream != null) {
			try {
				String contentType = attachment.getContentType();
				String mimeType = contentType!=null ? contentType.split(";")[0] : null;
				FileXfer attachmentXFer = new FileXfer(attachment.getSize(), mimeType, 
						attachment.getFileName(), attachmentStream);
				return attachmentXFer;
			} catch (Throwable e) {
				throw new BigBangJewelException(e.getMessage(), e);
			}
		}
		return null;		
	}

	/**
	 *	This method creates an InternetAddress from a MessageAddressData's object
	 */
	public static InternetAddress buildAddress(MessageAddressData pobjSource)
			throws BigBangJewelException {
		try {
			if ( pobjSource.mstrDisplay == null ) {
				return new InternetAddress(pobjSource.mstrAddress);
			} else {
				return new InternetAddress(pobjSource.mstrAddress, pobjSource.mstrDisplay);
			}
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
	}

	/**
	 *	This method returns a Store initialized with the smtp protocol, with a connection already set
	 */
	private static Store getStore(Session session) throws BigBangJewelException {

		Store store = null; 

		try { 
			store = session.getStore("imaps");
			store.connect(session.getProperty("mail.host"), 
					getUserEmail(), getUserPassword());
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return store;
	}

	/**
	 *	This method, giving an array of strings representing email addresses, builds an array
	 *	of Internet Addresses
	 */
	private static InternetAddress[] buildAddresses(String[] mailArray) throws AddressException {

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
		Properties mailProps = System.getProperties();
		
		mailServer = "imap.gmail.com"; //TODO - mudar para a BD
		authenticator = new JewelAuthenticator(getUserEmail(), getUserPassword());
		
		mailProps.setProperty("mail.store.protocol", "imaps");
		mailProps.put("mail.host", mailServer);
		
		return Session.getInstance(mailProps, authenticator);
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

	/**
	 *	This method gets all the messages inside the sent or inbox folder, according 
	 *	to a parameter
	 */
	public static Message[] getSentOrReceived(boolean sent) throws BigBangJewelException {

		Message[] result = null;

		if (sent) {
			result = getMails("Itens Enviados", false);
		} else {
			result = getMails("Inbox", false);
		}

		return result;
	}
	
	/**
	 *	This method gets all "first level" folders, or all folders inside a folder
	 */
	public static Folder[] getFolders(String folderId) throws BigBangJewelException {

		Folder[] result = null;
		Store store;
		
		try {
			OAuthHandler.initialize();
			store = OAuthHandler.getImapStore(getUserEmail()); 
			
			if (folderId == null) {
				result = store.getDefaultFolder().list();
				
				// In Gmail, when accessing through imap, it returns the [Gmail] folder, which must be filtered 
				int sysFolderIdx = getSystemFolderIndex(result);
				if (sysFolderIdx != -1) {
					ArrayList <Folder> resultsList = new ArrayList<Folder>(Arrays.asList(result));
					resultsList.remove(sysFolderIdx);
					result = resultsList.toArray(new Folder[resultsList.size()]);
				}
			} else {
				result = store.getFolder(folderId).list();
			}

		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return result;
	}
	
	/**
	 *	This method returns the index for the [Gmail] folder in a Folder's array.
	 *	That folder should not be listed to the user.
	 */
	public static int getSystemFolderIndex(Folder[] folders) {
		
		if (folders==null) {
			return -1;
		}
		
		for (int i=0; i<folders.length; i++) {
			if (folders[i].getFullName().equals("[Gmail]") ||
					folders[i].getFullName().equals("[Google Mail]")) {
				return i;
			}
		}
		
		return -1;
	}

	/**
	 *	This method returns a given folder, identified by id.
	 *	It calls the method to retrieve all folders, and iterates them
	 */
	public static Folder getFolder(String folderID) throws BigBangJewelException {

		Folder[] allFolders = listFolders(null);
		Folder result = null; 

		if (allFolders == null) {
			return null;
		}

		for (int i=0; i<allFolders.length; i++) {
			result = allFolders[i];
			if (result.getName().equals(folderID)) {
				break;
			}
		}

		return result;
	}
}
