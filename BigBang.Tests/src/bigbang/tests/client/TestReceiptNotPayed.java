package bigbang.tests.client;

import bigBang.definitions.shared.Receipt;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptNotPayed
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Receipt> callback = new AsyncCallback<Receipt>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Receipt result)
			{
				return;
			}
		};

		Services.receiptService.markNotPayed("066862C5-C2CE-4AEB-9C33-A02D01286505", callback);
	}
}
