package bigbang.tests.client;

import bigBang.definitions.shared.InsurerAccountingExtra;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptInsurerAccounting
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		InsurerAccountingExtra extra;

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

		extra = new InsurerAccountingExtra();
		extra.insurerId = "B1965886-2947-4C0D-8C8A-A0FE00E1AEAD";
		extra.text = "Comissões adicionais";
		extra.value = 20000.0;
		extra.isCommissions = true;
		extra.hasTax = true;

		Services.receiptService.massInsurerAccounting(new String[0], new InsurerAccountingExtra[] {extra}, callback);
	}
}
