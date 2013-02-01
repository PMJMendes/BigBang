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

		Services.insurancePolicyService.validatePolicy("1F6B5AE6-0CD1-49F1-AD8A-A0A3017EE18E", callback);
	}
}
