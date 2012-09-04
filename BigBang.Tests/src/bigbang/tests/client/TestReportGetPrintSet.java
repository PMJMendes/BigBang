package bigbang.tests.client;

import bigBang.definitions.shared.Report;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReportGetPrintSet
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

		Services.reportService.generatePrintSetReport("B2F9808F-340B-43AF-BDA1-A03201262121", "24D03787-D22E-4861-B2EA-A046011B3CE2", callback);
	}
}
