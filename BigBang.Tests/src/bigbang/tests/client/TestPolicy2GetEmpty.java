package bigbang.tests.client;

import bigBang.definitions.shared.Policy2;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestPolicy2GetEmpty
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Policy2> callback = new AsyncCallback<Policy2>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Policy2 result)
			{
				return;
			}
		};

		Services.policy2Service.getEmptyPolicy("BEBB58B5-CD95-4872-B72F-9EE90118938F", callback);
	}
}
