package bigbang.tests.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestConversationPrint
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

		Services.conversationService.getForPrinting("E3E971E7-FD02-4154-9A2E-A13500B3EA64", callback);
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
