package bigbang.tests.client;

import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.Remap;
import bigBang.definitions.shared.TipifiedListItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubExerciseEdit
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

		Services.insurancePolicyService.getListItemsFilter("DEE32F69-B33D-4427-AD5B-9F9C001607F2", gstrPad, callback);
	}

	private static void DoStep3(String exerciseId)
	{
		AsyncCallback<Exercise> callback = new AsyncCallback<Exercise>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Exercise result)
			{
				DoStep4(result);
			}
		};

		Services.insurancePolicyService.getExerciseInPad(exerciseId, callback);
	}

	private static void DoStep4(Exercise exercise)
	{
		int i, j, k, n;

		AsyncCallback<Exercise> callback = new AsyncCallback<Exercise>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Exercise result)
			{
				DoStep5();
			}
		};

		exercise.label = "Início";
		exercise.endDate = "2012-12-01";

		n = 1001;
		for ( i = 0; i < exercise.headerData.fixedFields.length; i++ )
		{
			exercise.headerData.fixedFields[i].value = Integer.toString(n);
			n++;
		}
		for ( i = 0; i < exercise.headerData.variableFields.length; i++ )
		{
			for ( j = 0; j < exercise.headerData.variableFields[i].data.length; j++ )
			{
				exercise.headerData.variableFields[i].data[j].value = Integer.toString(n);
				n++;
			}
		}
		for ( i = 0; i < exercise.coverageData.length; i++ )
		{
			for ( j = 0; j < exercise.coverageData[i].fixedFields.length; j++ )
			{
				exercise.coverageData[i].fixedFields[j].value = Integer.toString(n);
				n++;
			}
			for ( j = 0; j < exercise.coverageData[i].variableFields.length; j++ )
			{
				for ( k = 0; k < exercise.coverageData[i].variableFields[j].data.length; k++ )
				{
					exercise.coverageData[i].variableFields[j].data[k].value = Integer.toString(n);
					n++;
				}
			}
		}

		Services.insurancePolicyService.updateExerciseInPad(exercise, callback);
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
