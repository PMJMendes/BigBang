package bigbang.tests.client;

import bigBang.definitions.shared.Negotiation;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestNegotiationReceiveQuote
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Negotiation.Response response;

		AsyncCallback<Negotiation> callback = new AsyncCallback<Negotiation>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Negotiation result)
			{
				return;
			}
		};

		response = new Negotiation.Response();
		response.negotiationId = "D5697A30-26E4-4913-9EDE-9FFE011B6F30";
		response.message.notes = "Fazemos por 1500. Somos muita bons...";
		response.message.emailId = null;
		response.message.upgrades = null;

		Services.negotiationService.receiveResponse(response, callback);
	}
}
