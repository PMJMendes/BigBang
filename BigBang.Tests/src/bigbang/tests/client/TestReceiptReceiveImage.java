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
		source.handle = "Document-467769";
		source.locationHandle = null;

		Services.receiptService.receiveImage("065C7B88-BFF0-47A0-BFCC-A08900EA532A", source, callback);
	}
}
