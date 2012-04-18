package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSubCasualtyDelete
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

		Services.subCasualtyService.deleteSubCasualty("B8A20B89-6AC3-4538-A07C-A03701260645", "Porque sim...", callback);
	}
}
