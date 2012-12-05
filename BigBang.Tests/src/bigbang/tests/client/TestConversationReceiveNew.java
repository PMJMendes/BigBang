package bigbang.tests.client;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestConversationReceiveNew
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Conversation conversation;

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

		conversation = new Conversation();
		conversation.parentDataObjectId = "145F0BA8-0893-40B5-A71F-A0FE00E1B27C";
		conversation.requestTypeId = "03E56D41-5924-44F5-A8DE-A10B01044482";
		conversation.replylimit = 15;

		conversation.messages = new Message[] {new Message()};
		conversation.messages[0].kind = Message.Kind.NOTE;
		conversation.messages[0].subject = "Questão sobre a Tua";
		conversation.messages[0].text = "Qual é, meu?";
		conversation.messages[0].outgoingAttachmentIds = new String[]
				{"A6B657B0-9513-403B-859C-A10B0102B580", "ECB3070B-0494-4A50-BA70-A10B010C8963"};

		conversation.messages[0].addresses = null;

		Services.clientService.receiveMessage(conversation, callback);
	}
}
