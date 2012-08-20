package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.Rectangle;

public class TestReceiptCrop
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Rectangle rect;

		AsyncCallback<Document> callback = new AsyncCallback<Document>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Document result)
			{
				return;
			}
		};

		rect = new Rectangle();
		rect.x1 = 0;
		rect.y1 = 1500;
		rect.x2 = 1654;
		rect.y2 = 2000;

		Services.receiptService.cropRectangle("BE3D25BC-4579-4C22-87F4-A0B3010076DB", rect, callback);
	}
}
