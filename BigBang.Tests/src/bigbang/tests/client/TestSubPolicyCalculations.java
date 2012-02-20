package bigbang.tests.client;

import bigBang.definitions.shared.SubPolicy;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubPolicyCalculations
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<SubPolicy> callback = new AsyncCallback<SubPolicy>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(SubPolicy result)
			{
				return;
			}
		};

		Services.subPolicyService.performCalculations("2238CC33-CBBB-4FC1-A7E8-9FFA011E42DD", callback);
	}
}
