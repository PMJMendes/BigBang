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

		Services.receiptService.massCreatePaymentNotice(new String[] {"14E4DEF9-E9F7-41CE-83AB-A11700C1EF88",
				"A40D598A-46A2-47F4-B1E2-A11E00FACD6E", "0892BFDE-2A09-4501-8D6D-A12300D866FF", "50A0FCCD-E559-47F7-A783-A12500D1208B",
				"1CDE2D6D-BB0E-46FC-A8AB-A13301565A2E"}, callback);
	}
}
