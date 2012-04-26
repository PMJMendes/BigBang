package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExpenseReturnToClient
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Void result)
			{
				return;
			}
		};

		Services.expenseService.massReturnToClient(new String[] {"0D273CB9-122A-4DF5-B786-A03C00BB8E20",
				"FE45D90D-A41E-494C-B902-A03C00C159A9"}, callback);
	}
}
