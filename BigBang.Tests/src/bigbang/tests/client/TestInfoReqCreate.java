package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.InfoOrDocumentRequest;

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
		request.ownerId = "8F474C7D-DE85-4BEC-8139-9FB70020135F";
		request.documentType = "5ABB972E-9E7E-4733-9C1E-9F1300B4EB3A";
		request.text = "Por favor, envie-nos uma cópia digital da sua carta de condução.";
		request.replylimit = 15;
		request.toInfoId = "C6764677-0885-4BF3-8EB8-9FDD00D78FB5";
		request.forwardUserIds = new String[] {"091B8442-B7B0-40FA-B517-9EB00068A390"};
		request.externalCCs = "joao.mendes@archon-se.com";

		Services.clientService.createInfoOrDocumentRequest(request, callback);
	}
}
