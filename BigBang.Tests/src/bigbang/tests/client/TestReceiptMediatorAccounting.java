package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptMediatorAccounting
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

		Services.receiptService.massMediatorAccounting(new String[] {"F980F3EE-C954-4214-9CA0-A0B600B23188"}, callback);
	}
}
