package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptCreatePaymentNotice
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

		Services.receiptService.massCreatePaymentNotice(new String[] {"6E2B850B-877D-4D8A-9D72-A02101388CE1",
				"E33B3E31-38B0-4898-9773-A0210138BCEC", "B585A918-FD63-4E07-931B-A0210138DAAC"}, callback);
	}
}
