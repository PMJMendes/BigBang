package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.ExternalInfoRequest;

public class TestExternRequestReceive
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		ExternalInfoRequest.Incoming response;

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

		response = new ExternalInfoRequest.Incoming();
		response.requestId = "0D867A6D-ADAE-41F8-8826-A00001005C32";
		response.notes = null;
		response.emailId = "AAMkADg1OTUzYzcxLTVmZjQtNDU3Zi04Nzg3LWYwODFhMDE5MzlkNQBGAAAAAABr2ZTbJcmoQYK7SlFUwi1VBwCtSKTbKCtrR6MYTzZmb/1MAAADRQAbAACtSKTbKCtrR6MYTzZmb/1MAAADRWwBAAA=";
		response.replylimit = 14;

		Services.externRequestService.receiveAdditional(response, callback);
	}
}
