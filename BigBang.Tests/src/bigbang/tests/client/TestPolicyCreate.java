package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestPolicyCreate
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

		Services.insurancePolicyService.getEmptyPolicy("BEBB58B5-CD95-4872-B72F-9EE90118938F", "145F0BA8-0893-40B5-A71F-A0FE00E1B27C",
				callback);
	}

	private static void DoStep2(InsurancePolicy empty)
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

		empty.clientId = "145F0BA8-0893-40B5-A71F-A0FE00E1B27C";
		empty.insuranceAgencyId = "B68AA910-208D-4940-8124-A0FE00E1AEAD";
		empty.durationId = "FFF15F7F-EB59-40D4-8E86-9F810157FD24";
		empty.fractioningId = "B8234D73-4432-45A0-B670-9F8101580CB5";
		empty.startDate = "2012-10-01";

		Services.clientService.createPolicy(empty, callback);
	}
}
