package bigbang.tests.client;

import bigBang.definitions.shared.ManagerTransfer;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestMgrXferAccept
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<ManagerTransfer> callback = new AsyncCallback<ManagerTransfer>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(ManagerTransfer result)
			{
				return;
			}
		};

		Services.transferManagerService.acceptTransfer("C3561111-33D8-4481-8D12-9FBE00E98CBD", callback);
	}
}
