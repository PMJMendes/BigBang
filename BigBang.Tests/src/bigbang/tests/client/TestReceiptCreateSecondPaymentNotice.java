package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptCreateSecondPaymentNotice
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
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

		Services.receiptService.massCreateSecondPaymentNotice(new String[] {"BCBAE9BD-9BED-4461-801B-A13301569904",
				"2E512249-606E-4692-BBB5-A10E0161B554", "B3EC2D32-407C-46E1-895D-A12301032DEA", "23447C99-34BA-42E8-AF54-A13B0103CBE0",
				"E3628CD9-6C7F-4FBD-9672-A118011C0967", "FA7D7392-8BA7-4FDA-801E-A117012BB19B"}, callback);
	}
}
