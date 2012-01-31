package bigbang.tests.client;

import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.HistorySearchParameter;
import bigBang.library.shared.HistorySortParameter;
import bigBang.library.shared.NewSearchResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestHistoryGet
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		HistorySearchParameter parameter;
		HistorySortParameter sorts;

		AsyncCallback<NewSearchResult> callback = new AsyncCallback<NewSearchResult>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(NewSearchResult result)
			{
				if ( result.workspaceId != null )
				{
					if ( (result.results != null) && (result.results.length > 0) )
					{
						tmpWorkspace = result.workspaceId;
						DoStep2(result.results[0]);
					}
					else
						DoStep3(result.workspaceId);
				}
				else
					return;
			}
		};

		parameter = new HistorySearchParameter();
		parameter.dataObjectId = "AD5EEEE7-E646-4F9D-9E41-9FE80137FC19";
		sorts = new HistorySortParameter();
		sorts.field = HistorySortParameter.SortableField.TIMESTAMP;
		sorts.order = SortOrder.DESC;
		
		Services.historyService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {sorts}, 5, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<HistoryItem> callback = new AsyncCallback<HistoryItem>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(HistoryItem result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.historyService.getItem(stub.id, callback);
	}

	private static void DoStep3(String workspaceId)
	{
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

		Services.historyService.closeSearch(workspaceId, callback);
	}
}
