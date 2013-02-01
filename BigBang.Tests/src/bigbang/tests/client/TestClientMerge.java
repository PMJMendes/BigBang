package bigbang.tests.client;

import bigBang.definitions.shared.Client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestClientMerge
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
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

		Services.clientService.mergeWithClient("B7E14F4D-04AB-433D-B09C-A0A30124391B", "E4C865D8-BE89-4AE5-8F1C-A0A301243903", callback);
	}
}
