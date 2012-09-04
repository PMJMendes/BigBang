package bigbang.tests.client;

import bigBang.definitions.shared.Receipt;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptSetReturn
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Receipt.ReturnMessage message;

		AsyncCallback<Receipt> callback = new AsyncCallback<Receipt>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Receipt result)
			{
				return;
			}
		};

		message = new Receipt.ReturnMessage();
		message.receiptId = "FEEB7760-CCCD-485E-B242-A0160114A216";
		message.motiveId = null; //TODO: Pôr aqui um motivo de teste

		Services.receiptService.setForReturn(message, callback);
	}
}
