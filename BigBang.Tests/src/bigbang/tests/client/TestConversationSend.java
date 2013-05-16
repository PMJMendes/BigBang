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
		message.conversationId = "35D525C7-4518-42D4-A5B0-A11E01258DBB";
		message.kind = Message.Kind.EMAIL;
		message.subject = "Resposta";
		message.text = "Então é assim...";
		message.attachments = new Message.Attachment[] {new Message.Attachment(), new Message.Attachment()};
		message.attachments[0].docId = "A6B657B0-9513-403B-859C-A10B0102B580";
		message.attachments[1].docId = "ECB3070B-0494-4A50-BA70-A10B010C8963";

		message.addresses = new Message.MsgAddress[2];
		message.addresses[0] = new Message.MsgAddress();
		message.addresses[0].usage = Message.MsgAddress.Usage.TO;
		message.addresses[0].contactInfoId = "6AEFC7DC-FF60-417D-BEE4-A10B01028128";
		message.addresses[1] = new Message.MsgAddress();
		message.addresses[1].usage = Message.MsgAddress.Usage.REPLYTO;
		message.addresses[1].userId = "CCFE40AE-126F-48A0-9E5C-A0FE00E13F90";

		Services.conversationService.sendMessage(message, null, callback);
	}
}
