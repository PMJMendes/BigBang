package bigbang.tests.client;

import bigBang.module.loginModule.shared.LoginResponse;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class BigBang_Tests
	implements EntryPoint
{
	public void onModuleLoad()
	{
		AsyncCallback<LoginResponse> callback = new AsyncCallback<LoginResponse>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(LoginResponse result)
			{
				DoTests();
			}
		};

		Services.authenticationService.login("CrediteEGS", callback);
//		Services.authenticationService.login("root", "Premium.", "CrediteEGS", callback);
	}

	private void DoTests()
	{
		TestGetPolicy.DoTest();
//		TestNewPolicy.DoTest();
//		TestEditPolicy.DoTest();
	}
}
