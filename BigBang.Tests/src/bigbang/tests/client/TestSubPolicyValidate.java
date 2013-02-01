package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubPolicyValidate
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

		Services.subPolicyService.validateSubPolicy("2238CC33-CBBB-4FC1-A7E8-9FFA011E42DD", callback);
	}
}
