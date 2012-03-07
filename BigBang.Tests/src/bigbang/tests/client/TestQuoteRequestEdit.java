package bigbang.tests.client;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.Remap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestQuoteRequestEdit
{
	private static String gstrPad;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		final String lstrRequest = "F8F245CE-5072-40E8-BBF9-A00D01040F4F";

		AsyncCallback<Remap[]> callback = new AsyncCallback<Remap[]> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Remap[] result)
			{
				int i, j;

				gstrPad = null;
				for (i = 0; i < result.length; i++ )
				{
					if ( result[i].typeId.equalsIgnoreCase("6ABD2A4A-ECBD-46A8-A4E6-A00500CDAE4A") )
					{
						for ( j = 0; j < result[i].remapIds.length; j++ )
						{
							if ( lstrRequest.equalsIgnoreCase(result[i].remapIds[j].oldId) )
							{
								gstrPad = result[i].remapIds[j].newId;
								break;
							}
						}
						break;
					}
				}

				if ( gstrPad == null )
					return;

				DoStep2();
			}
		};

		Services.quoteRequestService.openRequestScratchPad(lstrRequest, callback);
	}

	private static void DoStep2()
	{
		AsyncCallback<QuoteRequest> callback = new AsyncCallback<QuoteRequest>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(QuoteRequest result)
			{
				DoStep3(result);
			}
		};

		Services.quoteRequestService.getRequestInPad(gstrPad, callback);
	}

	private static void DoStep3(QuoteRequest request)
	{
		AsyncCallback<QuoteRequest> callback = new AsyncCallback<QuoteRequest>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(QuoteRequest result)
			{
				DoStep4();
			}
		};

		request.notes = "Notas mais a sério...";
		Services.quoteRequestService.updateHeader(request, callback);
	}

	private static void DoStep4()
	{
		AsyncCallback<Remap[]> callback = new AsyncCallback<Remap[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Remap[] result)
			{
				return;
			}
		};

		Services.quoteRequestService.commitPad(gstrPad, callback);
	}
}
