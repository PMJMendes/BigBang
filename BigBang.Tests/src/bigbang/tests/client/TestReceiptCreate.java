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
		receipt.number = "A123";
		receipt.typeId = "6B91D626-4CAD-4F53-8FD6-9F900111C39F";
		receipt.totalPremium = "153.42";
		receipt.comissions = "13.2";
		receipt.retrocessions = "0";
		receipt.issueDate = "2011-11-30";
		Services.insurancePolicyService.createReceipt("588E0BE9-6E92-4711-B0CD-9FD50118C191", receipt, callback);
	}
}
