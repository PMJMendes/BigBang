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

		Services.reportService.generateParamReport("D4AD4585-539D-454E-97A4-A0BE0112D1CE",
				new String[] {"AC6CBF6B-966B-40F6-AB63-A0B80001F6D9", null, null, null, null}, callback);
	}
}
