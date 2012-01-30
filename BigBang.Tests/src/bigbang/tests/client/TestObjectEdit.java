package bigbang.tests.client;

import bigBang.definitions.shared.InsuredObject;
import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.TipifiedListItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestObjectEdit
{
	private static String gstrPad;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		final String lstrPolicy = "588E0BE9-6E92-4711-B0CD-9FD50118C191";

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
					if ( result[i].typeId.equalsIgnoreCase("D0C5AE6B-D340-4171-B7A3-9F81011F5D42") )
					{
						for ( j = 0; j < result[i].remapIds.length; j++ )
						{
							if ( lstrPolicy.equalsIgnoreCase(result[i].remapIds[j].oldId) )
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

		Services.insurancePolicyService.openPolicyScratchPad(lstrPolicy, callback);
	}

	private static void DoStep2()
	{
		AsyncCallback<TipifiedListItem[]> callback = new AsyncCallback<TipifiedListItem[]> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}
	
			public void onSuccess(TipifiedListItem[] result)
			{
				if ( (result != null) && (result.length > 0) )
					DoStep3(result[0].id);
				else
					return;
			}
		};

		Services.insurancePolicyService.getListItemsFilter("3A3316D2-9D7C-4FD1-8486-9F9C0012E119", gstrPad, callback);
	}

	private static void DoStep3(String tempObjectId)
	{
		AsyncCallback<InsuredObject> callback = new AsyncCallback<InsuredObject>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsuredObject result)
			{
				DoStep4(result);
			}
		};

		Services.insurancePolicyService.getObjectInPad(tempObjectId, callback);
	}

	private static void DoStep4(InsuredObject object)
	{
		int i, j, k, n;

		AsyncCallback<InsuredObject> callback = new AsyncCallback<InsuredObject>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsuredObject result)
			{
				DoStep5();
			}
		};

		object.unitIdentification = "Teste";

		n = 101;
		for ( i = 0; i < object.headerData.fixedFields.length; i++ )
		{
			object.headerData.fixedFields[i].value = Integer.toString(n);
			n++;
		}
		for ( i = 0; i < object.headerData.variableFields.length; i++ )
		{
			for ( j = 0; j < object.headerData.variableFields[i].data.length; j++ )
			{
				object.headerData.variableFields[i].data[j].value = Integer.toString(n);
				n++;
			}
		}
		for ( i = 0; i < object.coverageData.length; i++ )
		{
			for ( j = 0; j < object.coverageData[i].fixedFields.length; j++ )
			{
				object.coverageData[i].fixedFields[j].value = Integer.toString(n);
				n++;
			}
			for ( j = 0; j < object.coverageData[i].variableFields.length; j++ )
			{
				for ( k = 0; k < object.coverageData[i].variableFields[j].data.length; k++ )
				{
					object.coverageData[i].variableFields[j].data[k].value = Integer.toString(n);
					n++;
				}
			}
		}

		Services.insurancePolicyService.updateObjectInPad(object, callback);
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

		Services.insurancePolicyService.commitPad(gstrPad, callback);
	}
}
