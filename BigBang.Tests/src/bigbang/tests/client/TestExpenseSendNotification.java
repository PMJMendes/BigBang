package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExpenseSendNotification
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

		Services.expenseService.massSendNotification(new String[] {"9F99D844-C525-4605-9955-A03F00E5F4AE"}, callback);
	}
}
