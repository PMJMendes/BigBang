package bigbang.tests.client;

import bigBang.definitions.shared.DocuShareItem;
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
		DocuShareItem source;

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

		source = new DocuShareItem();
		source.handle = "Document-453607";
		source.locationHandle = null;

		Services.receiptService.receiveImage("4D6C559B-9893-4FA5-9516-A014013A205C", source, callback);
	}
}
