package bigbang.tests.client;

import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequest.TableSection;
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
		int i, j;

		AsyncCallback<QuoteRequest> callback = new AsyncCallback<QuoteRequest>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(QuoteRequest result)
			{
				int i, j, k;
				boolean b;

				b = false;
				if ( result.requestData != null )
				{
					for ( i = 0; i < result.requestData.length; i++ )
					{
						if ( result.requestData[i].tableData != null )
						{
							for ( j = 0; j < result.requestData[i].tableData.length; j++ )
							{
								if ( result.requestData[i].tableData[j].data != null )
								{
									for ( k = 0; k < result.requestData[i].tableData[j].data.length; k++ )
										result.requestData[i].tableData[j].data[k].value = null;
									DoStep4(result.requestData[i].tableData[j]);
									b = true;
									break;
								}
							}
							if ( b )
								break;
						}
					}
				}
				if ( !b )
					DoStep5();
			}
		};

		if ( request.requestData != null )
		{
			for ( i = 0; i < request.requestData.length; i++ )
			{
				if ( request.requestData[i].headerFields != null )
					for ( j = 0; j < request.requestData[i].headerFields.length; j++ )
						request.requestData[i].headerFields[j].value = null;

				if ( request.requestData[i].extraData != null )
					for ( j = 0; j < request.requestData[i].extraData.length; j++ )
						request.requestData[i].extraData[j].value = null;
			}
		}
		Services.quoteRequestService.updateHeader(request, callback);
	}

	private static void DoStep4(QuoteRequest.TableSection page)
	{
		AsyncCallback<TableSection> callback = new AsyncCallback<TableSection>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(TableSection result)
			{
				DoStep5();
			}
		};

		Services.quoteRequestService.savePage(page, callback);
	}

	private static void DoStep5()
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
