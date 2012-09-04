package bigbang.tests.client;

import bigBang.library.shared.ExchangeItemStub;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSpecialGetExchangeItems
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<ExchangeItemStub[]> callback = new AsyncCallback<ExchangeItemStub[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(ExchangeItemStub[] result)
			{
				return;
			}
		};

		Services.exchangeService.getItems(callback);
	}
}
