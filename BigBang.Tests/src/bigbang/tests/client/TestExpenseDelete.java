package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExpenseDelete
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

		Services.expenseService.deleteExpense("5339582F-814A-4AC9-9510-A039013F313A", "Porque sim", callback);
	}
}
