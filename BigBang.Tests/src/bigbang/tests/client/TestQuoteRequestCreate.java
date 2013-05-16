package bigbang.tests.client;

//import bigBang.definitions.shared.QuoteRequest;
//
//import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestQuoteRequestCreate
{
//	private static String gstrPad;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
//		AsyncCallback<Remap[]> callback = new AsyncCallback<Remap[]>()
//		{
//			public void onFailure(Throwable caught)
//			{
//				return;
//			}
//
//			public void onSuccess(Remap[] result)
//			{
//				int i, j;
//
//				gstrPad = null;
//				for (i = 0; i < result.length; i++ )
//				{
//					if ( result[i].typeId.equalsIgnoreCase("6ABD2A4A-ECBD-46A8-A4E6-A00500CDAE4A") )
//					{
//						for ( j = 0; j < result[i].remapIds.length; j++ )
//						{
//							if ( result[i].remapIds[j].oldId == null )
//							{
//								gstrPad = result[i].remapIds[j].newId;
//								break;
//							}
//						}
//						break;
//					}
//				}
//
//				if ( gstrPad == null )
//					return;
//
//				DoStep2();
//			}
//		};
//
//		Services.quoteRequestService.openRequestScratchPad(null, callback);
//	}
//
//	private static void DoStep2()
//	{
//		QuoteRequest newRequest;
//
//		AsyncCallback<QuoteRequest> callback = new AsyncCallback<QuoteRequest>()
//		{
//			public void onFailure(Throwable caught)
//			{
//				return;
//			}
//
//			public void onSuccess(QuoteRequest result)
//			{
//				DoStep3(result);
//			}
//		};
//
//		newRequest = new QuoteRequest();
//		newRequest.id = gstrPad;
//		newRequest.clientId = "D7570502-0495-4E18-9CB3-9FB700201363";
//
//		Services.quoteRequestService.initRequestInPad(newRequest, callback);
//	}
//
//	private static void DoStep3(QuoteRequest request)
//	{
//		AsyncCallback<QuoteRequest> callback = new AsyncCallback<QuoteRequest>()
//		{
//			public void onFailure(Throwable caught)
//			{
//				return;
//			}
//
//			public void onSuccess(QuoteRequest result)
//			{
//				DoStep4();
//			}
//		};
//
//		request.mediatorId = null;
//		request.notes = "Umas quantas notas...";
//		request.caseStudy = false;
//
//		Services.quoteRequestService.updateHeader(request, callback);
//	}
//
//	private static void DoStep4()
//	{
//		AsyncCallback<Remap[]> callback = new AsyncCallback<Remap[]>()
//		{
//			public void onFailure(Throwable caught)
//			{
//				return;
//			}
//
//			public void onSuccess(Remap[] result)
//			{
//				return;
//			}
//		};
//
//		Services.quoteRequestService.commitPad(gstrPad, callback);
	}
}
