package bigBang.library.server;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

import microsoft.exchange.webservices.data.Attachment;
import microsoft.exchange.webservices.data.EmailAddress;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.FileAttachment;
import microsoft.exchange.webservices.data.Item;
import Jewel.Engine.Engine;
import Jewel.Engine.SysObjects.FileXfer;
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
				larrResults[i] = new ExchangeItemStub();
				larrResults[i].id = larrItems[i].getId().getUniqueId();
				larrResults[i].subject = larrItems[i].getSubject();
				if ( larrItems[i] instanceof EmailMessage )
				{
					lobjFrom = ((EmailMessage)larrItems[i]).getFrom(); 
					larrResults[i].from = ( lobjFrom.getName() == null ? lobjFrom.getAddress() : lobjFrom.getName() );
				}
				else
					larrResults[i].from = null;
				larrResults[i].timestamp = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(larrItems[i].getDateTimeSent());
				larrResults[i].attachmentCount = larrItems[i].getAttachments().getCount();
				larrItems[i].load();
				lstrBody = larrItems[i].getBody().toString();
				if ( lstrBody.length() > 200 )
					larrResults[i].bodyPreview = lstrBody.substring(0, 200);
				else
					larrResults[i].bodyPreview = lstrBody;
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
			lobjItem.load();
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

	public bigBang.library.shared.Attachment getAttachment(String emailId, String attachmentId)
		throws SessionExpiredException, BigBangException
	{
		Item lobjItem;
		bigBang.library.shared.Attachment lobjResult;
		byte[] larrBytes;
		FileXfer lobjFile;
		UUID lidKey;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			lobjItem = MailConnector.DoGetItem(emailId);

			for ( Attachment lobjAtt: lobjItem.getAttachments() )
			{
				if ( !lobjAtt.getId().equals(attachmentId) )
					continue;

				lobjResult = new bigBang.library.shared.Attachment();
				((FileAttachment)lobjAtt).load();
				lobjResult.id = lobjAtt.getId();
				lobjResult.fileName = ((FileAttachment)lobjAtt).getName();
				lobjResult.mimeType = ( lobjAtt.getContentType() == null ? "application/octet-stream" : lobjAtt.getContentType() );
				lobjResult.size = ((FileAttachment)lobjAtt).getContent().length;

				larrBytes = ((FileAttachment)lobjAtt).getContent();
				lobjFile = new FileXfer(larrBytes.length, lobjResult.mimeType, lobjResult.fileName,
						new ByteArrayInputStream(larrBytes));
				lidKey = UUID.randomUUID();
				FileServiceImpl.GetFileXferStorage().put(lidKey, lobjFile);
				lobjResult.storageId = lidKey.toString();

				return lobjResult;
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		throw new BigBangException("Erro: Anexo não encontrado na mensagem indicada.");
	}
}
