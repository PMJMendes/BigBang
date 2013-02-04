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
		AsyncCallback<Receipt> callback = new AsyncCallback<Receipt>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Receipt result)
			{
				DoStep2(result);
			}
		};

		Services.receiptService.getReceipt("065C7B88-BFF0-47A0-BFCC-A08900EA532A", callback);
	}

	private static void DoStep2(Receipt receipt)
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

		receipt.description = "x -> " + receipt.description;

		Services.receiptService.receiveImage(receipt, source, callback);
	}
}
