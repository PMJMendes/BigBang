package bigbang.tests.client;

import bigBang.definitions.shared.ManagerTransfer;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestMgrXferMassCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		ManagerTransfer transfer;

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

		transfer = new ManagerTransfer();
		transfer.managedProcessIds = new String[] {"898B5C0F-47F9-4BFD-988C-9FB70020177D", "F6A0F123-98F3-417D-AE8D-9FB70020177E",
				"C058AC25-A3E8-47A1-BF6B-9FB70020177F", "7C4CB7A3-1767-4651-B8E5-9FB70020177F",
				"1E96ED8F-B9B4-4979-ACF3-9FB700201781"};
		transfer.newManagerId = "1836B62E-239F-4A1A-B50E-9FB700200FB4";

		Services.clientService.massCreateManagerTransfer(transfer, callback);
	}
}
