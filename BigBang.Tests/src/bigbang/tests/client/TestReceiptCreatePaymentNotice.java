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

		Services.receiptService.massCreatePaymentNotice(new String[] {"E7638B7C-CC56-4531-A331-A02200F08310",
				"6BE6ECCC-3D83-444C-84C6-A0270144BF34", "11FB0F8D-7A45-4B26-9C57-A02E00F5AE32"}, callback);
	}
}
