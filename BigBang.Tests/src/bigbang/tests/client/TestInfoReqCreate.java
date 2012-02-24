package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.OutgoingHeaders;

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
		request.parentDataObjectId = "8F474C7D-DE85-4BEC-8139-9FB70020135F";
		request.requestTypeId = "05D3096C-FC09-47F4-B6FD-9FE801358AD2";
		request.subject = "Pedido de Carta de Condução";
		request.text = "Por favor, envie-nos uma cópia digital da sua carta de condução.";
		request.replylimit = 15;
		request.headers = new OutgoingHeaders();
		request.headers.toContactInfoId = "C6764677-0885-4BF3-8EB8-9FDD00D78FB5";
		request.headers.forwardUserIds = new String[] {"091B8442-B7B0-40FA-B517-9EB00068A390"};
		request.headers.externalCCs = "joao.mendes@archon-se.com";

		Services.clientService.createInfoOrDocumentRequest(request, callback);
	}
}
