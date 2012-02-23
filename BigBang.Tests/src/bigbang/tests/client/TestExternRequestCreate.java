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
		request.parentDataObjectId = "D5697A30-26E4-4913-9EDE-9FFE011B6F30";
		request.parentDataTypeId = "0D50EB51-725D-4741-8618-9FFD00E918D3";
		request.emailId = "AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwCtSKTbKCtrR6MYTzZmb/1MAAADRQAbAAAjNXAn+Af6QrQHRLqsAzc5ABMr6TphAAA=";
		request.subject = null;
		request.text = null;
		request.replylimit = 14;
		request.upgrades = null;

		Services.negotiationService.createExternalRequest(request, callback);
	}
}
