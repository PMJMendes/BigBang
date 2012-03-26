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

		Services.insurancePolicyService.validatePolicy("01B7B31C-0EFF-4A93-84EE-A02000F26B1F", callback);
	}
}
