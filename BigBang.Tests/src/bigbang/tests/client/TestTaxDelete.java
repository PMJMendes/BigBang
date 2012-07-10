package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestTaxDelete
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

		Services.coveragesService.deleteTax("", callback);
	}
}
