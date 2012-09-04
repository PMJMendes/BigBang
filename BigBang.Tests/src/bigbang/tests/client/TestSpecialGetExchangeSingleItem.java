package bigbang.tests.client;

import bigBang.library.shared.ExchangeItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSpecialGetExchangeSingleItem
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<ExchangeItem> callback = new AsyncCallback<ExchangeItem>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(ExchangeItem result)
			{
				return;
			}
		};

		Services.exchangeService.getItem("AAMkADdmNDVhMDgyLWQzNDItNDRjNy04Mjk5LWY2YjgwZjA0YTAwOABGAAAAAADy5wo1GEKBQ7mf4MGQSeS5BwCo4XVv2UW6RZN0o9Yy+qctAClNyBCCAADDZpLTOBZZQoBQCyW3yamgAClNHAwDAAA=",
				callback);
	}
}
