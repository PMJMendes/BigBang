package bigbang.tests.client;

import bigBang.definitions.shared.ManagerTransfer;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestMgrXferCreate
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
		transfer.dataObjectIds = new String[] {"8F474C7D-DE85-4BEC-8139-9FB70020135F"};
		transfer.newManagerId = "CA20CE5A-C6A8-4820-81B4-9FB700200FB4";

		Services.clientService.createManagerTransfer(transfer, callback);
	}
}
