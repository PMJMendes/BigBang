package bigBang.library.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import microsoft.exchange.webservices.data.Attachment;
import microsoft.exchange.webservices.data.EmailAddress;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.FileAttachment;
import microsoft.exchange.webservices.data.Item;
import microsoft.exchange.webservices.data.MessageBody;
import Jewel.Engine.Engine;
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

	public ExchangeItemStub[] getItems()
		throws SessionExpiredException, BigBangException
	{
		Item[] larrItems;
		ExchangeItemStub[] larrResults;
		int i;
		EmailAddress lobjFrom;
		MessageBody lobjBody;
		String lstrBody;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			larrItems = MailConnector.DoGetMail();

			if ( larrItems == null )
				return null;

			larrResults = new ExchangeItemStub[larrItems.length];
			for ( i = 0; i < larrResults.length; i++ )
			{
				try
				{
					larrItems[i].load();
				}
				catch (Throwable e)
				{
					larrResults[i] = new ExchangeItemStub();
				}
				larrResults[i] = new ExchangeItemStub();
				larrResults[i].id = larrItems[i].getId().getUniqueId();
				larrResults[i].subject = larrItems[i].getSubject();
				if ( larrItems[i] instanceof EmailMessage )
				{
					lobjFrom = ((EmailMessage)larrItems[i]).getFrom();
					larrResults[i].from = ( lobjFrom == null ? null :
							(lobjFrom.getName() == null ? lobjFrom.getAddress() : lobjFrom.getName()) );
				}
				else
					larrResults[i].from = null;
				try
				{
					larrResults[i].timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(larrItems[i].getDateTimeSent());
				}
				catch (Throwable e)
				{
					larrResults[i].timestamp = null;
				}
				larrResults[i].attachmentCount = larrItems[i].getAttachments().getCount();
				lobjBody = larrItems[i].getBody();
				if ( lobjBody == null )
					larrResults[i].bodyPreview = "";
				else
				{
					lstrBody = lobjBody.toString();
					if ( lstrBody.length() > 200 )
						larrResults[i].bodyPreview = lstrBody.substring(0, 200);
					else
						larrResults[i].bodyPreview = lstrBody;
				}
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrResults;
	}

	public ExchangeItemStub[] getItemsPaged(int page)
		throws SessionExpiredException, BigBangException
	{
		Item[] larrItems;
		ExchangeItemStub[] larrResults;
		int i;
		EmailAddress lobjFrom;
		MessageBody lobjBody;
		String lstrBody;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			larrItems = MailConnector.DoGetMail(page);

			if ( larrItems == null )
				return null;

			larrResults = new ExchangeItemStub[larrItems.length];
			for ( i = 0; i < larrResults.length; i++ )
			{
				try
				{
					larrItems[i].load();
				}
				catch (Throwable e)
				{
					larrResults[i] = new ExchangeItemStub();
				}
				larrResults[i] = new ExchangeItemStub();
				larrResults[i].id = larrItems[i].getId().getUniqueId();
				larrResults[i].subject = larrItems[i].getSubject();
				if ( larrItems[i] instanceof EmailMessage )
				{
					lobjFrom = ((EmailMessage)larrItems[i]).getFrom();
					larrResults[i].from = ( lobjFrom == null ? null :
							(lobjFrom.getName() == null ? lobjFrom.getAddress() : lobjFrom.getName()) );
				}
				else
					larrResults[i].from = null;
				try
				{
					larrResults[i].timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(larrItems[i].getDateTimeSent());
				}
				catch (Throwable e)
				{
					larrResults[i].timestamp = null;
				}
				larrResults[i].attachmentCount = larrItems[i].getAttachments().getCount();
				lobjBody = larrItems[i].getBody();
				if ( lobjBody == null )
					larrResults[i].bodyPreview = "";
				else
				{
					lstrBody = lobjBody.toString();
					if ( lstrBody.length() > 200 )
						larrResults[i].bodyPreview = lstrBody.substring(0, 200);
					else
						larrResults[i].bodyPreview = lstrBody;
				}
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrResults;
	}

	public ExchangeItem getItem(String id)
		throws SessionExpiredException, BigBangException
	{
		Item lobjItem;
		ExchangeItem lobjResult;
		EmailAddress lobjFrom;
		ArrayList<AttachmentStub> larrStubs;
		AttachmentStub lobjAttStub;
		String lstrBody;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjItem = MailConnector.DoGetItem(id);
			lobjItem.load();

			lobjResult = new ExchangeItem();
			lobjResult.id = lobjItem.getId().getUniqueId();
			lobjResult.subject = lobjItem.getSubject();
			if ( lobjItem instanceof EmailMessage )
			{
				lobjFrom = ((EmailMessage)lobjItem).getFrom(); 
				lobjResult.from = ( lobjFrom.getName() == null ? lobjFrom.getAddress() : lobjFrom.getName() );
			}
			else
				lobjResult.from = null;
			lobjResult.timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(lobjItem.getDateTimeSent());
			lobjResult.attachmentCount = lobjItem.getAttachments().getCount();
			lstrBody = lobjItem.getBody().toString();
			if ( lstrBody.length() > 200 )
				lobjResult.bodyPreview = lstrBody.substring(0, 200);
			else
				lobjResult.bodyPreview = lstrBody;

			lobjResult.body = lobjItem.getBody().toString();
			larrStubs = new ArrayList<AttachmentStub>();
			for ( Attachment lobjAtt: lobjItem.getAttachments() )
			{
				if ( !(lobjAtt instanceof FileAttachment) )
					continue;

				lobjAttStub = new AttachmentStub();
				((FileAttachment)lobjAtt).load();
				lobjAttStub.id = lobjAtt.getId();
				lobjAttStub.fileName = ((FileAttachment)lobjAtt).getName();
				lobjAttStub.mimeType = ( lobjAtt.getContentType() == null ? "application/octet-stream" : lobjAtt.getContentType() );
				lobjAttStub.size = ((FileAttachment)lobjAtt).getContent().length;
				larrStubs.add(lobjAttStub);
			}
			lobjResult.attachments = larrStubs.toArray(new AttachmentStub[larrStubs.size()]);
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return lobjResult;
	}

//	public bigBang.library.shared.Attachment getAttachment(String emailId, String attachmentId)
//		throws SessionExpiredException, BigBangException
//	{
//		FileXfer lobjFile;
//		bigBang.library.shared.Attachment lobjResult;
//		UUID lidKey;
//
//		if ( Engine.getCurrentUser() == null )
//			throw new SessionExpiredException();
//
//		try
//		{
//			lobjFile = MailConnector.DoGetAttachment(emailId, attachmentId);
//		}
//		catch (BigBangJewelException e)
//		{
//			throw new BigBangException(e.getMessage(), e);
//		}
//
//		lobjResult = new bigBang.library.shared.Attachment();
//		lobjResult.id = attachmentId;
//		lobjResult.fileName = lobjFile.getFileName();
//		lobjResult.mimeType = lobjFile.getContentType();
//		lobjResult.size = lobjFile.getLength();
//
//		lidKey = UUID.randomUUID();
//		FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);
//		lobjResult.storageId = lidKey.toString();
//
//		return lobjResult;
//	}
}
