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

		Services.reportService.generateParamReport("8D11763D-4B0B-44EF-8779-A0A701270881",
				new String[] {"29631C89-6783-4EBA-A214-A0A600B8AFFA", "15DABF21-62F3-4D79-9AFE-A0A701250BFE"}, callback);
	}
}
