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

		Services.historyService.undo("1B14D73A-8281-4D82-B8E6-A00E0132E335", callback);
	}
}
