package bigbang.tests.client;

//import bigBang.definitions.shared.TipifiedListItem;
//
//import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestQRObjectDelete
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
//		AsyncCallback<TipifiedListItem[]> callback = new AsyncCallback<TipifiedListItem[]> ()
//		{
//			public void onFailure(Throwable caught)
//			{
//				return;
//			}
//	
//			public void onSuccess(TipifiedListItem[] result)
//			{
//				if ( (result != null) && (result.length > 0) )
//					DoStep3(result[0].id);
//				else
//					DoStep4();
//			}
//		};
//
//		Services.quoteRequestService.getListItemsFilter("B594AB7F-573F-4401-A369-A00500F9D2D8", gstrPad, callback);
//	}
//
//	private static void DoStep3(String tempObjectId)
//	{
//		AsyncCallback<Void> callback = new AsyncCallback<Void>()
//		{
//			public void onFailure(Throwable caught)
//			{
//				return;
//			}
//
//			public void onSuccess(Void result)
//			{
//				DoStep4();
//			}
//		};
//
//		Services.quoteRequestService.deleteObjectInPad(tempObjectId, callback);
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
