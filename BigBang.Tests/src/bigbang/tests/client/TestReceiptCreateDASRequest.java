package bigbang.tests.client;

import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.DASRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptCreateDASRequest
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		DASRequest request;

		AsyncCallback<Receipt> callback = new AsyncCallback<Receipt>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Receipt result)
			{
				return;
			}
		};

		request = new DASRequest();
		request.receiptId = "C0DD296F-1262-42FB-8025-A021012FE728";
		request.replylimit = 7;
		Services.receiptService.createDASRequest(request, callback);
	}
}
