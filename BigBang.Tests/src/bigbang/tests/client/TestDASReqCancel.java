package bigbang.tests.client;

import bigBang.definitions.shared.DASRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestDASReqCancel
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<DASRequest> callback = new AsyncCallback<DASRequest>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(DASRequest result)
			{
				DoStep2(result);
			}
		};

		Services.dasRequestService.getRequest("53315D30-4E6E-4839-8B82-A03001104942", callback);
	}

	private static void DoStep2(DASRequest request)
	{
		DASRequest.Cancellation cancellation;

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

		cancellation = new DASRequest.Cancellation();
		cancellation.requestId = request.id;
		cancellation.motiveId = "B464EFA6-A770-49C6-B038-9FE900C93F9A";
		Services.dasRequestService.cancelRequest(cancellation, callback);
	}
}
