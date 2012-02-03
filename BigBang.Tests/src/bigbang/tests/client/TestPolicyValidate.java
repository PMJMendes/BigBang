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

		Services.insurancePolicyService.validatePolicy("988F0431-476B-4CE0-93A9-9FEC00D6006B", callback);
	}
}
