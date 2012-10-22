package bigbang.tests.client;

import bigBang.definitions.shared.IncomingMessage;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest.Response;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestInfoReqReply
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
//		AsyncCallback<Attachment> callback = new AsyncCallback<Attachment>()
//		{
//			public void onFailure(Throwable caught)
//			{
//				return;
//			}
//
//			public void onSuccess(Attachment result)
//			{
//				DoStep2(result);
//			}
//		};
//
//		Services.exchangeService.getAttachment("AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwCtSKTbKCtrR6MYTzZmb/1MAAADRQAbAACtSKTbKCtrR6MYTzZmb/1MAAADRWwGAAA=",
//				"AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwCtSKTbKCtrR6MYTzZmb/1MAAADRQAbAACtSKTbKCtrR6MYTzZmb/1MAAADRWwGAAABEgAQAABKS+HqJCtLjEYxuUTRIZg=",
//				callback);
//	}
//
//	private static void DoStep2(Attachment attachment)
//	{
		Response response;

		AsyncCallback<InfoOrDocumentRequest> callback = new AsyncCallback<InfoOrDocumentRequest>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InfoOrDocumentRequest result)
			{
				return;
			}
		};

		response = new Response();
		response.requestId = "B875C2A6-ADD6-47C4-A7CD-A08900CDE072";
		response.message.emailId = "AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwCtSKTbKCtrR6MYTzZmb/1MAAADRQAbAACtSKTbKCtrR6MYTzZmb/1MAAADRWwGAAA=";
		response.message.upgrades = new IncomingMessage.AttachmentUpgrade[] {new IncomingMessage.AttachmentUpgrade()};
		response.message.upgrades[0].name = "Teste";
		response.message.upgrades[0].docTypeId = "5ABB972E-9E7E-4733-9C1E-9F1300B4EB3A";
//		response.message.upgrades[0].storageId = attachment.storageId;
		Services.infoOrDocumentRequestService.receiveResponse(response, callback);
	}
}
