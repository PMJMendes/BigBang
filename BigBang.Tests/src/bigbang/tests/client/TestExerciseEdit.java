package bigbang.tests.client;

import bigBang.definitions.shared.Exercise;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.TipifiedListItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExerciseEdit
{
	private static InsurancePolicy tmpPolicy;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<InsurancePolicy> callback = new AsyncCallback<InsurancePolicy> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}
	
			public void onSuccess(InsurancePolicy result)
			{
				DoStep2(result);
			}
		};
	
		Services.insurancePolicyService.getPolicy("FBA922E2-E2CE-4351-ABD5-9FBB00CE51B2", callback);
	}

	private static void DoStep2(InsurancePolicy policy)
	{
		AsyncCallback<InsurancePolicy> callback = new AsyncCallback<InsurancePolicy> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}
	
			public void onSuccess(InsurancePolicy result)
			{
				tmpPolicy = result;
				DoStep3(result);
			}
		};
	
		Services.insurancePolicyService.openForEdit(policy, callback);
	}

	private static void DoStep3(InsurancePolicy policy)
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
					DoStep4(result[0].id);
				else
					return;
			}
		};

		Services.insurancePolicyService.getPadItemsFilter("DEE32F69-B33D-4427-AD5B-9F9C001607F2", policy.scratchPadId, callback);
	}

	private static void DoStep4(String tempObjectId)
	{
		AsyncCallback<Exercise> callback = new AsyncCallback<Exercise>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Exercise result)
			{
				DoStep5(result);
			}
		};

		Services.insurancePolicyService.getExerciseInPad(tempObjectId, callback);
	}

	private static void DoStep5(Exercise exercise)
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
				DoStep6();
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

	private static void DoStep6()
	{
		AsyncCallback<InsurancePolicy> callback = new AsyncCallback<InsurancePolicy>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsurancePolicy result)
			{
				return;
			}
		};

		Services.insurancePolicyService.commitPolicy(tmpPolicy.scratchPadId, callback);
	}
}
