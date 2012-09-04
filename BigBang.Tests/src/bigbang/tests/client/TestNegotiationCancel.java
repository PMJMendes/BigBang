package bigbang.tests.client;

import bigBang.definitions.shared.Negotiation;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestNegotiationCancel
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Negotiation.Cancellation cancellation;

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

		cancellation = new Negotiation.Cancellation();
		cancellation.negotiationId = "153A3B54-2FA3-4008-B682-A00400DCBBF1";
		cancellation.internalMotiveId = "B464EFA6-A770-49C6-B038-9FE900C93F9A";
		cancellation.sendResponseToInsuranceAgency = true;
		cancellation.message.toContactInfoId = "C6764677-0885-4BF3-8EB8-9FDD00D78FB5";
		cancellation.message.forwardUserFullNames = null;
		cancellation.message.internalBCCs = null;
		cancellation.message.externalCCs = null;
		cancellation.message.subject = "Renovação da Apólice";
		cancellation.message.text = "Obrigaduncho, mas já não queremos...";

		Services.negotiationService.cancelNegotiation(cancellation, callback);
	}
}
