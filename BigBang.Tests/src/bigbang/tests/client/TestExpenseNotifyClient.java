package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExpenseNotifyClient
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

		Services.expenseService.massNotifyClient(new String[] {"27F3557D-C25D-4FC3-BFD3-A03C00B9811A",
				"BE29511A-4319-4296-A3FE-A03C00C1751C", "CB529531-736F-4E88-A3B4-A03C012A212E"}, callback);
	}
}
