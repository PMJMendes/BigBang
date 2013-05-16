package bigbang.tests.client;

import bigBang.definitions.shared.Report;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReportGetTransactionSet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Report> callback = new AsyncCallback<Report>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Report result)
			{
				return;
			}
		};

		Services.reportService.generateTransactionSetReport("B39C1B5A-BD1D-4C70-B998-A09900FBA339", "4CB7E5C9-6120-4AC1-A573-A11E00BD430F", callback);
	}
}
