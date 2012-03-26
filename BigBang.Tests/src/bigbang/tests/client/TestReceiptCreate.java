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
		receipt.number = "ZZ333";
		receipt.typeId = "6B91D626-4CAD-4F53-8FD6-9F900111C39F";
		receipt.totalPremium = "153.42";
		receipt.comissions = "13.2";
		receipt.retrocessions = "0";
		receipt.issueDate = "2011-11-30";
		Services.insurancePolicyService.createReceipt("01B7B31C-0EFF-4A93-84EE-A02000F26B1F", receipt, callback);
	}
}
