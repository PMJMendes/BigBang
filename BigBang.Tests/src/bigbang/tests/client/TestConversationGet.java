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

		Services.conversationService.getConversation("AD5EEEE7-E646-4F9D-9E41-9FE80137FC19", callback);
	}
}
