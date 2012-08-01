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
		transfer.dataObjectIds = new String[] {"32794C5A-BA2C-473E-803E-A08F0141D472", "0ADC7578-ED26-486B-8068-A08F0141D472",
				"E9DA642F-BC9D-413D-810E-A08F0141D472", "9A0EC7C7-8059-40C9-8309-A08F0141D472"};
		transfer.newManagerId = "3365E127-5A77-451E-BFD7-A08F01419BA9";

		Services.clientService.massCreateManagerTransfer(transfer, callback);
	}
}
