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

		Services.expenseService.deleteExpense("30538EB5-5A53-4201-8706-A039010FB953", "Porque sim", callback);
	}
}
