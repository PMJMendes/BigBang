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

		Services.reportService.RunVerb("P:D9B82698-59AC-496A-9A63-A0B30122642B", callback);
	}
}
