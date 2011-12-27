package bigbang.tests.client;

import bigBang.definitions.shared.HistoryItem;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestSpecialUndo
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

		Services.historyService.undo("374FA521-778A-441B-95A2-9FC6010AB397", callback);
	}
}
