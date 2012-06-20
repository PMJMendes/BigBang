package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Receipt;

public class TestReceiptCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Receipt receipt;

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
		receipt.number = "E001";
		receipt.typeId = "BFC1AE6D-53E8-41AF-84BE-9F900111D967";
		receipt.totalPremium = 153.42;
		receipt.comissions = 13.2;
		receipt.retrocessions = 0.0;
		receipt.issueDate = "2011-11-30";
		Services.insurancePolicyService.createReceipt("01B7B31C-0EFF-4A93-84EE-A02000F26B1F", receipt, callback);
	}
}
