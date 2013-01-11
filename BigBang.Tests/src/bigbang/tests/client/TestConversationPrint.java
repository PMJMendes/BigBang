package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestConversationPrint
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<String> callback = new AsyncCallback<String>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(String result)
			{
				return;
			}
		};

		Services.conversationService.getForPrinting("E3E971E7-FD02-4154-9A2E-A13500B3EA64", callback);
	}
}
