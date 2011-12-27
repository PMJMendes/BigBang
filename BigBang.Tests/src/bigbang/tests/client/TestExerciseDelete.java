package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.TipifiedListItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExerciseDelete
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
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Void result)
			{
				DoStep5();
			}
		};

		Services.insurancePolicyService.deleteExerciseInPad(tempObjectId, callback);
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
