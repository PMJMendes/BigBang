package bigbang.tests.client;

import bigBang.definitions.shared.InsurerAccountingExtra;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptInsurerAccounting
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

		Services.receiptService.massInsurerAccounting(new String[] {"E62C9524-408F-4A78-916F-A0E90057A04D"}, new InsurerAccountingExtra[0], callback);
	}
}
