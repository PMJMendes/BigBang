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

		Services.receiptService.massCreatePaymentNotice(new String[] {"AC53A495-C861-4B6D-BF87-A0D300F1BF0B"}, callback);
	}
}
