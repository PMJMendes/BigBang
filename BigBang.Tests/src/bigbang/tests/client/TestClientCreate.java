package bigbang.tests.client;

import bigBang.definitions.shared.Client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestClientCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Client client;

		AsyncCallback<Client> callback = new AsyncCallback<Client>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Client result)
			{
				return;
			}
		};

		client = new Client();
		client.name = "Gumbercindo";
		client.typeId = "4098CF7A-B5EE-4C3F-973F-9EE600C961AA";
		client.subtypeId = "5C7A0424-126B-467B-977A-9EE600CC13A4";
		client.mediatorId = "B8FDD1D1-45EF-4587-B747-A0A301243632";
		client.operationalProfileId = "9F871430-9BBC-449F-B125-9EE600BE5A9A";

		Services.clientService.createClient(client, callback);
	}
}
