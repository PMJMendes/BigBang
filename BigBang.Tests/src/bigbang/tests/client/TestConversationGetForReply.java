package bigbang.tests.client;

import bigBang.definitions.shared.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestConversationGetForReply
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Message> callback = new AsyncCallback<Message>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Message result)
			{
				return;
			}
		};

		Services.conversationService.getForReply("FE1929A4-E6B4-47E0-882F-A1FC00FA01E4", callback);
	}
}
