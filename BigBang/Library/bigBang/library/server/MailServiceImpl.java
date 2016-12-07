package bigBang.library.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.Document;
import bigBang.library.interfaces.MailService;
import bigBang.library.shared.AttachmentStub;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.MailItem;
import bigBang.library.shared.MailItemStub;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.Constants;
import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;
import com.premiumminds.BigBang.Jewel.SysObjects.StorageConnector;


public class MailServiceImpl
	extends EngineImplementor
	implements MailService
{
	private static final long serialVersionUID = 1L;
	
	private static MailItemStub[] storedFolders;
	
	/**
	 * Prepares the data from a mails' list to be displayed
	 */
	private static MailItemStub[] messagesToClient(Message[] parrSource)
			throws BigBangException {

		String lstrEmail;
		MailItemStub[] larrResults;
		int i;
		String lstrFrom;
		
		try {
			lstrEmail = MailConnector.getUserEmail();
		} catch (Throwable e) {
			lstrEmail = null;
		}

		int size = parrSource==null ? 0 : parrSource.length;
		
		larrResults = new MailItemStub[size+1];
		
		// sets the parent "back" folder
		larrResults[0] = new MailItemStub();
		larrResults[0].isParentFolder = true;
		larrResults[0].id = "Voltar";
		larrResults[0].isFolder = true;
		larrResults[0].isFromMe = false;
		larrResults[0].subject = "Voltar";
		larrResults[0].from = null;
		larrResults[0].timestamp = null;
		larrResults[0].attachmentCount = -1;
		larrResults[0].bodyPreview = null;
		larrResults[0].folderId = null;
		larrResults[0].isParentFolder = true; 
		larrResults[0].parentFolderId = "bck";
				
		if (parrSource==null || parrSource.length==0) {
			return larrResults;
		}
		
		String folderId = parrSource[0].getFolder().getFullName();	
		
		for ( i = 1; i < larrResults.length; i++ )
		{
			larrResults[i] = new MailItemStub();

			try {

				Message message = parrSource[i-1];
				
				larrResults[i].id = "" + message.getHeader("Message-Id")[0];
				
				larrResults[i].isFolder = false;
				
				larrResults[i].subject = message.getSubject();
				
				larrResults[i].folderId = folderId;
				
				InternetAddress address = (InternetAddress) (message.getFrom() == null ? null : message.getFrom()[0]);
				lstrFrom = address == null ? "" : address.getAddress();
				larrResults[i].from = lstrFrom;
				
				try
				{
					larrResults[i].timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(message.getReceivedDate());
				}
				catch (Throwable e)
				{
					larrResults[i].timestamp = null;
				}

				if ((lstrFrom.length() > 0) && (lstrEmail != null))
					larrResults[i].isFromMe = lstrEmail.equalsIgnoreCase(lstrFrom);
				else
					larrResults[i].isFromMe = false;

				larrResults[i].attachmentCount = attachmentsNumber(message);
				
				larrResults[i].bodyPreview = "_";
				
			} catch (Throwable e) {
				throw new BigBangException(e.getMessage(), e);
			}
		}

		return larrResults;
	}

	/**
	 * Prepares the data from a folders' list to be displayed to the client
	 */
	private static MailItemStub[] foldersToClient(Folder[] parrSource)
			throws BigBangException {

		MailItemStub[] result;

		result = new MailItemStub[parrSource.length];

		for (int i = 0; i < result.length; i++ )
		{
			result[i] = new MailItemStub();

			try {
				
				String tempFolderName = parrSource[i].getFullName();
				String cleanName = tempFolderName;
				
				result[i].id = tempFolderName; 
				result[i].isFolder = true;
				result[i].isFromMe = false;
				result[i].subject = cleanName;
				result[i].from = null;
				result[i].timestamp = null;
				result[i].bodyPreview = null;
				result[i].folderId = tempFolderName;
				result[i].isParentFolder = false; 
				result[i].parentFolderId = null;
				
			} catch (Throwable e) {
				throw new BigBangException(e.getMessage(), e);
			}
		}

		storedFolders = result;
		
		return result;
	}
	
	/**
	 * Counts the number of attachments in a message
	 */
	private static int attachmentsNumber(Message msg) throws BigBangException {

		try {
			if (msg.isMimeType("multipart/mixed")) {
				Multipart mp = (Multipart)msg.getContent();
				return mp.getCount();
			}
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage(), e);
		}
		return 0;
	}
	
	
	/**
	 * Gets the initial/most used folders, as defined in the constants
	 * No call to the mail service is needed, so it can make the process faster.
	 */
	public MailItemStub[] getItems()
		throws SessionExpiredException, BigBangException
	{
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();
		
		MailItemStub[] result = new MailItemStub[Constants.GoogleAppsConstants.INITIAL_FOLDERS.length];
		
		// Iterates the folders defined as the ones to display in the initial
		// page, and creates MailItemStubs for them
		for (int i=0; i<Constants.GoogleAppsConstants.INITIAL_FOLDERS.length; i++) {
			String tempFolderName = Constants.GoogleAppsConstants.INITIAL_FOLDERS[i];
			String cleanName = tempFolderName;
			if (tempFolderName.contains(Constants.GoogleAppsConstants.GMAIL_FOLDER_NAME)) {
				cleanName = cleanName.substring(Constants.GoogleAppsConstants.GMAIL_FOLDER_NAME.length() + 1);
			}
			result[i] = new MailItemStub();
			result[i].id = tempFolderName; 
			result[i].isFolder = true;
			result[i].isFromMe = false;
			result[i].subject = cleanName;
			result[i].from = null;
			result[i].timestamp = null;
			result[i].attachmentCount = -1;
			result[i].bodyPreview = null;
			result[i].folderId = tempFolderName;
			result[i].isParentFolder = false; 
			result[i].parentFolderId = null;
		}
		
		storedFolders = result;
		
		return result;
	}
	
	public MailItemStub[] getItemsAll() throws BigBangException, SessionExpiredException {
		
		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();
		
		Folder [] folders = null;
		
		try {
			folders = MailConnector.getAllFolders();
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage(), e);
		}
		
		MailItemStub[] folderItems = null;
		
		if (folders != null && folders.length > 0) {
			folderItems = foldersToClient(folders);
			closeFolderAndStore(folders[0]);
		}
		
		return folderItems;
	}
	
	public MailItemStub[] getStoredItems() {
		
		return storedFolders;
	}

	/**
	 * Gets all items and folders in root
	 */
	public MailItemStub[] getFolder(MailItemStub current)
			throws SessionExpiredException, BigBangException
		{
			Message[] items;
			
			String folderId = current.folderId;

			if ( Engine.getCurrentUser() == null )
				throw new SessionExpiredException();

			try {
				items = MailConnector.getMailsFast(folderId, false);
			}
			catch (Throwable e) {
				throw new BigBangException(e.getMessage(), e);
			}

			MailItemStub[] mailItems = null;
			
			mailItems = messagesToClient(items);
			
			if (items != null && items.length > 0) {
				closeFolderAndStore((MimeMessage) items[0]);
			}
			
			return mailItems;
		}

	/**
	 * Gets a given item in a given folder
	 */
	public MailItem getItem(String folderId, String id)
		throws SessionExpiredException, BigBangException
	{
		MimeMessage mailMessage;
		MailItem result;
		String from;
		ArrayList<AttachmentStub> attStubsList;
		AttachmentStub attachmentStub;
		String body;
		Map<String, BodyPart> attachmentsMap = null;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			mailMessage = (MimeMessage) MailConnector.getMessage(id, folderId);

			result = new MailItem();
			result.folderId = mailMessage.getFolder().getFullName();
			result.id = mailMessage.getMessageID();
			result.isFolder = false;
			result.subject = mailMessage.getSubject();
			InternetAddress address = (InternetAddress) (mailMessage.getFrom() == null ? null : mailMessage.getFrom()[0]);
			from = address == null ? "" : address.getAddress();
			result.from = from.length()>0 ? from : null;
			result.isFromMe = (from.length()>0 && from.equals(MailConnector.getUserEmail()));
			
			result.timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(mailMessage.getSentDate());
			
			attachmentsMap = MailConnector.getAttachmentsMap(mailMessage);
			
			result.attachmentCount = attachmentsMap==null ? 0 : attachmentsMap.size();
			Object content = mailMessage.getContent();
			attStubsList = new ArrayList<AttachmentStub>();
			
			try
			{
				if (content instanceof Multipart && attachmentsMap != null) {
					body = attachmentsMap.get("main").getContent().toString();
					body = MailConnector.prepareBodyInline(body, attachmentsMap);
					result.body = body;
				} else {
					body = content.toString();
					result.body = MailConnector.prepareSimpleBody(body);
				}
				body = MailConnector.removeHtml(body);
			}
			catch (Throwable e)
			{
				result.bodyPreview = "(Erro interno do servidor de Email.)";
			}
			
			if (attachmentsMap != null) {

				// Removes the mail's text from the attachments
				attachmentsMap.remove("main");
				
				for (Map.Entry<String, BodyPart> entry : attachmentsMap.entrySet()) {
				    attachmentStub = new AttachmentStub();
				    attachmentStub.id = entry.getKey();
					attachmentStub.fileName = entry.getKey();
					String contentType = entry.getValue().getContentType();
					attachmentStub.mimeType = contentType!=null ? contentType.split(";")[0] : null;
					attachmentStub.size = entry.getValue().getSize();
					attStubsList.add(attachmentStub);
				}
				
				result.attachments = attStubsList.toArray(new AttachmentStub[attStubsList.size()]);
				
			}
			
			closeFolderAndStore(mailMessage);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return result;
	}

	protected void closeFolderAndStore(MimeMessage lobjItem)
			throws BigBangException {
		try {
			Store storeRef = lobjItem.getFolder().getStore();
			lobjItem.getFolder().close(false);
			storeRef.close();
		} catch (Throwable e){
			throw new BigBangException(e.getMessage(), e);
		}	
	}
	
	protected void closeFolderAndStore(Folder lobjItem)
			throws BigBangException {
		try {
			Store storeRef = lobjItem.getStore();
			storeRef.close();
		} catch (Throwable e){
			throw new BigBangException(e.getMessage(), e);
		}	
	}

	/**
	 * Gets an attachment from the email as a doc
	 */
	public Document getAttAsDoc(String emailId, String folderId, String attachmentId)
		throws SessionExpiredException, BigBangException
	{
		Document lobjResult;
		FileXfer lobjFile;
		UUID lidKey;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjFile = MailConnector.getAttachment(emailId, folderId, attachmentId);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		lobjResult = new Document();

		lobjResult.id = null;
		lobjResult.name = lobjFile.getFileName();
		lobjResult.ownerTypeId = null;
		lobjResult.ownerId = null;
		lobjResult.docTypeId = null;
		lobjResult.docTypeLabel = "(Anexo)";
		lobjResult.creationDate = null;
		lobjResult.text = null;
		lobjResult.hasFile = true;
    	lidKey = UUID.randomUUID();
    	FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);
    	lobjResult.mimeType = lobjFile.getContentType();
    	lobjResult.fileName = lobjFile.getFileName();
    	lobjResult.fileStorageId = lidKey.toString();

		lobjResult.parameters = new DocInfo[0];

		return lobjResult;
	}
	
	/**
	 * Gets an attachment from the email in the storage as a doc
	 */
	public Document getAttAsDocFromStorage(String storageId, String attachmentId) throws BigBangException, SessionExpiredException {
		
		Document lobjResult;
		FileXfer lobjFile;
		UUID lidKey;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjFile = StorageConnector.getAttachmentAsFileXfer(storageId, attachmentId);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}
		
		if (lobjFile == null) {
			throw new BigBangException("Não foi possível obter o attachment da storage");
		}

		lobjResult = new Document();

		lobjResult.id = null;
		lobjResult.name = lobjFile.getFileName();
		lobjResult.ownerTypeId = null;
		lobjResult.ownerId = null;
		lobjResult.docTypeId = null;
		lobjResult.docTypeLabel = "(Anexo)";
		lobjResult.creationDate = null;
		lobjResult.text = null;
		lobjResult.hasFile = true;
    	lidKey = UUID.randomUUID();
    	FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);
    	lobjResult.mimeType = lobjFile.getContentType();
    	lobjResult.fileName = lobjFile.getFileName();
    	lobjResult.fileStorageId = lidKey.toString();

		lobjResult.parameters = new DocInfo[0];

		return lobjResult;
	}
}
