package bigbang.tests.client;

import bigBang.definitions.shared.Casualty;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestCasualtyClose
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Casualty> callback = new AsyncCallback<Casualty>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Casualty result)
			{
				return;
			}
		};

		Services.casualtyService.closeProcess("1FEC1DDE-B053-4C30-BB20-A02F00CDCB90", callback);
	}
}
