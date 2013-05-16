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
		request.receiptId = "7870394D-D69A-48D9-B8A9-A0BC00B9A4BE";
		request.replylimit = 7;
		Services.receiptService.createDASRequest(request, callback);
	}
}
