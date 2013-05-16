package bigbang.tests.client;

//import bigBang.definitions.shared.QuoteRequestObject;
//import bigBang.definitions.shared.TipifiedListItem;
//
//import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestQRObjectEdit
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
//					DoStep5();
//			}
//		};
//
//		Services.quoteRequestService.getListItemsFilter("B594AB7F-573F-4401-A369-A00500F9D2D8", gstrPad, callback);
//	}
//
//	private static void DoStep3(String tempObjectId)
//	{
//		AsyncCallback<QuoteRequestObject> callback = new AsyncCallback<QuoteRequestObject>()
//		{
//			public void onFailure(Throwable caught)
//			{
//				return;
//			}
//
//			public void onSuccess(QuoteRequestObject result)
//			{
//				DoStep4(result);
//			}
//		};
//
//		Services.quoteRequestService.getObjectInPad(tempObjectId, callback);
//	}
//
//	private static void DoStep4(QuoteRequestObject object)
//	{
//		int i, j, k;
//
//		AsyncCallback<QuoteRequestObject> callback = new AsyncCallback<QuoteRequestObject>()
//		{
//			public void onFailure(Throwable caught)
//			{
//				return;
//			}
//
//			public void onSuccess(QuoteRequestObject result)
//			{
//				DoStep5();
//			}
//		};
//
//		object.unitIdentification = "Teste";
//
//		if ( object.objectData != null )
//		{
//			for ( i = 0; i < object.objectData.length; i++ )
//			{
//				if ( (object.objectData[i].headerData != null) && (object.objectData[i].headerData.fixedFields != null) )
//					for ( j = 0; j < object.objectData[i].headerData.fixedFields.length; j++ )
//						object.objectData[i].headerData.fixedFields[j].value = null;
//
//				if ( object.objectData[i].coverageData != null )
//				{
//					for ( j = 0; j < object.objectData[i].coverageData.length; j++ )
//					{
//						if ( object.objectData[i].coverageData[j].fixedFields != null )
//							for ( k = 0; k < object.objectData[i].coverageData[j].fixedFields.length; k++ )
//								object.objectData[i].coverageData[j].fixedFields[k].value = null;
//					}
//				}
//			}
//		}
//		Services.quoteRequestService.updateObjectInPad(object, callback);
//	}
//
//	private static void DoStep5()
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
