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

		Services.historyService.undo("1BA8B5B0-BC2F-4670-B707-A01701022DA0", callback);
	}
}
