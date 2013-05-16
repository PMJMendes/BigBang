package bigbang.tests.client;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestConversationSendNew
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
		conversation.messages[0].kind = Message.Kind.EMAIL;
		conversation.messages[0].subject = "Pedido de Carta de Condução";
		conversation.messages[0].text = "Por favor, envie-nos uma cópia digital da sua carta de condução.";
		conversation.messages[0].attachments = new Message.Attachment[] {new Message.Attachment(), new Message.Attachment()};
		conversation.messages[0].attachments[0].docId = "A6B657B0-9513-403B-859C-A10B0102B580";
		conversation.messages[0].attachments[1].docId = "ECB3070B-0494-4A50-BA70-A10B010C8963";

		conversation.messages[0].addresses = new Message.MsgAddress[2];
		conversation.messages[0].addresses[0] = new Message.MsgAddress();
		conversation.messages[0].addresses[0].usage = Message.MsgAddress.Usage.TO;
		conversation.messages[0].addresses[0].contactInfoId = "6AEFC7DC-FF60-417D-BEE4-A10B01028128";
		conversation.messages[0].addresses[1] = new Message.MsgAddress();
		conversation.messages[0].addresses[1].usage = Message.MsgAddress.Usage.REPLYTO;
		conversation.messages[0].addresses[1].userId = "CCFE40AE-126F-48A0-9E5C-A0FE00E13F90";

		Services.clientService.sendMessage(conversation, callback);
	}
}
