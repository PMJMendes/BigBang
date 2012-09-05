package bigbang.tests.client;

import bigBang.definitions.shared.InsuredObject;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestPolicyGetEmptyObject
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<InsuredObject> callback = new AsyncCallback<InsuredObject>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(InsuredObject result)
			{
				return;
			}
		};

		Services.insurancePolicyService.getEmptyObject("C1F15187-4201-49D8-9A7B-A0B8002EB131", callback);
	}
}
