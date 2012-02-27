package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.Negotiation.Grant;

public class TestNegotiationGrant
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Grant grant;

		AsyncCallback<Grant> callback = new AsyncCallback<Grant>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Grant result)
			{
				return;
			}
		};

		grant = new Negotiation.Grant();
		grant.negotiationId = "D5697A30-26E4-4913-9EDE-9FFE011B6F30";
		grant.message.toContactInfoId = "C6764677-0885-4BF3-8EB8-9FDD00D78FB5";
		grant.message.forwardUserFullNames = null;
		grant.message.internalBCCs = null;
		grant.message.externalCCs = null;
		grant.message.subject = "Adjudicação da Negociação";
		grant.message.text = "Ok, queremos...";
		grant.effectDate = "2012-03-01";

		Services.negotiationService.grantNegotiation(grant, callback);
	}
}
