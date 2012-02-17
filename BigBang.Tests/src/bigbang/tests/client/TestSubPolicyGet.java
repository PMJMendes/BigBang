package bigbang.tests.client;

import bigBang.definitions.shared.SubPolicy;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubPolicyGet
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<SubPolicy> callback = new AsyncCallback<SubPolicy> ()
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

		Services.subPolicyService.getSubPolicy("F01F675F-CB85-46E2-90E1-9FFA010FB114", callback);
	}
}
