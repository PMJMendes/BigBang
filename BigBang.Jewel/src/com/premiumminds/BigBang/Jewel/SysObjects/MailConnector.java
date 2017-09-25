package com.premiumminds.BigBang.Jewel.SysObjects;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.util.IOUtils;

import Jewel.Engine.Engine;
import Jewel.Engine.DataAccess.SQLServer;
import Jewel.Engine.Implementation.Entity;
import Jewel.Engine.Interfaces.IEntity;
import Jewel.Engine.SysObjects.FileXfer;

import com.premiumminds.BigBang.Jewel.BigBangJewelException;
import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.Data.MessageAddressData;
import com.premiumminds.BigBang.Jewel.Data.MessageAttachmentData;
import com.premiumminds.BigBang.Jewel.Data.MessageData;
import com.premiumminds.BigBang.Jewel.Data.OutgoingMessageData;
import com.premiumminds.BigBang.Jewel.Objects.ContactInfo;
import com.premiumminds.BigBang.Jewel.Objects.Document;
import com.premiumminds.BigBang.Jewel.Objects.UserDecoration;
import com.premiumminds.BigBang.Jewel.Security.OAuthHandler;
import com.sun.mail.imap.IMAPFolder.FetchProfileItem;
import com.sun.mail.smtp.SMTPTransport;
import com.sun.mail.util.DecodingException;

/**
 *	Class responsible for implementing the needed functionalities relating 
 *	with mails, namely the interaction between BigBang and the mail server.
 *	It provides the implementation to all the "common" functionalities (send, receive, etc) 
 *	as well as auxiliary methods.
 */
public class MailConnector {

	private static HashMap<String, Store> storesByUser;
	
	private static HashMap<String, Message> lastMessageUser;
	
	private static HashMap<String, LinkedHashMap<String, BodyPart>> lastAttachmentsMapUser;
	
	private static HashMap<String, String> lastPreparedBodyUser;
	
	/**
	 *	This method initializes the store, if needed.
	 *	Avoids "too much connections" error
	 */
	private static void initializeStore() throws BigBangJewelException {
		try {
		
			if (storesByUser == null) {
				storesByUser = new HashMap<String, Store>();
			}
			
			String userMail = getUserEmail();
			
			Store store = storesByUser.get(userMail);
			
			if (store == null || !store.isConnected()) {
				store = OAuthHandler.getImapStore(getUserEmail());
				storesByUser.put(userMail, store);
			}
			
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage() + " 98 ", e);
		}
	}
	
	/**
	 *	This method sends an email, receiving all the "usual" content on an email message.
	 */
	private static void sendMail(String[] replyTo, String[] to, String[] cc, String[] bcc, 
			String[] from, String subject, String body, FileXfer[] attachments, boolean addFrom) throws BigBangJewelException {

		InternetAddress[] addresses;

		try {
			
			OAuthHandler.initialize();
			
			Session smptpSession = OAuthHandler.getSmtpSession(false);
			SMTPTransport sendingConnection = OAuthHandler.getSmtpConnection("smtp.gmail.com", 587, getUserEmail(), smptpSession); 
						
			//Creates a message and sends the mail
			MimeMessage mailMsg = new MimeMessage(smptpSession);
			
			if (addFrom) {
				// Sets FROM
				mailMsg.setFrom(buildAddresses(from)[0]);
			}

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
				bodyPart.setContent(body, "text/html; charset=utf-8");
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
			
			sendingConnection.sendMessage(mailMsg, mailMsg.getAllRecipients());
			
			sendingConnection.close();
			
		} catch (Throwable e) {
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
					null, message.mstrSubject, message.mstrBody, attachments, false);
		}
	}	
	
	/**
	 *	This method gets all the mails in a given folder, or from the inbox folder if no folder is specified.
	 *	The mails may be filtered, returning only those which are unread.
	 */
	public static Message[] getMailsFast(String folderId, boolean filterUnseen) throws BigBangJewelException {

		Message[] fetchedMails = null;
		Folder folder = null;
		
		try {
			OAuthHandler.initialize();
			initializeStore();

			String userMail = getUserEmail();
			
			if (folderId != null && folderId.length() > 0) {
				folder = storesByUser.get(userMail).getFolder(folderId);
			} else {
				// Default - Get inbox
				folder = storesByUser.get(userMail).getFolder("inbox");
			}

			folder.open(Folder.READ_ONLY);
			
			if (filterUnseen) {
				// search for all "unseen" messages
				Flags seen = new Flags(Flags.Flag.SEEN);
				FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
				fetchedMails = folder.search(unseenFlagTerm);
			} else {
				int nrItems = folder.getMessageCount();
				if (nrItems > Constants.GoogleAppsConstants.MAX_FETCHED_MAILS) {
					fetchedMails = folder.getMessages(nrItems-Constants.GoogleAppsConstants.MAX_FETCHED_MAILS, nrItems);
					
					FetchProfile fp = new FetchProfile();
				    fp.add(FetchProfile.Item.ENVELOPE);
				    fp.add(FetchProfileItem.FLAGS);
				    fp.add(FetchProfileItem.CONTENT_INFO);

				    fp.add("X-mailer");
				    folder.fetch(fetchedMails, fp);
				} else {
					fetchedMails = folder.getMessages();
				}
			}

		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}
		
		List<Message> asList = Arrays.asList(fetchedMails);
		Collections.reverse(asList);
		fetchedMails = (Message[]) asList.toArray();
		
		return fetchedMails;		
	}

	/**
	 *	This method returns a message identified by a given ID, as it exists in the message header
	 */
	public static Message getMessage(String msgId, String folderId) throws BigBangJewelException {

		Message fetchedMessage = null;

		Folder folder = null;

		OAuthHandler.initialize();
		initializeStore();

		try {
			
			String userMail = getUserEmail();
			
			// Gets the folder with the argument's ID, and opens it
			if (folderId != null && folderId.length() > 0) {
				folder = storesByUser.get(userMail).getFolder(folderId);
			} else {
				// Default - Get inbox
				folder = storesByUser.get(userMail).getFolder("inbox");
			}
			folder.open(Folder.READ_ONLY);
			
			// Uses the message ID as a search term to try to get the message from the 
			// folder
			SearchTerm searchTerm = new MessageIDTerm(msgId);
			Message[] messages = folder.search(searchTerm);
			
			boolean idFound = false;
			
			if (messages!=null && messages.length>0) {
				for (int i=0; i<=messages.length; i++) {
					Message tmp = messages[i];
					
					Enumeration<?> headers = tmp.getAllHeaders();

					idFound = false;
					
					// If it is a MimeMessage, gets its Id and checks if it
					// matches the method's parameter
					if (tmp instanceof MimeMessage) {
						if (((MimeMessage)tmp).getMessageID().equals(msgId)) {
							return tmp;
						}
					}
					
					while (headers.hasMoreElements()) {
						Header h = (Header) headers.nextElement();         
						String mID = h.getName();                
						if(mID.contains("Message-ID") || mID.contains("Message-Id")) {
							if (h.getValue().equals(msgId)) {
								fetchedMessage = tmp;
								idFound = true;
								break;
							}
						}
					}
					
					if (idFound) {
						return fetchedMessage;
					}
				}
			}
			
			// If for some reason it could not get the message searching for its id, 
			// it fetches message-by-message in the folder until it gets it
			// This is, of course... slow. Hopefully never used
			for (int i=1; i<=folder.getMessageCount(); i++) {

				Message tmp = folder.getMessage(i);
				tmp.getHeader("Message-Id"); 
				Enumeration<?> headers = tmp.getAllHeaders();

				idFound = false;
				
				// If it is a MimeMessage, gets its Id and checks if it
				// matches the method's parameter
				if (tmp instanceof MimeMessage) {
					if (((MimeMessage)tmp).getMessageID().equals(msgId)) {
						return tmp;
					}
				}

				while (headers.hasMoreElements()) {
					Header h = (Header) headers.nextElement();         
					String mID = h.getName();                
					if(mID.contains("Message-ID") || mID.contains("Message-Id")) {
						if (h.getValue().equals(msgId)) {
							fetchedMessage = tmp;
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
			throw new BigBangJewelException(e.getMessage() + " 424 ", e);
		}

		return fetchedMessage;
	}

	/**
	 *	This method gets all attachments on a message. It calls the method which returns
	 * 	a map with attachmentId / Attachment content, and extracts only the contents 
	 */
	public static List<BodyPart> getAttachments(Message message) throws Exception {

		Map<String, BodyPart> attachmentsMap = conditionalGetAttachmentsMap((MimeMessage) message);

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
	public static LinkedHashMap<String, BodyPart> getAttachmentsMap(Message message) throws BigBangJewelException {
		Object content;
		try {
			content = message.getContent(); 
			
			if (content instanceof String)
				return null;        

			if (content instanceof Multipart) {
				Multipart multipart = (Multipart) content;
				LinkedHashMap<String, BodyPart> result = new LinkedHashMap<String, BodyPart>();

				for (int i = 0; i < multipart.getCount(); i++) {
					LinkedHashMap<String, BodyPart> attachmentsMap = getAttachmentsMap(multipart.getBodyPart(i));
					
					for (String key : attachmentsMap.keySet()) {
						BodyPart test = result.get(key);
						if (test == null || key.equals("main")) {
							result.put(key, attachmentsMap.get(key));
						} else {
							// Ups... duplicated key...
							int z=1;
							while (true) {
								z++;
								int lastDot = key.lastIndexOf('.');
								String newKey = key.substring(0,lastDot) + "_" + z + key.substring(lastDot);
								test = result.get(newKey);
								if (test == null) {
									result.put(newKey, attachmentsMap.get(key));
									break;
								}
							}
						}	
					}
				}
				return result;

			}
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage() + " 493 ", e);
		}
		return null;
	}

	/**
	 *	This is a recursive method to get all attachments in a body part. 
	 */
	private static LinkedHashMap<String, BodyPart> getAttachmentsMap(BodyPart part) throws Exception {
		
		LinkedHashMap<String, BodyPart> result = new LinkedHashMap<String, BodyPart>();
		Object content = null;
		try {
			content = part.getContent();
		} catch (DecodingException e) {
			return result;
		} catch (IOException io) {
			if (part.getContentType().contains("TEXT/PLAIN") || part.getContentType().contains("TEXT/HTML")
					|| part.getContentType().contains("text/plain") || part.getContentType().contains("text/html")) { 
				result.put("main", part);
			}
		}
		
		// If it is an attachment, gets its id from the header and inserts it in the result's map
		if (content instanceof InputStream || content instanceof String) {
			if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) || part.getFileName()!=null ) {
				
				String attId = "";
				
				if (part instanceof MimeBodyPart) {
					// If the part is a MimeBodyPart, tries to get the file name in a more direct way
					MimeBodyPart mimePart = (MimeBodyPart) part;
					
					attId = MimeUtility.decodeText(mimePart.getFileName());
					
					if(attId != null) {
						result.put(attId, part);
					}
					
				} else {
					// If it is not possible, iterates the headers
					Enumeration<?> headers = part.getAllHeaders();
					
					while (headers.hasMoreElements()) {
						
						if(part.getHeader("Content-Id") != null) {
							attId = MimeUtility.decodeText(part.getHeader("Content-Id")[0]);
						} else if(part.getHeader("Content-Description") != null) {
							attId = MimeUtility.decodeText(part.getHeader("Content-Description")[0]);
						} else {
							attId = MimeUtility.decodeText(part.getFileName());
						}
						
						if(attId != null) {
							result.put(attId, part);
							break;
						}
					}
				}
				return result;
			} else if (part.getContentType().contains("TEXT/PLAIN") || part.getContentType().contains("TEXT/HTML")
					|| part.getContentType().contains("text/plain") || part.getContentType().contains("text/html")) { 
				result.put("main", part);
			} else {
				return new LinkedHashMap<String, BodyPart>();
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
	public static Map<String, String> processItem(String pstrUniqueID, String folderId, Message fetchedItem, Map<String, BodyPart> mailAttachments) throws BigBangJewelException {

		Map<String, String> processed = null;

		if (fetchedItem == null) {
			fetchedItem = conditionalGetMessage(folderId, pstrUniqueID);
		}

		processed = new HashMap<String, String>();

		processed.put("_", pstrUniqueID);

		try {
			if (mailAttachments == null) {
				mailAttachments = conditionalGetAttachmentsMap((MimeMessage) fetchedItem);
			}
		} catch (Exception e) {
			throw new BigBangJewelException(e.getMessage() + " 588 ", e);
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
		int countFrom = 0;

		String[] to;
		String[] cc;
		String[] bcc;
		String[] replyTo;
		String[] from;

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
			} else if ( Constants.UsageID_From.equals(message.marrAddresses[i].midUsage)) {
				countFrom++;
			}
		}

		to = new String[countTo];
		cc = new String[countCC];
		bcc = new String[countBCC];
		replyTo = new String[countReplyTo];
		from = new String[countFrom];

		countTo = 0;
		countCC = 0;
		countBCC = 0;
		countReplyTo = 0;
		countFrom = 0;

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
			} else if (Constants.UsageID_From.equals(message.marrAddresses[i].midUsage)) {
				from[countFrom] = message.marrAddresses[i].mstrAddress;
				countFrom++;
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

		sendMail(replyTo, to, cc, bcc, from, message.mstrSubject, message.mstrBody, attachments, false);
	}

	/**
	 *	This methods gets a Message, and creates (and returns) a MessageData object with that 
	 *	Message's information
	 */
	public static MessageData getAsData(String pstrUniqueID, String folderId) throws BigBangJewelException {

		// Gets the message with a given ID
		Message fetchedMessage = conditionalGetMessage(folderId, pstrUniqueID);

		if (fetchedMessage != null) {
			
			return messageToData(fetchedMessage, pstrUniqueID);
		}

		return null;
	}

	/**
	 *	Auxiliary method which converts a javax Message to MessageData
	 */
	public static MessageData messageToData(Message fetchedMessage, String emailId)
			throws BigBangJewelException {
		
		MessageData result = new MessageData();
		
		InternetAddress fromAddress = null;
		String from;
		Address[] to, cc, bcc, replyTo;
		int addLength;
		int i;

		// Builds the MessageData with the existing Message's fields
		try {
			result.mstrSubject = fetchedMessage.getSubject();
			result.midOwner = null;
			result.mlngNumber = -1;
			result.mbIsEmail = true;
			
			if (emailId != null) {
				result.mstrEmailID = emailId;
			}
			
			Object content = fetchedMessage.getContent();
			
			LinkedHashMap<String, BodyPart> attachmentsMap = null;
			
			result.mstrBody = null;
			
			if (content instanceof Multipart) {
				
				attachmentsMap = conditionalGetAttachmentsMap((MimeMessage) fetchedMessage);
				result.mstrBody = conditionalGetBody((MimeMessage) fetchedMessage, attachmentsMap);
				
				if (attachmentsMap != null) {
					// Removes the mail's text from the attachments
					attachmentsMap.remove("main");
					MessageAttachmentData[] atts = new MessageAttachmentData[attachmentsMap.size()];
					int u=0;
					for (Map.Entry<String, BodyPart> entry : attachmentsMap.entrySet()) {
						MessageAttachmentData tmp = new MessageAttachmentData();
						tmp.mid = null;
						tmp.midOwner = null;
						tmp.midDocument = null;
						tmp.mstrAttId = entry.getKey();
						atts[u] = tmp;
						u++;
					}
					result.marrAttachments = atts;
				}
			}
			
			if (result.mstrBody == null) {
				result.mstrBody = conditionalGetBody((MimeMessage) fetchedMessage, attachmentsMap);
			}
			
			fromAddress = (InternetAddress) (fetchedMessage.getFrom() == null ? null : fetchedMessage.getFrom()[0]);
			from = fromAddress == null ? "" : fromAddress.getAddress();
			
			to = fetchedMessage.getRecipients(RecipientType.TO);
			cc = fetchedMessage.getRecipients(RecipientType.CC);
			bcc = fetchedMessage.getRecipients(RecipientType.BCC);
			replyTo = fetchedMessage.getReplyTo();
			
			String userEmail = getUserEmail();
			if (userEmail != null) {
				result.midDirection = ( userEmail.equalsIgnoreCase(from) ?
						Constants.MsgDir_Outgoing : Constants.MsgDir_Incoming );
			} else {
				result.midDirection = Constants.MsgDir_Outgoing;
			}
			
			if (result.midDirection.equals(Constants.MsgDir_Outgoing) && fetchedMessage.getSentDate() != null) {
				result.mdtDate = new Timestamp(fetchedMessage.getSentDate().getTime());
			} else if (result.midDirection.equals(Constants.MsgDir_Incoming) && fetchedMessage.getReceivedDate() != null) {
				result.mdtDate = new Timestamp(fetchedMessage.getReceivedDate().getTime());
			} else {
				result.mdtDate = null;
			}
			
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

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
	public static String prepareBodyInline(String body, Map<String, BodyPart> attachmentsMap) throws BigBangJewelException {
		
		// Uses a Regular Expression to find "IMG" tags in html
		final String regex = "src\\s*=\\s*([\"'])?([^\"']*)";
		final Pattern p = Pattern.compile(regex);
        final Matcher m = p.matcher(body);
		
        // Iterates the found images
        while (m.find()) {
        	// Gets the image's name from html, and adapts it to use as a key to fetch the 
        	// image from the attachments' map
        	String originalHtml = m.group();
			String prevImg = "<" + originalHtml.substring(9, originalHtml.length()) + ">";
			BodyPart imageBodyPart = attachmentsMap.get(prevImg);
			if (imageBodyPart == null) {
				// Trying to get for "weird" image names...
				prevImg = prevImg.replaceAll(".(.*?(?:jpg|png|jpeg|gif|tif|tiff))|.*","$1");
				imageBodyPart = attachmentsMap.get(prevImg);
			}
			if (imageBodyPart!=null) {
				
				// Gets the encoded base-64 String, ready to be used in HTML's IMG's SRC
				try {
					byte[] binaryData = null;
					try {
						binaryData = IOUtils.toByteArray(imageBodyPart.getInputStream());
					} catch (DecodingException d) {
						// Removes the inline image from the attachments
						attachmentsMap.remove(prevImg);
						continue;
					}
					String encodedString = Base64.encodeBase64String(binaryData);
					
					String dataType = imageBodyPart.getContentType();
					String[] splittedType = dataType.split(";");
					dataType = "data:" + splittedType[0] + ";base64,"; 
					
					// Changes HTML's IMG tag
					body = body.replaceAll(originalHtml, "src=\"" + dataType + encodedString);
					
					// Removes the inline image from the attachments
					attachmentsMap.remove(prevImg);
										
				} catch (Throwable e) {
					throw new BigBangJewelException(e.getMessage() + " 884 ", e);
				}
			}
        }
		
		return body;
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
		result.mstrDisplay = displayName==null ? result.mstrAddress : displayName;

		return result;
	}

	/**
	 *	This method returns an attachment, giving a message ID and an attachment ID
	 *	It calls the methods to retrieve a message, and that message's attachments. 
	 */
	public static FileXfer getAttachment(String msgId, String folderId, String attachmentId) throws BigBangJewelException {

		// Gets a message from a given folder, with a given ID
		Message message = conditionalGetMessage(folderId, msgId);

		LinkedHashMap<String, BodyPart> messageAttachments = conditionalGetAttachmentsMap((MimeMessage) message);
		BodyPart attachment; 

		InputStream attachmentStream;
		try {
			attachment = messageAttachments.get(attachmentId);
			attachmentStream = attachment==null ? null : attachment.getInputStream();
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
	 *	This method gets all "first level" folders, or all folders inside a folder
	 */
	public static Folder[] getAllFolders() throws BigBangJewelException {

		Folder[] result = null;
		
		try {
			OAuthHandler.initialize();
			initializeStore();
			
			String userMail = getUserEmail();
			result = storesByUser.get(userMail).getDefaultFolder().list("*");

		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}

		return result;
	}
	
	/**
	 * This method "stores" in memory the users' folders list
	 * @throws BigBangJewelException 
	 */
	public static void storeLastMessage(Message msg) throws BigBangJewelException {
		
		if (lastMessageUser == null) {
			lastMessageUser = new HashMap<String, Message>();
		}
		
		try {
			String userMail = getUserEmail();
			lastMessageUser.put(userMail, msg);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}		
	}
	
	/**
	 * This method returns the stored users' message
	 */
	public static Message getStoredMessage() throws BigBangJewelException {
		
		Message result = null; 
				
		if (lastMessageUser == null) {
			lastMessageUser = new HashMap<String, Message>();
		}
		
		try {
			String userMail = getUserEmail();
			result = lastMessageUser.get(userMail);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage() + " 1063 ", e);
		}
		
		return result;
	}
	
	/**
	 * This method "stores" in memory the users' current attachments
	 * @param mailMessage 
	 */
	public static void storeLastAttachments(MimeMessage mailMessage, LinkedHashMap<String, BodyPart> atts) throws BigBangJewelException {
		
		if (lastAttachmentsMapUser == null) {
			lastAttachmentsMapUser = new HashMap<String, LinkedHashMap<String, BodyPart>>();
		}
		
		try {
			String key = getUserEmail() + "_|_" + mailMessage.getHeader("Message-Id")[0];
			lastAttachmentsMapUser.put(key, atts);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}		
	}
	
	/**
	 * This method returns the stored users' attachments
	 */
	public static LinkedHashMap<String, BodyPart> getStoredAttachments(MimeMessage mailMessage) throws BigBangJewelException {
		
		LinkedHashMap<String, BodyPart> result = null; 
		
		try {
			String key = getUserEmail() + "_|_" + mailMessage.getHeader("Message-Id")[0];
			result = lastAttachmentsMapUser==null? null : lastAttachmentsMapUser.get(key);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage() + " 1116 ", e);
		}
		
		return result;
	}
	
	/**
	 * This method "stores" in memory the users' current ready to display message
	 */
	public static void storeLastPreparedBody(MimeMessage mailMessage, String body) throws BigBangJewelException {
		
		if (lastPreparedBodyUser == null) {
			lastPreparedBodyUser = new HashMap<String, String>();
		}
		
		try {
			String key = getUserEmail() + "_|_" + mailMessage.getHeader("Message-Id")[0];
			lastPreparedBodyUser.put(key, body);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage(), e);
		}		
	}
	
	/**
	 * This method returns the stored users' attachments
	 */
	public static String getStoredPreparedBody(MimeMessage mailMessage) throws BigBangJewelException {
		
		String result = null; 
		
		try {
			String key = getUserEmail() + "_|_" + mailMessage.getHeader("Message-Id")[0];
			result = lastPreparedBodyUser==null ? null : lastPreparedBodyUser.get(key);
		} catch (Throwable e) {
			throw new BigBangJewelException(e.getMessage() + " 1116 ", e);
		}
		
		return result;
	}
	
	public static String conditionalGetBody(MimeMessage mailMessage,
			LinkedHashMap<String, BodyPart> attachmentsMap) throws BigBangJewelException {
		
		String result = getStoredPreparedBody(mailMessage);
		
		if (result==null) {
			try {
				Object content = mailMessage.getContent();
				if (content instanceof Multipart && attachmentsMap != null) {
					result = attachmentsMap.get("main").getContent().toString();
					result = prepareBodyInline(result, attachmentsMap);
				} else {
					result = prepareSimpleBody(content.toString());
				}
			} catch (Exception e) {
				throw new BigBangJewelException("Problems while getting the stored prepared body " + e.getMessage());
			}
			storeLastPreparedBody(mailMessage, result);
		}

		return result;
	}

	/**
	 * Tries to get the stored attachments map, from a message
	 */
	public static LinkedHashMap<String, BodyPart> conditionalGetAttachmentsMap(
			MimeMessage mailMessage) throws BigBangJewelException {
		
		LinkedHashMap<String, BodyPart> attachmentsMap = getStoredAttachments(mailMessage);
		
		if (attachmentsMap == null) {
			attachmentsMap = getAttachmentsMap(mailMessage);
			storeLastAttachments(mailMessage, attachmentsMap);
		}	
		
		return attachmentsMap;
	}

	/**
	 * Tries to get the stored message, so it does not have to fetch it from gmail.
	 * If it is not possible, gets the message
	 */
	public static MimeMessage conditionalGetMessage(String folderId, String id)
			throws BigBangJewelException {
		
		MimeMessage mailMessage = (MimeMessage) getStoredMessage();
		
		try {
			if (mailMessage != null && mailMessage.getFolder() != null && 
					mailMessage.getFolder().getName() != null && mailMessage.getFolder().getFullName().equals(folderId) &&
					mailMessage.getHeader("Message-Id")[0]!=null && mailMessage.getHeader("Message-Id")[0].equals(id)) {
			
				return mailMessage;
			}			
		} catch (MessagingException e) {
			throw new BigBangJewelException("Error while checking stored message's existence: " + e.getMessage());
		}		
		
		mailMessage = (MimeMessage) getMessage(id, folderId);
		storeLastMessage(mailMessage);
		
		return mailMessage;
	}
	
	/**
	 * Clears the stored message, attachments'map or prepared bodies, on demand.
	 */
	public static void clearStoredValues(boolean clearMessage,
			boolean clearAttsMap, boolean clearPreparedBody,
			MimeMessage mailMessage) throws BigBangJewelException {

		String userKey = getUserEmail();
		String fullKey = userKey;

		try {
			fullKey = fullKey + "_|_" + mailMessage.getHeader("Message-Id")[0];
		} catch (MessagingException e) {
			throw new BigBangJewelException(
					"Error while cleaning stored values: " + e.getMessage());
		}

		if (clearMessage) {
			if (getStoredMessage() != null) {
				lastMessageUser.remove(userKey);
			}
		}

		if (clearAttsMap) {
			if (getStoredAttachments(mailMessage) != null) {
				lastAttachmentsMapUser.remove(fullKey);
			}
		}

		if (clearPreparedBody) {
			if (getStoredPreparedBody(mailMessage) != null) {
				lastPreparedBodyUser.remove(fullKey);
			}
		}
	}
}
