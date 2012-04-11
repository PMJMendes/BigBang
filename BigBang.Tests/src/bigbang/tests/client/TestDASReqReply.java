package bigbang.tests.client;

import bigBang.definitions.shared.DASRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestDASReqReply
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		DASRequest.Response response;

		AsyncCallback<DASRequest> callback = new AsyncCallback<DASRequest>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(DASRequest result)
			{
				return;
			}
		};

		response = new DASRequest.Response();
		response.requestId = "E37CB9FA-DD33-454E-8B59-A02D0111BA37";
		Services.dasRequestService.receiveResponse(response, callback);
	}
}
