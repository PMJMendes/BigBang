package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReportVerb
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

		Services.reportService.RunVerb("T:B39C1B5A-BD1D-4C70-B998-A09900FBA339:4A896B61-9E91-41EA-B389-A0D900CD80AF", callback );
	}
}
