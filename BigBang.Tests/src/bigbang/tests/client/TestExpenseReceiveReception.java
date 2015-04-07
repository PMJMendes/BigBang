package bigbang.tests.client;

import bigBang.definitions.shared.ScanHandle;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExpenseReceiveReception
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		ScanHandle handle;

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

		handle = new ScanHandle();
		handle.handle = "Document-501016";

		Services.expenseService.massReceiveReception(new String[] {"74E660B1-20A7-4097-8260-A10100A7463C",
				"E2C6C689-D1AE-4E59-BC31-A10100A7FF07", "036D8066-C68C-4A84-A2CC-A10100A82BDD"}, handle, callback);
	}
}
