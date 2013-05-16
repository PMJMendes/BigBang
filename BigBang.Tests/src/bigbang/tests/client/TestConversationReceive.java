package bigbang.tests.client;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestConversationReceive
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
		message.conversationId = "EC20D291-71B2-4B9D-B24B-A11E01274A39";
		message.kind = Message.Kind.EMAIL;
		message.emailId = "AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwAfB8bLbZT3Q6D4HtPpCBWKAAAAAKnCAACtSKTbKCtrR6MYTzZmb/1MAAADRX77AAA=";

		Services.conversationService.receiveMessage(message, null, callback);
	}
}
