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

		Services.subPolicyService.getSubPolicy("dd9d5ed0-0b8e-40b8-b3e5-a0fe00e41644", callback);
	}
}
