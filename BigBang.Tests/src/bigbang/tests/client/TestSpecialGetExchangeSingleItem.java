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

		Services.exchangeService.getItem("AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwCtSKTbKCtrR6MYTzZmb/1MAAADRQAbAACtSKTbKCtrR6MYTzZmb/1MAAADRWwGAAA=",
				callback);
	}
}
