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

		Services.receiptService.massCreatePaymentNotice(new String[] {"980104AC-B184-41CD-8312-A0BD00EE670B",
				"B4B9E006-3BC9-4677-B410-A0BD00EE9686"}, callback);
	}
}
