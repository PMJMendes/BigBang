package bigBang.library.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.ArrayUtils;

import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
import bigBang.definitions.shared.DocInfo;
import bigBang.definitions.shared.Document;
import bigBang.library.interfaces.ExchangeService;
import bigBang.library.shared.AttachmentStub;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.ExchangeItem;
import bigBang.library.shared.ExchangeItemStub;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;


public class ExchangeServiceImpl
	extends EngineImplementor
	implements ExchangeService
{
	private static final long serialVersionUID = 1L;

	private static ExchangeItemStub[] sToClient(Message[] parrSource)
			throws BigBangException {

		String lstrEmail;
		ExchangeItemStub[] larrResults;
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

		larrResults = new ExchangeItemStub[parrSource.length];

		for ( i = 0; i < larrResults.length; i++ )
		{
			larrResults[i] = new ExchangeItemStub();

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
					larrResults[i].timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(parrSource[i].getSentDate());
				}
				catch (Throwable e)
				{
					larrResults[i].timestamp = null;
				}

				if ((lstrFrom.length() > 0) && (lstrEmail != null))
					larrResults[i].isFromMe = lstrEmail.equalsIgnoreCase(lstrFrom);
				else
					larrResults[i].isFromMe = false;

				Map<String, BodyPart> attachmentsMap = MailConnector.getAttachmentsMap (parrSource[i]);
				larrResults[i].attachmentCount = attachmentsMap==null ? 0 : attachmentsMap.size();
				
				Object content = parrSource[i].getContent();
				
				if ( content.toString() == null )
					larrResults[i].bodyPreview = "_";
				else
				{
					if (content instanceof Multipart && attachmentsMap != null) {
						lstrBody = attachmentsMap.get("main").getContent().toString();
						lstrBody = MailConnector.prepareBodyInline(lstrBody, attachmentsMap);
					} else {
						lstrBody = content.toString();
					}
					lstrBody = MailConnector.removeHtml(lstrBody);
					if ( lstrBody.length() > 170 ) {
						larrResults[i].bodyPreview = lstrBody.substring(0, 170);
					}
				}
			} catch (Throwable e) {
				throw new BigBangException(e.getMessage(), e);
			}
		}

		return larrResults;
	}
	
	private static ExchangeItemStub[] sToClient(javax.mail.Folder[] folders)
			throws BigBangException { 
		
		ExchangeItemStub[] larrResults = null;
		
		if (folders == null) {
			return null;
		}
		
		larrResults = new ExchangeItemStub[folders.length];
		
		for (int i = 0; i < larrResults.length; i++ ) {
			
			larrResults[i] = new ExchangeItemStub();
			
			try {
				
				// TODO ver folder.getURLName() e folder.getFullName() e mudar nos outros sitios onde usas a 
				// folder (o fetch) para ver se esta a ir buscar bem por nome
				larrResults[i].id = folders[i].getName(); 
				larrResults[i].isFolder = true;
				larrResults[i].isFromMe = false;
				larrResults[i].subject = folders[i].getName();
				larrResults[i].from = null;
				larrResults[i].timestamp = null;
				larrResults[i].attachmentCount = -1;
				larrResults[i].bodyPreview = null;
				larrResults[i].folderId = folders[i].getFullName();
			} catch (Throwable e) {
				throw new BigBangException(e.getMessage(), e);
			}
		}
		
		return larrResults;
	}

	public ExchangeItemStub[] getItems()
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

		return sToClient(larrItems);
	}

	public ExchangeItemStub[] getItemsAll()
		throws SessionExpiredException, BigBangException
	{
		
		return getItemsAll(null);
	}
	
	private ExchangeItemStub[] getItemsAll(String folderId)
			throws SessionExpiredException, BigBangException
		{
			Message[] items;
			Folder[] folders;

			if ( Engine.getCurrentUser() == null )
				throw new SessionExpiredException();

			try {
				items = MailConnector.getMails(folderId, false);
				folders = MailConnector.getFolders(folderId);
			}
			catch (Throwable e) {
				throw new BigBangException(e.getMessage(), e);
			}

			ExchangeItemStub[] mailItems = items==null ? null : sToClient(items);
			ExchangeItemStub[] folderItems = folders==null ? null : sToClient(folders);
			
			return ArrayUtils.addAll(folderItems, mailItems);
		}
	
	@Override
	public ExchangeItemStub[] getFolder(String id)
			throws SessionExpiredException, BigBangException {
		return getItemsAll(id);
	}

	public ExchangeItem getItem(String folderId, String id)
		throws SessionExpiredException, BigBangException
	{
		MimeMessage lobjItem;
		ExchangeItem lobjResult;
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

			lobjResult = new ExchangeItem();
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
				lobjResult.bodyPreview = "(Erro interno do servidor de Exchange.)";
			}
			
			if (attachmentsMap != null) {

				// Removes the mail's text from the attachments
				attachmentsMap.remove("main");
				
				for (Map.Entry<String, BodyPart> entry : attachmentsMap.entrySet()) {
				    System.out.println(entry.getKey() + "/" + entry.getValue());
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
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return lobjResult;
	}

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
}
