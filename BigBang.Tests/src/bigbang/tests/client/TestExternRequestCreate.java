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
		request.parentDataObjectId = "EB3E2719-E7B2-4BB1-A39E-A08400432A03";
		request.parentDataTypeId = "09963260-CDB1-4207-B856-A03800B8AFC8";
		request.subject = "Questão sobre 'A Tua'";
		request.message.emailId = "AAMkADdmNDVhMDgyLWQzNDItNDRjNy04Mjk5LWY2YjgwZjA0YTAwOABGAAAAAADy5wo1GEKBQ7mf4MGQSeS5BwCo4XVv2UW6RZN0o9Yy+qctAClNyBCCAADDZpLTOBZZQoBQCyW3yamgAClNHAwDAAA=";
		request.message.notes = "Qual é, meu?";
		request.message.upgrades = null;
		request.replylimit = 14;

		Services.expenseService.createExternalRequest(request, callback);
	}
}
