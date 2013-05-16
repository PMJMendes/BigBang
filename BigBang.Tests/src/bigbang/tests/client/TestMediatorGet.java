package bigbang.tests.client;

import bigBang.definitions.shared.Mediator;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestMediatorGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Mediator[]> callback = new AsyncCallback<Mediator[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Mediator[] result)
			{
				return;
			}
		};

		Services.mediatorService.getMediators(callback);
	}
}
