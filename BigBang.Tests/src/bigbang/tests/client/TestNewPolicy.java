package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestNewPolicy
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		InsurancePolicy newPolicy;

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

		newPolicy = new InsurancePolicy();
		newPolicy.subLineId = "34E19434-6106-4359-93FE-9EE90118CEE0";

		Services.insurancePolicyService.initializeNewPolicy(newPolicy, callback);
	}

	private static void DoStep2(InsurancePolicy testPolicy)
	{
		AsyncCallback<InsurancePolicy> callback = new AsyncCallback<InsurancePolicy>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsurancePolicy result)
			{
				DoStep3(result);
			}
		};

		testPolicy.clientId = "21C84FD4-4F8E-4F63-A3EA-9F92015BD90A";
		testPolicy.caseStudy = false;
		testPolicy.insuranceAgencyId = "E4BBA479-ED15-4EA5-B5E8-9F92015BD5E9";
		testPolicy.startDate = "2012-01-01";
		testPolicy.durationId = "FFF15F7F-EB59-40D4-8E86-9F810157FD24";
		testPolicy.fractioningId = "B8234D73-4432-45A0-B670-9F8101580CB5";
		testPolicy.maturityDay = 1;
		testPolicy.maturityMonth = 1;

		Services.insurancePolicyService.updateHeader(testPolicy, callback);
	}

	private static void DoStep3(InsurancePolicy testPolicy)
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

		Services.insurancePolicyService.commitPolicy(testPolicy.scratchPadId, callback);
	}
}
