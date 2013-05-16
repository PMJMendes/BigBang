package bigbang.tests.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReportExcel
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

		Services.reportService.generateParamAsXL(/*"D4AD4585-539D-454E-97A4-A0BE0112D1CE", */"7612C979-1937-4B83-BA9A-A0F301100E28",
				new String[] {"F38731EB-1FC4-4453-B026-A0FE00E1B27C", null/*, null, null, null, null, null*/}, callback);
	}

	private static void DoStep2(final String pstrRef)
	{
		RequestBuilder lobjBuilder;

		RequestCallback callback = new RequestCallback()
		{
			public void onResponseReceived(Request request, Response response)
			{
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
