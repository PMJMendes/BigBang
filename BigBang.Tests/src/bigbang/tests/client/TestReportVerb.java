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

		Services.reportService.RunVerb("P:B66BC8F1-0EF5-407F-8883-A10E0107ECA5", callback );
	}
}
