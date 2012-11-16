package bigbang.tests.client;

import bigBang.definitions.shared.InfoOrDocumentRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestInfoReqCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		InfoOrDocumentRequest request;

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

		request = new InfoOrDocumentRequest();
		request.parentDataObjectId = "145F0BA8-0893-40B5-A71F-A0FE00E1B27C";
		request.requestTypeId = "03E56D41-5924-44F5-A8DE-A10B01044482";
		request.replylimit = 15;
		request.message.toContactInfoId = "6AEFC7DC-FF60-417D-BEE4-A10B01028128";
//		request.message.forwardUserFullNames = new String[] {"Administrator"};
		request.message.externalCCs = "joao.mendes@marar.eu";
		request.message.subject = "Pedido de Carta de Condução";
		request.message.text = "Por favor, envie-nos uma cópia digital da sua carta de condução.";
		request.message.attachmentIds = new String[] {"A6B657B0-9513-403B-859C-A10B0102B580", "ECB3070B-0494-4A50-BA70-A10B010C8963"};

		Services.clientService.createInfoOrDocumentRequest(request, callback);
	}
}
