package bigbang.tests.client;

import bigBang.definitions.shared.HistoryItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestHistoryUndo
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<HistoryItem> callback = new AsyncCallback<HistoryItem> ()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(HistoryItem result)
			{
				return;
			}
		};

		Services.historyService.undo("48BB3CC1-098A-4C8A-AE58-A00000FF0DEC", callback);
	}
}
