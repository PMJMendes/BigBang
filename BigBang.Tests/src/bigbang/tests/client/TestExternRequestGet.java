package bigbang.tests.client;

import bigBang.definitions.shared.ExternalInfoRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExternRequestGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
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

		Services.externRequestService.getRequest("0D867A6D-ADAE-41F8-8826-A00001005C32", callback);
	}
}
