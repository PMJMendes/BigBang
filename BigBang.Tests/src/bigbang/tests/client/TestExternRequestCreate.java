package bigbang.tests.client;

import bigBang.definitions.shared.ExternalInfoRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExternRequestCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		ExternalInfoRequest request;

		AsyncCallback<ExternalInfoRequest> callback = new AsyncCallback<ExternalInfoRequest>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(ExternalInfoRequest result)
			{
				return;
			}
		};

		request = new ExternalInfoRequest();
		request.parentDataObjectId = "8C85AB5C-9C90-404D-B1F3-A03C0116BCD9";
		request.parentDataTypeId = "D5FD2D1B-59FB-4171-961A-A02E0121C81B";
		request.subject = "Questão sobre 'A Tua'";
		request.message.emailId = null;
		request.message.notes = "Qual é, meu?";
		request.message.upgrades = null;
		request.replylimit = 14;

		Services.subCasualtyService.createExternalRequest(request, callback);
	}
}
