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
		request.parentDataObjectId = "CA38CF67-FB1C-4213-9ABE-A03C011459A1";
		request.requestTypeId = "05D3096C-FC09-47F4-B6FD-9FE801358AD2";
		request.replylimit = 15;
		request.message.toContactInfoId = "C6764677-0885-4BF3-8EB8-9FDD00D78FB5";
		request.message.forwardUserFullNames = new String[] {"Administrator"};
		request.message.externalCCs = "joao.mendes@archon-se.com";
		request.message.subject = "Pedido de Carta de Condução";
		request.message.text = "Por favor, envie-nos uma cópia digital da sua carta de condução.";

		Services.casualtyService.createInfoOrDocumentRequest(request, callback);
	}
}
