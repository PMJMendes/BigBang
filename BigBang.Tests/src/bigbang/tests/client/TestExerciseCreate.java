package bigbang.tests.client;

import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.Remap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExerciseCreate
{
	private static String gstrPad;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		final String lstrPolicy = "026CDFCF-17EB-41B6-ABEE-9FFA00FE0E40";

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
		AsyncCallback<Exercise> callback = new AsyncCallback<Exercise> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Exercise result)
			{
				DoStep3(result);
			}
		};

		Services.insurancePolicyService.createFirstExercise(gstrPad, callback);
	}

	private static void DoStep3(Exercise exercise)
	{
		AsyncCallback<Exercise> callback = new AsyncCallback<Exercise> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Exercise result)
			{
				DoStep4();
			}
		};

		exercise.label = "2013";
		exercise.startDate = "2013-01-01";
		exercise.endDate = "2013-12-31";
		Services.insurancePolicyService.updateExerciseInPad(exercise, callback);
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
