package bigbang.tests.client;

import bigBang.definitions.shared.QuoteRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestQuoteRequestClose
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<QuoteRequest> callback = new AsyncCallback<QuoteRequest>()
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

		Services.quoteRequestService.closeProcess("CDA354D6-0905-48F4-A220-A00E010A0EA5", "Eu não fiz nada...!", callback);
	}
}
