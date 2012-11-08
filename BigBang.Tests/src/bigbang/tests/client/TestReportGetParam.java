package bigbang.tests.client;

import bigBang.definitions.shared.Report;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReportGetParam
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

		Services.reportService.generateParamReport("976E150E-1AAF-4F40-AA09-A0CF00C37533",
				new String[] {null}, callback);
	}
}
