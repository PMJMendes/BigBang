package bigbang.tests.client;

import bigBang.definitions.shared.DASRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestDASReqRepeat
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

		Services.dasRequestService.getRequest("E37CB9FA-DD33-454E-8B59-A02D0111BA37", callback);
	}

	private static void DoStep2(DASRequest request)
	{
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

		request.replylimit = 21;
		Services.dasRequestService.repeatRequest(request, callback);
	}
}
