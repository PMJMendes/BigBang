package bigbang.tests.client;

import bigBang.library.shared.BigBangProcess;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSpecialGetSubProcs
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<BigBangProcess[]> callback = new AsyncCallback<BigBangProcess[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(BigBangProcess[] result)
			{
				return;
			}
		};

		Services.processService.getSubProcesses("31B97CAC-CADD-4F9F-8D62-9FB700201362", callback);
	}
}
