package bigbang.tests.client;

import bigBang.definitions.shared.DASRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestDASReqGet
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
				return;
			}
		};

		Services.dasRequestService.getRequest("53315D30-4E6E-4839-8B82-A03001104942", callback);
	}
}
