package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestConversationClose
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Void result)
			{
				return;
			}
		};

		Services.conversationService.closeConversation("0D867A6D-ADAE-41F8-8826-A00001005C32", "B464EFA6-A770-49C6-B038-9FE900C93F9A",
				callback);
	}
}
