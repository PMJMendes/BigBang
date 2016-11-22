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
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.ArrayUtils;

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

import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;
import com.premiumminds.BigBang.Jewel.SysObjects.StorageConnector;


public class MailServiceImpl
	extends EngineImplementor
	implements MailService
{
	private static final long serialVersionUID = 1L;

	/**
	 * Prepares the data from a mail list in an intelligible way to to the client
	 */
	private static MailItemStub[] sToClient(Message[] parrSource)
			throws BigBangException {

		String lstrEmail;
		MailItemStub[] larrResults;
		int i;
		String lstrFrom;
		String lstrBody;

		try
		{
			lstrEmail = MailConnector.getUserEmail();
		}
		catch (Throwable e)
		{
			lstrEmail = null;
		}

		larrResults = new MailItemStub[parrSource.length];

		for ( i = 0; i < larrResults.length; i++ )
		{
			larrResults[i] = new MailItemStub();

			try {

				larrResults[i].id = "" + parrSource[i].getHeader("Message-Id")[0];
				larrResults[i].isFolder = false;
				larrResults[i].subject = parrSource[i].getSubject();
				larrResults[i].folderId = parrSource[i].getFolder().getFullName();

				InternetAddress address = (InternetAddress) (parrSource[i].getFrom() == null ? null : parrSource[i].getFrom()[0]);
				lstrFrom = address == null ? "" : address.getAddress();
				larrResults[i].from = lstrFrom;

				try
				{
					larrResults[i].timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(parrSource[i].getReceivedDate());
				}
				catch (Throwable e)
				{
					larrResults[i].timestamp = null;
				}

				if ((lstrFrom.length() > 0) && (lstrEmail != null))
					larrResults[i].isFromMe = lstrEmail.equalsIgnoreCase(lstrFrom);
				else
					larrResults[i].isFromMe = false;

				/*Map<String, BodyPart> attachmentsMap = MailConnector.getAttachmentsMap (parrSource[i]);
				larrResults[i].attachmentCount = attachmentsMap==null ? 0 : attachmentsMap.size();*/
				
				larrResults[i].attachmentCount = attachmentsNumber(parrSource[i]);
				
				Object content = parrSource[i].getContent();
				
				larrResults[i].bodyPreview = "_";
				
				/*if ( content.toString() == null )
					larrResults[i].bodyPreview = "_";
				else
				{ */
					/*if (content instanceof Multipart && attachmentsMap != null && attachmentsMap.size() != 0) {
						lstrBody = attachmentsMap.get("main").getContent().toString();
						lstrBody = MailConnector.prepareBodyInline(lstrBody, attachmentsMap);
					} else {
						lstrBody = content.toString();
					} */
				/*	lstrBody = getTextFromMessage(parrSource[i]);
					lstrBody = MailConnector.removeHtml(lstrBody);
					if ( lstrBody.length() > 170 ) {
						larrResults[i].bodyPreview = lstrBody.substring(0, 170);
					}
				} */
			} catch (Throwable e) {
				throw new BigBangException(e.getMessage(), e);
			}
		}

		return larrResults;
	}
	
	private static String getTextFromMessage(Message message) throws Exception {
	    String result = "";
	    if (message.isMimeType("text/plain")) {
	        result = message.getContent().toString();
	    } else if (message.isMimeType("multipart/*")) {
	        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
	        result = getTextFromMimeMultipart(mimeMultipart);
	    }
	    return result;
	}

	private static String getTextFromMimeMultipart(
	        MimeMultipart mimeMultipart) throws Exception{
	    String result = "";
	    int count = mimeMultipart.getCount();
	    for (int i = 0; i < count; i++) {
	        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
	        if (bodyPart.isMimeType("text/plain")) {
	            result = result + "\n" + bodyPart.getContent();
	            break; // without break same text appears twice in my tests
	        } else if (bodyPart.isMimeType("text/html")) {
	            String html = (String) bodyPart.getContent();
	            //result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
	            result = html;
	        } else if (bodyPart.getContent() instanceof MimeMultipart){
	            result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
	        }
	    }
	    return result;
	}
	
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
	 * Prepares the data from a mail in an intelligible way to to the client
	 */
	private static MailItemStub[] sToClient(MailItemStub current, javax.mail.Folder[] folders)
			throws BigBangException { 
		
		MailItemStub[] larrResults = null; 
		
		int arrSize = (folders==null && current==null) ? 0 : 
			folders == null ? 1 : 
			(current == null || (current.isParentFolder && (current.parentFolderId==null || current.parentFolderId.length()==0))) ? folders.length : folders.length + 1; 
		
		int start = 0;
		
		larrResults = new MailItemStub[arrSize];
		
		// sets the parent folder, used to allow a back
		if (current != null && (!current.isParentFolder || (current.parentFolderId!=null && current.parentFolderId.length() > 0))) {
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
			larrResults[0].parentFolderId = current.isParentFolder ? getBackFolder(current.parentFolderId) : current.parentFolderId;
			
			start++;
		}
		
		for (int u=0; start < larrResults.length; start++, u++) {
			
			larrResults[start] = new MailItemStub();
			
			try {
				
				larrResults[start].id = folders[u].getName(); 
				larrResults[start].isFolder = true;
				larrResults[start].isFromMe = false;
				larrResults[start].subject = folders[u].getName();
				larrResults[start].from = null;
				larrResults[start].timestamp = null;
				larrResults[start].attachmentCount = -1;
				larrResults[start].bodyPreview = null;
				larrResults[start].folderId = folders[u].getFullName();
				larrResults[start].isParentFolder = false; 
				larrResults[start].parentFolderId = 
						(current == null || (current.isParentFolder && current.parentFolderId==null)) ? null : 
						(current.isParentFolder && current.parentFolderId!=null) ? current.parentFolderId : current.folderId;
			} catch (Throwable e) {
				throw new BigBangException(e.getMessage(), e);
			}
		}
		
		return larrResults;
	}

	/**
	 * This method gets the "back" folder, when navigating in the mail folders
	 */
	private static String getBackFolder(String parentFolderId) {
		
		String[] parts = parentFolderId.split("/");
		
		String result = "";
		
		for (int i=0;i<parts.length - 1;i++) {
			if (result.length() > 0) {
				result = result + "/";
			}
			result = result + parts[i];
		}
		
		if (result.length() == 0) {
			return null;
		}
		
		return result;
	}

	/**
	 * Gets the initial items in a mailbox (corresponding to inbox)
	 */
	public MailItemStub[] getItems()
		throws SessionExpiredException, BigBangException
	{
		Message[] larrItems;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try {
			larrItems = MailConnector.getMails(null, false);
		}
		catch (Throwable e) {
			throw new BigBangException(e.getMessage(), e);
		}
		
		MailItemStub[] mailItems = larrItems==null ? null : sToClient(larrItems);
		Folder sent = null;
		try {
			sent = MailConnector.getFolder("Correio enviado");
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage(), e);
		}
		
		Folder [] foldArray = new Folder[1];
		
		foldArray[0] = sent;
		
		MailItemStub[] sentFolder = null;
		
		if (sent!=null) {
			sentFolder = sToClient(null, foldArray);
		}
		
		if (sentFolder != null) {
			return ArrayUtils.addAll(sentFolder, mailItems);
		}
		
		if (larrItems.length > 0) {
			closeFolderAndStore((MimeMessage) larrItems[0]);
		}

		return mailItems;
	}

	public MailItemStub[] getItemsAll()
		throws SessionExpiredException, BigBangException
	{
		
		return getItemsAll(null);
	}
	
	/**
	 * Gets all items and folders in root
	 */
	private MailItemStub[] getItemsAll(MailItemStub current)
			throws SessionExpiredException, BigBangException
		{
			Message[] items;
			Folder[] folders;
			
			String folderId = current == null ? null : 
				current.isParentFolder ? current.parentFolderId : current.folderId;

			if ( Engine.getCurrentUser() == null )
				throw new SessionExpiredException();

			try {
				items = MailConnector.getMails(folderId, false);
				folders = MailConnector.getFolders(folderId);
			}
			catch (Throwable e) {
				throw new BigBangException(e.getMessage(), e);
			}

			MailItemStub[] mailItems = items==null ? null : sToClient(items);
			MailItemStub[] folderItems = (folders==null && current == null) ? null : sToClient(current, folders);
			
			if (items.length > 0) {
				closeFolderAndStore((MimeMessage) items[0]);
			}
			
			return ArrayUtils.addAll(folderItems, mailItems);
		}
	
	@Override
	public MailItemStub[] getFolder(MailItemStub current) throws SessionExpiredException, BigBangException {
		return getItemsAll(current);
	}

	/**
	 * Gets a given item in a given folder
	 */
	public MailItem getItem(String folderId, String id)
		throws SessionExpiredException, BigBangException
	{
		MimeMessage lobjItem;
		MailItem lobjResult;
		String lstrFrom;
		ArrayList<AttachmentStub> larrStubs;
		AttachmentStub lobjAttStub;
		String lstrBody;
		Map<String, BodyPart> attachmentsMap = null;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjItem = (MimeMessage) MailConnector.getMessage(id, folderId);

			lobjResult = new MailItem();
			lobjResult.folderId = lobjItem.getFolder().getFullName();
			lobjResult.id = lobjItem.getMessageID();
			lobjResult.isFolder = false;
			lobjResult.subject = lobjItem.getSubject();
			InternetAddress address = (InternetAddress) (lobjItem.getFrom() == null ? null : lobjItem.getFrom()[0]);
			lstrFrom = address == null ? "" : address.getAddress();
			lobjResult.from = lstrFrom.length()>0 ? lstrFrom : null;
			lobjResult.isFromMe = (lstrFrom.length()>0 && lstrFrom.equals(MailConnector.getUserEmail()));
			
			lobjResult.timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(lobjItem.getSentDate());
			
			attachmentsMap = MailConnector.getAttachmentsMap(lobjItem);
			
			lobjResult.attachmentCount = attachmentsMap==null ? 0 : attachmentsMap.size();
			Object content = lobjItem.getContent();
			larrStubs = new ArrayList<AttachmentStub>();
			
			try
			{
				if (content instanceof Multipart && attachmentsMap != null) {
					lstrBody = attachmentsMap.get("main").getContent().toString();
					lstrBody = MailConnector.prepareBodyInline(lstrBody, attachmentsMap);
					lobjResult.body = lstrBody;
					lobjResult.bodyPreview = lobjResult.subject;
				} else {
					lstrBody = content.toString();
					lobjResult.body = MailConnector.prepareSimpleBody(lstrBody);
				}
				lstrBody = MailConnector.removeHtml(lstrBody);
				if ( lstrBody.length() > 170 ) {
					lobjResult.bodyPreview = lstrBody.substring(0, 170);
				}
			}
			catch (Throwable e)
			{
				lobjResult.bodyPreview = "(Erro interno do servidor de Email.)";
			}
			
			if (attachmentsMap != null) {

				// Removes the mail's text from the attachments
				attachmentsMap.remove("main");
				
				for (Map.Entry<String, BodyPart> entry : attachmentsMap.entrySet()) {
				    lobjAttStub = new AttachmentStub();
				    lobjAttStub.id = entry.getKey();
					lobjAttStub.fileName = entry.getValue().getFileName();
					String contentType = entry.getValue().getContentType();
					lobjAttStub.mimeType = contentType!=null ? contentType.split(";")[0] : null;
					lobjAttStub.size = entry.getValue().getSize();
					larrStubs.add(lobjAttStub);
				}
				
				lobjResult.attachments = larrStubs.toArray(new AttachmentStub[larrStubs.size()]);
				
			}
			
			closeFolderAndStore(lobjItem);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return lobjResult;
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
