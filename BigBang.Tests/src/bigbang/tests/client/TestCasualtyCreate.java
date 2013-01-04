package bigbang.tests.client;

import bigBang.definitions.shared.Casualty;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestCasualtyCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Casualty casualty;

		AsyncCallback<Casualty> callback = new AsyncCallback<Casualty>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Casualty result)
			{
				return;
			}
		};

		casualty = new Casualty();
		casualty.clientId = "72C55741-E996-477A-89C7-A0FE00E1B24D";
		casualty.casualtyDate = "2012-12-01";
		casualty.caseStudy = false;
		casualty.description = "Escorregou, partiu um pé, salta agora só com um pé.";
		casualty.internalNotes = "Nhurro...";

		Services.clientService.createCasualty(casualty, callback);
	}
}
