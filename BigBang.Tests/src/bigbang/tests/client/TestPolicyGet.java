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

		Services.insurancePolicyService.getPolicy("FFA24734-C5AC-4AD5-80A4-A05100CA1A78", callback);
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

		Services.insurancePolicyService.getPage("FFA24734-C5AC-4AD5-80A4-A05100CA1A78",
				"778D45D1-6D67-456A-863B-A05100CA24A9", null, callback);
	}
}
