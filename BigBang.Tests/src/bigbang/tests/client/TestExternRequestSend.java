package bigbang.tests.client;

import bigBang.definitions.shared.ExternalInfoRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExternRequestSend
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		ExternalInfoRequest.Outgoing outgoing;

		AsyncCallback<ExternalInfoRequest> callback = new AsyncCallback<ExternalInfoRequest>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(ExternalInfoRequest result)
			{
				return;
			}
		};

		outgoing = new ExternalInfoRequest.Outgoing();
		outgoing.requestId = "0D867A6D-ADAE-41F8-8826-A00001005C32";
		outgoing.message.subject = "Resposta";
		outgoing.message.text = "Então é assim...";
		outgoing.isFinal = true;
		outgoing.replylimit = 7;

		Services.externRequestService.sendInformation(outgoing, callback);
	}
}
