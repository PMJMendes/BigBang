package bigbang.tests.client;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest.Cancellation;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestInfoReqCancel
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
				DoStep2(result);
			}
		};

		Services.infoOrDocumentRequestService.getRequest("AD5EEEE7-E646-4F9D-9E41-9FE80137FC19", callback);
	}

	private static void DoStep2(InfoOrDocumentRequest request)
	{
		Cancellation cancellation;

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

		cancellation = new Cancellation();
		cancellation.requestId = request.id;
		cancellation.motiveId = "B464EFA6-A770-49C6-B038-9FE900C93F9A";
		Services.infoOrDocumentRequestService.cancelRequest(cancellation, callback);
	}
}
