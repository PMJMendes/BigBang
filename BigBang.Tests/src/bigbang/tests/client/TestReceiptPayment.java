package bigbang.tests.client;

import bigBang.definitions.shared.Receipt;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptPayment
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Receipt.PaymentInfo info;

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

		info = new Receipt.PaymentInfo();
		info.receiptId = "E62C9524-408F-4A78-916F-A0E90057A04D";
		info.payments = new Receipt.PaymentInfo.Payment[] {new Receipt.PaymentInfo.Payment()};
		info.payments[0].paymentTypeId = "40B9ACC7-A99A-4DC2-BAEF-A02200EB59B3";
		Services.receiptService.markPayed(info, callback);
	}
}
