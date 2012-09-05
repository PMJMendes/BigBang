package bigbang.tests.client;

import bigBang.definitions.shared.InsuredObjectOLD;
import bigBang.definitions.shared.Remap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubObjectCreate
{
	private static String gstrPad;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		final String lstrPolicy = "2238CC33-CBBB-4FC1-A7E8-9FFA011E42DD";

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
					if ( result[i].typeId.equalsIgnoreCase("C7BC8D2F-BD61-43D5-9347-9FF300EE9986") )
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

		Services.subPolicyService.openSubPolicyScratchPad(lstrPolicy, callback);
	}

	private static void DoStep2()
	{
		AsyncCallback<InsuredObjectOLD> callback = new AsyncCallback<InsuredObjectOLD> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsuredObjectOLD result)
			{
				DoStep3(result);
			}
		};

		Services.subPolicyService.createObjectInPad(gstrPad, callback);
	}

	private static void DoStep3(InsuredObjectOLD object)
	{
		AsyncCallback<InsuredObjectOLD> callback = new AsyncCallback<InsuredObjectOLD> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsuredObjectOLD result)
			{
				DoStep4();
			}
		};

		object.unitIdentification = "Peste";
		Services.subPolicyService.updateObjectInPad(object, callback);
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

		Services.subPolicyService.commitPad(gstrPad, callback);
	}
}
