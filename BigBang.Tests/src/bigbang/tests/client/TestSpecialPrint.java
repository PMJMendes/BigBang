package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSpecialPrint
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

		Services.reportService.RunVerb("P:24D03787-D22E-4861-B2EA-A046011B3CE2", callback );
	}
}
