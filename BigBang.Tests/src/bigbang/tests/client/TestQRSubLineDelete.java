package bigbang.tests.client;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.Remap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestQRSubLineDelete
{
	private static String gstrPad;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		final String lstrRequest = "CDA354D6-0905-48F4-A220-A00E010A0EA5";

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
		AsyncCallback<QuoteRequest> callback = new AsyncCallback<QuoteRequest> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}
	
			public void onSuccess(QuoteRequest result)
			{
				if ( (result.requestData != null) && (result.requestData.length > 0) )
					DoStep3(result.requestData[0]);
				else
					DoStep4();
			}
		};

		Services.quoteRequestService.getRequestInPad(gstrPad, callback);
	}

	private static void DoStep3(QuoteRequest.RequestSubLine subLine)
	{
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Void result)
			{
				DoStep4();
			}
		};

		Services.quoteRequestService.deleteSubLineFromPad(subLine.qrslId, callback);
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
