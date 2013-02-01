package bigbang.tests.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReportParamHTML
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<String> callback = new AsyncCallback<String>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(String result)
			{
				DoStep2(result);
			}
		};

		Services.reportService.generateParamAsHTML("7612C979-1937-4B83-BA9A-A0F301100E28",
				new String[] {"C77DDD1B-13D8-495F-AF7D-A0E80184B5E9"}, callback);
	}

	private static void DoStep2(final String pstrRef)
	{
		RequestBuilder lobjBuilder;

		RequestCallback callback = new RequestCallback()
		{
			public void onResponseReceived(Request request, Response response)
			{
//				String lstr = response.getText();

				DoStep3(pstrRef);
			}

			public void onError(Request request, Throwable exception)
			{
				return;
			}
		};

		lobjBuilder = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() + "bbfile?fileref=" + pstrRef);

		try
		{
			lobjBuilder.sendRequest(null, callback);
		}
		catch (Throwable e)
		{
			return;
		}
	}

	private static void DoStep3(String pstrRef)
	{
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

		Services.fileService.Discard(pstrRef, callback);
	}
}
