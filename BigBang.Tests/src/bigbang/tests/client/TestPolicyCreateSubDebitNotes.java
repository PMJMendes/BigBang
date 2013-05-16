package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.DebitNoteBatch;

public class TestPolicyCreateSubDebitNotes
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		DebitNoteBatch batch;

		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Void result)
			{
				return;
			}
		};

		batch = new DebitNoteBatch();
		batch.policyId = "7648703F-D8CE-472A-8F59-A0FE00E40AA8";
		batch.maturityDate = "2013-01-01";
		batch.endDate = "2013-03-31";
		batch.limitDate = "2012-12-20";

		Services.insurancePolicyService.createSubPolicyReceipts(batch, callback);
	}
}
