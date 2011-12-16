package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestObjectCreate
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
		AsyncCallback<InsuredObject> callback = new AsyncCallback<InsuredObject> ()
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

		Services.insurancePolicyService.createObjectInPad(policy.scratchPadId, callback);
	}

	private static void DoStep4(InsuredObject object)
	{
		AsyncCallback<InsuredObject> callback = new AsyncCallback<InsuredObject> ()
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

		object.unitIdentification = "Peste";
		Services.insurancePolicyService.updateObjectInPad(object, callback);
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
