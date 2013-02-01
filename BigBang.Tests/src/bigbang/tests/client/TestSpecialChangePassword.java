package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSpecialChangePassword
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<String> callback = new AsyncCallback<String>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(String result)
			{
				return;
			}
		};

		Services.authenticationService.changePassword("12345", "Premium.", callback);
	}
}
