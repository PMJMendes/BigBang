package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestPolicyValidate
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

		Services.insurancePolicyService.validatePolicy("588E0BE9-6E92-4711-B0CD-9FD50118C191", callback);
	}
}
