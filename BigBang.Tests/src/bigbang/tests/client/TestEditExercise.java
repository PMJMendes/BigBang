package bigbang.tests.client;

import bigBang.definitions.shared.ExerciseStub;
import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.TipifiedListItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestEditExercise
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
	
		Services.insurancePolicyService.getPolicy("F4D6391A-CBB3-4555-BCB1-9FA900BA4838", callback);
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
		AsyncCallback<ExerciseStub> callback = new AsyncCallback<ExerciseStub>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(ExerciseStub result)
			{
				DoStep5(result);
			}
		};

		Services.insurancePolicyService.getExerciseInPad(tempObjectId, callback);
	}

	private static void DoStep5(ExerciseStub exercise)
	{
		AsyncCallback<ExerciseStub> callback = new AsyncCallback<ExerciseStub>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(ExerciseStub result)
			{
				DoStep6();
			}
		};

		exercise.label = "Início";
		exercise.endDate = "2012-12-01";
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
