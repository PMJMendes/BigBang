package bigBang.library.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

import microsoft.exchange.webservices.data.Attachment;
import microsoft.exchange.webservices.data.BasePropertySet;
import microsoft.exchange.webservices.data.BodyType;
import microsoft.exchange.webservices.data.EmailAddress;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.FileAttachment;
import microsoft.exchange.webservices.data.Folder;
import microsoft.exchange.webservices.data.Item;
import microsoft.exchange.webservices.data.ItemAttachment;
import microsoft.exchange.webservices.data.MessageBody;
import microsoft.exchange.webservices.data.PropertySet;
import microsoft.exchange.webservices.data.ServiceObject;
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

	private static ExchangeItemStub[] sToClient(ServiceObject[] parrSource)
		throws BigBangException
	{
		String lstrEmail;
		ExchangeItemStub[] larrResults;
		int i;
		PropertySet lobjPropSet;
		EmailAddress lobjFrom;
		MessageBody lobjBody;
		String lstrBody;

		try
		{
			lstrEmail = MailConnector.getLoggedEmail();
		}
		catch (Throwable e)
		{
			lstrEmail = null;
		}

		larrResults = new ExchangeItemStub[parrSource.length];
		lobjPropSet = new PropertySet(BasePropertySet.FirstClassProperties);
		lobjPropSet.setRequestedBodyType(BodyType.Text);

		for ( i = 0; i < larrResults.length; i++ )
		{
			larrResults[i] = new ExchangeItemStub();
			try
			{
				if ( parrSource[i] instanceof Item )
				{
					larrResults[i].id = ((Item)parrSource[i]).getId().getUniqueId();
					larrResults[i].isFolder = false;
					larrResults[i].subject = ((Item)parrSource[i]).getSubject();
					if ( parrSource[i] instanceof EmailMessage )
					{
						lobjFrom = ((EmailMessage)parrSource[i]).getFrom();
						larrResults[i].from = ( lobjFrom == null ? null :
								(lobjFrom.getName() == null ? lobjFrom.getAddress() : lobjFrom.getName()) );
					}
					else
					{
						lobjFrom = null;
						larrResults[i].from = null;
					}
					try
					{
						larrResults[i].timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(((Item)parrSource[i]).getDateTimeSent());
					}
					catch (Throwable e)
					{
						larrResults[i].timestamp = null;
					}

					lobjPropSet = new PropertySet(BasePropertySet.FirstClassProperties);
					lobjPropSet.setRequestedBodyType(BodyType.Text);
					try
					{
						parrSource[i].load(lobjPropSet);
						if ((lobjFrom != null) && (lstrEmail != null))
							larrResults[i].isFromMe = lstrEmail.equalsIgnoreCase(lobjFrom.getAddress());
						else
							larrResults[i].isFromMe = false;
						larrResults[i].attachmentCount = ((Item)parrSource[i]).getAttachments().getCount();
						lobjBody = ((Item)parrSource[i]).getBody();
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
					}
					catch (Throwable e)
					{
						larrResults[i].bodyPreview = "(Erro interno do servidor de Exchange.)";
					}
				}

				if ( parrSource[i] instanceof Folder )
				{
					larrResults[i].id = ((Folder)parrSource[i]).getId().getUniqueId();
					larrResults[i].isFolder = true;
					larrResults[i].isFromMe = false;
					larrResults[i].subject = ((Folder)parrSource[i]).getDisplayName();
					larrResults[i].from = null;
					larrResults[i].timestamp = null;
					larrResults[i].attachmentCount = -1;
					larrResults[i].bodyPreview = null;
				}
			}
			catch (Throwable e)
			{
				throw new BigBangException(e.getMessage(), e);
			}
		}

		return larrResults;
	}

	public ExchangeItemStub[] getItems(boolean sent)
		throws SessionExpiredException, BigBangException
	{
		ServiceObject[] larrItems;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			larrItems = MailConnector.DoGetMail(sent);
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
		ServiceObject[] larrItems;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			larrItems = MailConnector.DoGetMailAll(sent);
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
		ServiceObject[] larrItems;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			larrItems = MailConnector.DoGetFolder(id);
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
		Item lobjItem;
		ExchangeItem lobjResult;
		EmailAddress lobjFrom;
		ArrayList<AttachmentStub> larrStubs;
		AttachmentStub lobjAttStub;
		PropertySet lobjPropSet;
		String lstrBody;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjItem = MailConnector.DoGetItem(id);
			lobjItem.load();

			lobjResult = new ExchangeItem();
			lobjResult.id = lobjItem.getId().getUniqueId();
			lobjResult.isFolder = false;
			lobjResult.subject = lobjItem.getSubject();
			if ( lobjItem instanceof EmailMessage )
			{
				lobjFrom = ((EmailMessage)lobjItem).getFrom(); 
				lobjResult.from = ( lobjFrom.getName() == null ? lobjFrom.getAddress() : lobjFrom.getName() );
				lobjResult.isFromMe = MailConnector.getLoggedEmail().equalsIgnoreCase(lobjFrom.getAddress());
			}
			else
			{
				lobjResult.from = null;
				lobjResult.isFromMe = false;
			}
			lobjResult.timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(lobjItem.getDateTimeSent());
			lobjResult.attachmentCount = lobjItem.getAttachments().getCount();
			lobjResult.body = lobjItem.getBody().toString();
			larrStubs = new ArrayList<AttachmentStub>();
			for ( Attachment lobjAtt: lobjItem.getAttachments() )
			{
				if ( lobjAtt instanceof FileAttachment )
				{
					lobjAttStub = new AttachmentStub();
					((FileAttachment)lobjAtt).load();
					lobjAttStub.id = lobjAtt.getId();
					lobjAttStub.fileName = ((FileAttachment)lobjAtt).getName();
					lobjAttStub.mimeType = ( lobjAtt.getContentType() == null ? "application/octet-stream" : lobjAtt.getContentType() );
					lobjAttStub.size = ((FileAttachment)lobjAtt).getContent().length;
					larrStubs.add(lobjAttStub);
				}
				else if ( lobjAtt instanceof ItemAttachment )
				{
					lobjAttStub = new AttachmentStub();
					((ItemAttachment)lobjAtt).load();
					lobjAttStub.id = lobjAtt.getId();
					lobjAttStub.fileName = ((ItemAttachment)lobjAtt).getName();
					lobjAttStub.mimeType = ( lobjAtt.getContentType() == null ? "application/octet-stream" : lobjAtt.getContentType() );
					lobjAttStub.size = 0;
					larrStubs.add(lobjAttStub);
				}
			}
			lobjResult.attachments = larrStubs.toArray(new AttachmentStub[larrStubs.size()]);

			try
			{
				lobjPropSet = new PropertySet(BasePropertySet.FirstClassProperties);
				lobjPropSet.setRequestedBodyType(BodyType.Text);
				lobjItem.load(lobjPropSet);
				lstrBody = lobjItem.getBody().toString();
				if ( lstrBody.length() > 200 )
					lobjResult.bodyPreview = lstrBody.substring(0, 200);
				else
					lobjResult.bodyPreview = lstrBody;
			}
			catch (Throwable e)
			{
				lobjResult.bodyPreview = "(Erro interno do servidor de Exchange.)";
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return lobjResult;
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
			lobjFile = MailConnector.DoGetAttachment(emailId, attachmentId);
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
