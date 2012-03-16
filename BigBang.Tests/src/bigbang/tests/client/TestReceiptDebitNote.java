package bigbang.tests.client;

import bigBang.definitions.shared.DebitNote;
import bigBang.definitions.shared.Receipt;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptDebitNote
{
	public static String receiptId = "2753DCB8-6D23-4B02-8699-A01600F5F16F";

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<DebitNote[]> callback = new AsyncCallback<DebitNote[]>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(DebitNote[] result)
			{
				if ( (result != null) && (result.length != 0) )
					DoStep2(result[0].id);
			}
		};

		Services.receiptService.getRelevantDebitNotes(receiptId, callback);
	}

	private static void DoStep2(String debitNoteId)
	{
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

		Services.receiptService.associateWithDebitNote(receiptId, debitNoteId, callback);
	}
}
