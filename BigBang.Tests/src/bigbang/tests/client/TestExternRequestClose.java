package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.ExternalInfoRequest;

public class TestExternRequestClose
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		ExternalInfoRequest.Closing closing;

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

		closing = new ExternalInfoRequest.Closing();
		closing.requestId = "0D867A6D-ADAE-41F8-8826-A00001005C32";
		closing.motiveId = "B464EFA6-A770-49C6-B038-9FE900C93F9A";

		Services.externRequestService.closeRequest(closing, callback);
	}
}
