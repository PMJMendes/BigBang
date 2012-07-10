package bigbang.tests.client;

import bigBang.definitions.shared.Tax;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestTaxCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Tax tax;

		AsyncCallback<Tax> callback = new AsyncCallback<Tax>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Tax result)
			{
				return;
			}
		};

		tax = new Tax();

		Services.coveragesService.createTax(tax, callback);
	}
}
