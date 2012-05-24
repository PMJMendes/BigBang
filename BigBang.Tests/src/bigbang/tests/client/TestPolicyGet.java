package bigbang.tests.client;

import bigBang.definitions.shared.InsurancePolicy;
import bigBang.definitions.shared.InsurancePolicy.TableSection;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestPolicyGet
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
				DoStep2();
			}
		};

		Services.insurancePolicyService.getPolicy("87c1b24d-daa1-42bc-864a-a058003c4dba", callback);
	}

	private static void DoStep2()
	{
		AsyncCallback<TableSection> callback = new AsyncCallback<TableSection>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(TableSection result)
			{
				return;
			}
		};

		Services.insurancePolicyService.getPage("87c1b24d-daa1-42bc-864a-a058003c4dba",
				"070a6adc-48bc-428c-a785-a058003ca488", null, callback);
	}
}
