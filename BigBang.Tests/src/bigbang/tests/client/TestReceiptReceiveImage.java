package bigbang.tests.client;

import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.Receipt;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptReceiveImage
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		DocuShareHandle source;

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

		source = new DocuShareHandle();
		source.handle = "Document-456132";
		source.locationHandle = null;

		Services.receiptService.receiveImage("9C3511F4-B2C4-4E6D-BBBA-A02101302172", source, callback);
	}
}
