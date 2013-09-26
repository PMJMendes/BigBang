package bigbang.tests.client;

import bigBang.definitions.shared.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestConversationGetForForward
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

		Services.conversationService.getForForward("9D83DCEC-F209-4212-BFCA-A200010BEF10", callback);
	}
}
