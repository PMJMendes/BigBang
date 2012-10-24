package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReportSettleTransaction
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

		Services.reportService.RunVerb("S:2FF04A44-2D5A-4686-B8B6-A0320126ABA5:4FC68D17-B5E8-47B0-9A9B-A0F4010897FA:4DD7657F-3C5D-4148-AB36-A0F401089E55",
				callback);
	}
}
