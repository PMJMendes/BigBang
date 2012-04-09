package bigbang.tests.client;

import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SignatureRequest;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptCreateSignatureRequest
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		SignatureRequest request;

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

		request = new SignatureRequest();
		request.receiptId = "D8A120C8-810A-4A7B-9FC3-A02D010E8584";
		request.replylimit = 7;
		Services.receiptService.createSignatureRequest(request, callback);
	}
}
