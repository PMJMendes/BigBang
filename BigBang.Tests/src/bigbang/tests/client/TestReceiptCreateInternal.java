package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptCreateInternal
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

		Services.receiptService.massCreateInternalReceipt(new String[] {"D687EE32-AF78-451C-9E0B-A11600EE6FB0"}, callback);
	}
}
