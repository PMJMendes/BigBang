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

		Services.reportService.RunVerb("P:4E8020BB-CD95-4AD6-A5F1-A021013A6B02", callback );
	}
}
