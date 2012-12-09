package bigbang.tests.client;

import bigBang.definitions.shared.Conversation;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestConversationGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Conversation> callback = new AsyncCallback<Conversation>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Conversation result)
			{
				return;
			}
		};

		Services.conversationService.getConversation("35D525C7-4518-42D4-A5B0-A11E01258DBB", callback);
	}
}
