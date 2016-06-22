package bigBang.library.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

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
		Address[] lobjFrom;
		String lobjBody;
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

				lobjFrom = parrSource[i].getFrom();
				larrResults[i].from = (String) ( lobjFrom == null ? null :
					(lobjFrom[0] == null ? "" : lobjFrom[0].toString()) );

				try
				{
					larrResults[i].timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(parrSource[i].getSentDate());
				}
				catch (Throwable e)
				{
					larrResults[i].timestamp = null;
				}

				if ((lobjFrom != null) && (lstrEmail != null))
					larrResults[i].isFromMe = lstrEmail.equalsIgnoreCase(lobjFrom[0].toString());
				else
					larrResults[i].isFromMe = false;

				List<BodyPart> attachmentsMap = MailConnector.getAttachments(parrSource[i]);
				larrResults[i].attachmentCount = attachmentsMap==null ? 0 : attachmentsMap.size();
				lobjBody = parrSource[i].getContent().toString();
				if ( lobjBody == null )
					larrResults[i].bodyPreview = "_";
				else
				{
					lstrBody = lobjBody.toString();
					if ( lstrBody.length() > 200 )
						larrResults[i].bodyPreview = lstrBody.substring(0, 200);
					else
						larrResults[i].bodyPreview = lstrBody;
				}
			} catch (Throwable e) {
				throw new BigBangException(e.getMessage(), e);
			}
		}

		return larrResults;
	}
	
	private static ExchangeItemStub[] sToClient(javax.mail.Folder folder)
			throws BigBangException { 
		
		ExchangeItemStub[] larrResults = null;
		
		if (folder == null) {
			return null;
		}
		
		try {
			larrResults = new ExchangeItemStub[1];
			
			// TODO ver folder.getURLName() e folder.getFullName() e mudar nos outros sitios onde usas a 
			// folder (o fetch) para ver se esta a ir buscar bem por noome
			larrResults[0].id = folder.getName(); 
			larrResults[0].isFolder = true;
			larrResults[0].isFromMe = false;
			larrResults[0].subject = folder.getName();
			larrResults[0].from = null;
			larrResults[0].timestamp = null;
			larrResults[0].attachmentCount = -1;
			larrResults[0].bodyPreview = null;
		} catch (Throwable e) {
			throw new BigBangException(e.getMessage(), e);
		}
		
		return larrResults;
	}

	public ExchangeItemStub[] getItems(boolean sent)
		throws SessionExpiredException, BigBangException
	{
		Message[] larrItems;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			//larrItems = MailConnector.DoGetMail(sent);
			larrItems = MailConnector.getMails(null, false);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sToClient(larrItems);
	}

	public ExchangeItemStub[] getItemsAll(boolean sent)
		throws SessionExpiredException, BigBangException
	{
		Message[] larrItems;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			larrItems = MailConnector.getSentOrReceived(sent);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sToClient(larrItems);
	}

	public ExchangeItemStub[] getFolder(String id)
		throws SessionExpiredException, BigBangException
	{
		javax.mail.Folder larrItems;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			larrItems = MailConnector.getFolder(id);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return sToClient(larrItems);
	}

	public ExchangeItem getItem(String id)
		throws SessionExpiredException, BigBangException
	{
		MimeMessage lobjItem;
		ExchangeItem lobjResult;
		Address[] lobjFrom;
		ArrayList<AttachmentStub> larrStubs;
		AttachmentStub lobjAttStub;
		String lstrBody;
		Map<String, BodyPart> attachmentsMap = null;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjItem = (MimeMessage) MailConnector.getMessage(id, null);

			lobjResult = new ExchangeItem();
			lobjResult.id = lobjItem.getMessageID();
			lobjResult.isFolder = false;
			lobjResult.subject = lobjItem.getSubject();
			lobjFrom = lobjItem.getFrom();
			lobjResult.from = lobjFrom != null ? lobjFrom[0].toString() : null;
			lobjResult.isFromMe = (lobjResult.from != null && lobjFrom[0].toString().equals(MailConnector.getUserEmail()));
			
			lobjResult.timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(lobjItem.getSentDate());
			
			attachmentsMap = MailConnector.getAttachmentsMap(lobjItem);
			
			lobjResult.attachmentCount = attachmentsMap==null ? 0 : attachmentsMap.size();
			Object content = lobjItem.getContent();
			lobjResult.body = content.toString();
			larrStubs = new ArrayList<AttachmentStub>();
			
			try
			{
				if (content instanceof Multipart && attachmentsMap != null) {
					lstrBody = attachmentsMap.get("main").getContent().toString();
					lstrBody = prepareBodyInline(lstrBody, attachmentsMap);
					lobjResult.body = lstrBody;
				} else {
					lstrBody = content.toString();
					if ( lstrBody.length() > 200 )
						lobjResult.bodyPreview = lstrBody.substring(0, 200);
					else
						lobjResult.bodyPreview = lstrBody;
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
					lobjAttStub.mimeType = entry.getValue().getContentType();
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

	/** 
	 * This method replaces the images in the body of an email with the equivalent
	 * Base-64 converted string. Removes those images and the main text from the
	 * mail attachments
	 */
	private String prepareBodyInline(String lstrBody, Map<String, BodyPart> attachmentsMap) throws BigBangException {
		
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
					throw new BigBangException(e.getMessage(), e);
				}
			}
        }
		
		return lstrBody;
	}

	public Document getAttAsDoc(String emailId, String attachmentId)
		throws SessionExpiredException, BigBangException
	{
		Document lobjResult;
		FileXfer lobjFile;
		UUID lidKey;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjFile = MailConnector.getAttachment(emailId, attachmentId);
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
