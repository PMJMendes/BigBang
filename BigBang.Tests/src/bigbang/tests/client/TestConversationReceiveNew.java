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
		conversation.parentDataObjectId = "EC052267-C010-45CA-9298-A15400CBDAD7";
		conversation.requestTypeId = "5D656EC2-E1CD-4577-B9F9-A13400D7C8C4";
		conversation.replylimit = null;

		conversation.messages = new Message[] {new Message()};
		conversation.messages[0].kind = Message.Kind.EMAIL;
		conversation.messages[0].emailId = "AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwAfB8bLbZT3Q6D4HtPpCBWKAAAAAKnCAACY2siwYA8YR7NHNtCOoGdcAAAQVuGRAAA=";
		conversation.messages[0].attachments = new Message.Attachment[] {new Message.Attachment()/*,
				new Message.Attachment(), new Message.Attachment(), new Message.Attachment()*/};

		conversation.messages[0].attachments[0].name = "Teste";
		conversation.messages[0].attachments[0].docTypeId = "50987B3E-166D-4C22-9F91-A0E500F23F46";
		conversation.messages[0].attachments[0].attachmentId = "AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwAfB8bLbZT3Q6D4HtPpCBWKAAAAAKnCAACY2siwYA8YR7NHNtCOoGdcAAAQVuGRAAABBgAEAAEAAAA=";
//		conversation.messages[0].attachments[1].name = "Teste 1";
//		conversation.messages[0].attachments[1].docTypeId = "85783327-AB72-46A4-B250-A1030113C043";
//		conversation.messages[0].attachments[1].attachmentId = "AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwAfB8bLbZT3Q6D4HtPpCBWKAAAAAKnCAACY2siwYA8YR7NHNtCOoGdcAAAAAS4sAAABBgAEAAEAAAA=";
//		conversation.messages[0].attachments[2].name = "Teste 2";
//		conversation.messages[0].attachments[2].docTypeId = "85783327-AB72-46A4-B250-A1030113C043";
//		conversation.messages[0].attachments[2].attachmentId = "AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwAfB8bLbZT3Q6D4HtPpCBWKAAAAAKnCAACY2siwYA8YR7NHNtCOoGdcAAAAAS4sAAABBgAEAAIAAAA=";
//		conversation.messages[0].attachments[3].name = "Teste 3";
//		conversation.messages[0].attachments[3].docTypeId = "85783327-AB72-46A4-B250-A1030113C043";
//		conversation.messages[0].attachments[3].attachmentId = "AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwAfB8bLbZT3Q6D4HtPpCBWKAAAAAKnCAACY2siwYA8YR7NHNtCOoGdcAAAAAS4sAAABBgAEAAMAAAA=";

		conversation.messages[0].addresses = null;

		Services.clientService.receiveMessage(conversation, callback);
	}
}
