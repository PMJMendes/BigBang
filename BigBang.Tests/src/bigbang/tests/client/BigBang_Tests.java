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
//		TestSpecialChangePassword.DoTest();
//		TestSpecialGetAsImage.DoTest();
//		TestSpecialGetSubProcs.DoTest();
//		TestSpecialGetPermissions.DoTest();
//		TestSpecialGetExchangeItems.DoTest();
//
//		TestHistoryGet.DoTest();
//		TestHistoryUndo.DoTest();
//
//		TestMgrXferGet.DoTest();
//		TestMgrXferCreate.DoTest();
//		TestMgrXferAccept.DoTest();
//		TestMgrXferCancel.DoTest();
//		TestMgrXferMassCreate.DoTest();
//		TestMgrXferMassAccept.DoTest();
//		TestMgrXferMassCancel.DoTest();
//
		TestInfoReqCreate.DoTest();
//
//		TestContactGet.DoTest();
//		TestContactCreate.DoTest();
//		TestContactEdit.DoTest();
//		TestContactDelete.DoTest();
//
//		TestDocumentGet.DoTest();
//		TestDocumentCreate.DoTest();
//		TestDocumentEdit.DoTest();
//		TestDocumentDelete.DoTest();
//
//		TestTextGet.DoTest();
//		TestTextCreate.DoTest();
//		TestTextEdit.DoTest();
//		TestTextDelete.DoTest();
//
//		TestAgendaGet.DoTest();
//		TestAgendaDismiss.DoTest();
//
//		TestValuesGet.DoTest();
//
//		TestUserGet.DoTest();
//		TestUserCreate.DoTest();
//		TestUserEdit.DoTest();
//		TestUserDelete.DoTest();
//
//		TestClientGet.DoTest();
//		TestClientCreate.DoTest();
//		TestClientEdit.DoTest();
//		TestClientDelete.DoTest();
//
//		TestPolicyGet.DoTest();
//		TestPolicyCreate.DoTest();
//		TestPolicyEdit.DoTest();
//		TestPolicyDelete.DoTest();
//		TestPolicyValidate.DoTest();
//
//		TestObjectGet.DoTest();
//		TestObjectCreate.DoTest();
//		TestObjectEdit.DoTest();
//		TestObjectDelete.DoTest();
//
//		TestExerciseGet.DoTest();
//		TestExerciseCreate.DoTest();
//		TestExerciseEdit.DoTest();
//		TestExerciseDelete.DoTest();
//
//		TestReceiptGet.DoTest();
//		TestReceiptCreate.DoTest();
//		TestReceiptEdit.DoTest();
//		TestReceiptDelete.DoTest();
	}
}
