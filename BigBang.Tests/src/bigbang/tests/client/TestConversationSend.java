package bigbang.tests.client;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestConversationSend
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Message message;

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

		message = new Message();
//		outgoing = new ExternalInfoRequest.Outgoing();
//		outgoing.requestId = "0D867A6D-ADAE-41F8-8826-A00001005C32";
//		outgoing.message.subject = "Resposta";
//		outgoing.message.text = "Então é assim...";
//		outgoing.isFinal = true;
//		outgoing.replylimit = 7;

		Services.conversationService.sendMessage(message, null, callback);
	}
}
