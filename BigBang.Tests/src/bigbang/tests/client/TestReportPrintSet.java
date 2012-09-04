package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReportPrintSet
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

		Services.reportService.RunVerb("P:1205F697-6D40-4D9B-B70E-A0BD00F29E42", callback);
	}
}
