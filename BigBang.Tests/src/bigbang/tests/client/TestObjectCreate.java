package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsuredObject;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestObjectCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<InsurancePolicy> callback = new AsyncCallback<InsurancePolicy>()
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

		Services.insurancePolicyService.getPolicy("B832C42B-573F-4EE3-AEF6-A0B8001118FD", callback);
	}

	private static void DoStep2(InsurancePolicy policy)
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

		policy.changedObjects = new InsuredObject[] {new InsuredObject(policy.emptyObject)};
		policy.changedObjects[0].unitIdentification = "TESTE 2";
		policy.changedObjects[0].headerFields[1].value = "Zé";

		Services.insurancePolicyService.editPolicy(policy, callback);
	}
}
