package bigbang.tests.client;

import bigBang.definitions.shared.SubPolicy;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubPolicyEdit
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
				DoStep2(result);
			}
		};

		Services.subPolicyService.getSubPolicy("87B5A725-64F9-44E0-B65B-A0FE00E41644", callback);
	}

	private static void DoStep2(SubPolicy policy)
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

		if ( policy.number.startsWith("x") )
			policy.number = policy.number.substring(1);
		else
			policy.number = "x" + policy.number;

		Services.subPolicyService.editSubPolicy(policy, callback);
	}
}
