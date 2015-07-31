package bigbang.tests.client;

import bigBang.definitions.shared.ScanHandle;
import bigBang.definitions.shared.Receipt;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptCreateSerial
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Receipt receipt;
		ScanHandle source;

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

		receipt = new Receipt();
		receipt.number = "QQ003";
		receipt.ownerId = "01B7B31C-0EFF-4A93-84EE-A02000F26B1F";
		receipt.typeId = "6B91D626-4CAD-4F53-8FD6-9F900111C39F";
		receipt.totalPremium = 153.42;
		receipt.comissions = 13.2;
		receipt.retrocessions = 0.0;
		receipt.issueDate = "2011-11-30";

		source = new ScanHandle();
		source.docushare = true;
		source.handle = "Document-456130";
		source.locationHandle = null;

		Services.receiptService.serialCreateReceipt(receipt, source, callback);
	}
}
