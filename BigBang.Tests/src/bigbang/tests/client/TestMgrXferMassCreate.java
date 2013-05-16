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
		transfer.dataObjectIds = new String[] {"8F474C7D-DE85-4BEC-8139-9FB70020135F", "330B083F-3840-4AB6-A982-9FB70020135F",
				"318A93F9-C1EE-455B-8DB6-9FB70020135F", "7627D3E2-538E-4848-BDF5-9FB70020135F",
				"9C118BBA-A621-47A5-BFFA-9FB70020135F"};
		transfer.newManagerId = "1836B62E-239F-4A1A-B50E-9FB700200FB4";

		Services.clientService.massCreateManagerTransfer(transfer, callback);
	}
}
