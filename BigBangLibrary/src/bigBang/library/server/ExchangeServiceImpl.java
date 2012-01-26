package bigBang.library.server;

import microsoft.exchange.webservices.data.Item;
import Jewel.Engine.Engine;
import bigBang.library.interfaces.ExchangeService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.ExchangeItem;
import bigBang.library.shared.SessionExpiredException;

import com.premiumminds.BigBang.Jewel.SysObjects.MailConnector;

public class ExchangeServiceImpl
	extends EngineImplementor
	implements ExchangeService
{
	private static final long serialVersionUID = 1L;

	public ExchangeItem[] getItems()
		throws SessionExpiredException, BigBangException
	{
		Item[] larrItems;
		ExchangeItem[] larrResults;
		int i;

		if ( Engine.getCurrentUser() == null )
			throw new SessionExpiredException();

		try
		{
			larrItems = MailConnector.DoGetMail();

			if ( larrItems == null )
				return null;

			larrResults = new ExchangeItem[larrItems.length];
			for ( i = 0; i < larrResults.length; i++ )
			{
				larrResults[i] = new ExchangeItem();
				larrResults[i].id = larrItems[i].getId().getUniqueId();
				larrResults[i].subject = larrItems[i].getSubject();
			}
		}
		catch (Throwable e)
		{
			throw new BigBangException(e.getMessage(), e);
		}

		return larrResults;
	}
}
