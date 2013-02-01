package bigbang.tests.client;

import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.Remap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestQRObjectCreate
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
		AsyncCallback<QuoteRequestObject> callback = new AsyncCallback<QuoteRequestObject> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(QuoteRequestObject result)
			{
				DoStep3(result);
			}
		};

		Services.quoteRequestService.createObjectInPad(gstrPad, "CD709854-DB59-424B-904A-9F9501403847", callback);
	}

	private static void DoStep3(QuoteRequestObject object)
	{
		AsyncCallback<QuoteRequestObject> callback = new AsyncCallback<QuoteRequestObject> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(QuoteRequestObject result)
			{
				DoStep4();
			}
		};

		object.unitIdentification = "Peste";
		Services.quoteRequestService.updateObjectInPad(object, callback);
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
