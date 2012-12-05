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
//		response = new ExternalInfoRequest.Incoming();
//		response.requestId = "0D867A6D-ADAE-41F8-8826-A00001005C32";
//		response.message.notes = null;
//		response.message.emailId = "AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwCtSKTbKCtrR6MYTzZmb/1MAAADRQAbAACtSKTbKCtrR6MYTzZmb/1MAAADRWwBAAA=";
//		response.replylimit = 14;

		Services.conversationService.receiveMessage(message, null, callback);
	}
}
