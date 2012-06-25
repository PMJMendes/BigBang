package bigbang.tests.client;

import bigBang.definitions.shared.TransactionSet;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReportGetTransactionSet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<TransactionSet[]> callback = new AsyncCallback<TransactionSet[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(TransactionSet[] result)
			{
				return;
			}
		};

		Services.reportService.getTransactionSets("2FF04A44-2D5A-4686-B8B6-A0320126ABA5", callback);
	}
}
