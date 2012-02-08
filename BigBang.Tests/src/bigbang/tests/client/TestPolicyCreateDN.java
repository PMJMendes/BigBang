package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.DebitNote;

public class TestPolicyCreateDN
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		DebitNote note;

		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Void result)
			{
				return;
			}
		};

		note = new DebitNote();
		note.value = "123.54";
		note.maturityDate = "2012-03-31";

		Services.insurancePolicyService.createDebitNote("988F0431-476B-4CE0-93A9-9FEC00D6006B", note, callback);
	}
}
