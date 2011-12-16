package bigbang.tests.client;

import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.tasksModule.shared.TaskSearchParameter;
import bigBang.module.tasksModule.shared.TaskSortParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestAgendaDismiss
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		TaskSortParameter sorts;

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

		sorts = new TaskSortParameter();
		sorts.field = TaskSortParameter.SortableField.STATUS;
		sorts.order = SortOrder.ASC;

		Services.tasksService.openSearch(new SearchParameter[] {new TaskSearchParameter()},
				new SortParameter[] {sorts}, 5, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<Void> callback = new AsyncCallback<Void>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Void result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.tasksService.dismissTask(stub.id, callback);
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

		Services.tasksService.closeSearch(workspaceId, callback);
	}
}
