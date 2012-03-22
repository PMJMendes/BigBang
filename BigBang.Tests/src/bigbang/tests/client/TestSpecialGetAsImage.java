package bigbang.tests.client;

import bigBang.library.interfaces.FileService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSpecialGetAsImage
{
	private static String tmpString;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<String> callback = new AsyncCallback<String> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(String result)
			{
				tmpString = result;
				DoStep2(result);
			}
		};

		Services.docuShareService.getItemAsImage("Document-455161", callback);
	}

	private static void DoStep2(String fileStr)
	{
		RequestBuilder lbuilder;

		RequestCallback callback = new RequestCallback()
		{
			public void onError(Request request, Throwable exception)
			{
				return;
			}

			public void onResponseReceived(Request request, Response response)
			{
				DoStep3(tmpString);
			}
		};

		lbuilder = new RequestBuilder(RequestBuilder.GET, GWT.getModuleBaseURL() +  FileService.GET_PREFIX + fileStr);
		try
		{
			lbuilder.sendRequest(null, callback);
		}
		catch (Throwable e)
		{
			return;
		}
	}

	private static void DoStep3(String fileStr)
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

		Services.fileService.Discard(fileStr, callback);
	}
}
