package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptReturnToInsurer
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

		Services.receiptService.massReturnToInsurer(new String[] {"2753DCB8-6D23-4B02-8699-A01600F5F16F",
				"60236506-9DDD-4C09-AF94-A01A0139F6A8", "BEF62B86-43C7-48B6-BC3D-A01B00ED8460", "F4D7AF61-AF5E-4DB8-A436-A0220114BAC4"},
				callback);
	}
}
