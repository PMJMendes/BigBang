package bigbang.tests.client;

import bigBang.definitions.shared.ManagerTransfer;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestMgrXferMassAccept
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

		Services.transferManagerService.massAcceptTransfer("B88B2A92-146C-43F3-8D88-9FC600FD296C", callback);
	}
}
