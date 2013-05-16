package bigbang.tests.client;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestConversationRepeat
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
//		AsyncCallback<InfoOrDocumentRequest> callback = new AsyncCallback<InfoOrDocumentRequest>()
//		{
//			public void onFailure(Throwable caught)
//			{
//				return;
//			}
//
//			public void onSuccess(InfoOrDocumentRequest result)
//			{
//				DoStep2(result);
//			}
//		};
//
//		Services.infoOrDocumentRequestService.getRequest("AD5EEEE7-E646-4F9D-9E41-9FE80137FC19", callback);
//	}
//
//	private static void DoStep2(InfoOrDocumentRequest request)
//	{
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
//		request.replylimit = 21;
//		request.message.subject = "Segundo Pedido de Carta de Condução";
//		request.message.text = "Já disse para nos enviar uma cópia digital da sua carta de condução!";
		Services.conversationService.repeatMessage(message, 7, callback);
	}
}
