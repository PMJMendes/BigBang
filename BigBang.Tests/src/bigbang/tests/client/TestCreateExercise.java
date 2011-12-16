package bigbang.tests.client;

import bigBang.definitions.shared.ExerciseStub;
import bigBang.definitions.shared.InsurancePolicy;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestCreateExercise
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
		AsyncCallback<ExerciseStub> callback = new AsyncCallback<ExerciseStub> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(ExerciseStub result)
			{
				DoStep4(result);
			}
		};

		Services.insurancePolicyService.createFirstExercise(policy.scratchPadId, callback);
	}

	private static void DoStep4(ExerciseStub exercise)
	{
		AsyncCallback<ExerciseStub> callback = new AsyncCallback<ExerciseStub> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(ExerciseStub result)
			{
				DoStep5();
			}
		};

		exercise.label = "Inácio";
		exercise.startDate = "2011-12-01";
		Services.insurancePolicyService.updateExerciseInPad(exercise, callback);
	}

	private static void DoStep5()
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
