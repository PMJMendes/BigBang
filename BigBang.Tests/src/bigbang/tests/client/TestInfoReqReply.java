package bigbang.tests.client;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest.Response;
import bigBang.library.shared.Attachment;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestInfoReqReply
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Attachment> callback = new AsyncCallback<Attachment>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Attachment result)
			{
				DoStep2(result);
			}
		};

		Services.exchangeService.getAttachment("AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwCtSKTbKCtrR6MYTzZmb/1MAAADRQAbAAAjNXAn+Af6QrQHRLqsAzc5ABMoVdUQAAA=",
				"AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwCtSKTbKCtrR6MYTzZmb/1MAAADRQAbAAAjNXAn+Af6QrQHRLqsAzc5ABMoVdUQAAABEgAQAABKS+HqJCtLjEYxuUTRIZg=",
				callback);
	}

	private static void DoStep2(Attachment attachment)
	{
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
		response.requestId = "AD5EEEE7-E646-4F9D-9E41-9FE80137FC19";
		response.emailId = "AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwCtSKTbKCtrR6MYTzZmb/1MAAADRQAbAAAjNXAn+Af6QrQHRLqsAzc5ABMoVdUQAAA=";
		response.upgrades = new Response.Upgrade[] {new Response.Upgrade()};
		response.upgrades[0].name = "Teste";
		response.upgrades[0].docTypeId = "5ABB972E-9E7E-4733-9C1E-9F1300B4EB3A";
		response.upgrades[0].storageId = attachment.storageId;
		Services.infoOrDocumentRequestService.receiveResponse(response, callback);
	}
}
