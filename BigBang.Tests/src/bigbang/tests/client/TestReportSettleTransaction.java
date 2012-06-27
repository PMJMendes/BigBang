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

		Services.reportService.RunVerb("S:2FF04A44-2D5A-4686-B8B6-A0320126ABA5:ABFA6EA4-7D1F-4F9F-858C-A07D0102CEFD:59C4D36F-2EDE-4CB4-B8D0-A07D0102CF28",
				callback);
	}
}
