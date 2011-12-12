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
//		TestCreateClient.DoTest();
//		TestEditClient.DoTest();
//		TestDeleteClient.DoTest();
//		TestGetPolicy.DoTest();
//		TestNewPolicy.DoTest();
//		TestEditPolicy.DoTest();
//		TestDeletePolicy.DoTest();
//		TestGetObject.DoTest();
//		TestCreateInsuredObject.DoTest();
//		TestEditInsuredObject.DoTest();
//		TestDeleteInsuredObject.DoTest();
//		TestCreateExercise.DoTest();
//		TestEditExercise.DoTest();
//		TestDeleteExercise.DoTest();
//		TestCreateReceipt.DoTest();
//		TestUpdateReceipt.DoTest();
//		TestDeleteReceipt.DoTest();

		TestGetAsImage.DoTest();
	}
}
