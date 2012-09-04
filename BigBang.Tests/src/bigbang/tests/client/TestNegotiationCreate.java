package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Negotiation;

public class TestNegotiationCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Negotiation negotiation;

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

		negotiation = new Negotiation();
		negotiation.ownerId = "026CDFCF-17EB-41B6-ABEE-9FFA00FE0E40";
		negotiation.companyId = null;
		negotiation.notes = "Notas importantes.";

		Services.insurancePolicyService.createNegotiation(negotiation, callback);
	}
}
