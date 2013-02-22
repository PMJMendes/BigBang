package bigbang.tests.client;

//import bigBang.definitions.shared.QuoteRequest;
//
//import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestQRSubLineCreate
{
//	private static String gstrPad;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
//		final String lstrRequest = "CDA354D6-0905-48F4-A220-A00E010A0EA5";
//
//		AsyncCallback<Remap[]> callback = new AsyncCallback<Remap[]> ()
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
//							if ( lstrRequest.equalsIgnoreCase(result[i].remapIds[j].oldId) )
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
//		Services.quoteRequestService.openRequestScratchPad(lstrRequest, callback);
//	}
//
//	private static void DoStep2()
//	{
//		AsyncCallback<QuoteRequest.RequestSubLine> callback = new AsyncCallback<QuoteRequest.RequestSubLine> ()
//		{
//			public void onFailure(Throwable caught)
//			{
//				return;
//			}
//
//			public void onSuccess(QuoteRequest.RequestSubLine result)
//			{
//				DoStep3();
//			}
//		};
//
//		Services.quoteRequestService.addSubLineToPad(gstrPad, "34E19434-6106-4359-93FE-9EE90118CEE0", callback);
//	}
//
//	private static void DoStep3()
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
