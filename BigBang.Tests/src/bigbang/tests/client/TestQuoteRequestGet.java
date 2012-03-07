package bigbang.tests.client;

import bigBang.definitions.shared.QuoteRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestQuoteRequestGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<QuoteRequest> callback = new AsyncCallback<QuoteRequest> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(QuoteRequest result)
			{
				return;
			}
		};

		Services.quoteRequestService.getRequest("F8F245CE-5072-40E8-BBF9-A00D01040F4F", callback);
	}
}
