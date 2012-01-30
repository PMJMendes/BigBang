package bigbang.tests.client;

import bigBang.definitions.shared.InfoOrDocumentRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestInfoReqGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<InfoOrDocumentRequest> callback = new AsyncCallback<InfoOrDocumentRequest>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InfoOrDocumentRequest result)
			{
				return;
			}
		};

		Services.infoOrDocumentRequestService.getRequest("AD5EEEE7-E646-4F9D-9E41-9FE80137FC19", callback);
	}
}
