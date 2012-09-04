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

		Services.reportService.RunVerb("S:2FF04A44-2D5A-4686-B8B6-A0320126ABA5:9A119C30-65FF-4B1C-AFC3-A09201102470:53536E26-EA2D-423C-B9C1-A092011024BF",
				callback);
	}
}
