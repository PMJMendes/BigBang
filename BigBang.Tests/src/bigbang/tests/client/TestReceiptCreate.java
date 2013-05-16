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
		receipt.number = "333333";
		receipt.typeId = "6B91D626-4CAD-4F53-8FD6-9F900111C39F";
		receipt.totalPremium = 100.0;
		receipt.salesPremium = 80.0;
		receipt.comissions = 8.33;
		receipt.issueDate = "2012-11-30";
		receipt.dueDate = "2012-12-10";
		receipt.maturityDate = "2012-12-01";
		receipt.endDate = "2013-03-01";
		Services.insurancePolicyService.createReceipt("6ddf2dd1-c3e3-4906-9322-a0e9000f2290", receipt, callback);
	}
}
