package bigbang.tests.client;

import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.TipifiedListItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExerciseDelete
{
	private static String gstrPad;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		final String lstrPolicy = "54FF52B3-076D-4E3E-B825-9FD000C6AF77";

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

		Services.insurancePolicyService.getListItemsFilter("DEE32F69-B33D-4427-AD5B-9F9C001607F2", gstrPad, callback);
	}

	private static void DoStep3(String exerciseId)
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

		Services.insurancePolicyService.deleteExerciseInPad(exerciseId, callback);
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

		Services.insurancePolicyService.commitPad(gstrPad, callback);
	}
}
