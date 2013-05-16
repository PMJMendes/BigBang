package bigbang.tests.client;

import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSearchParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestQuoteRequestDelete
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		QuoteRequestSearchParameter parameter;

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

		parameter = new QuoteRequestSearchParameter();
		parameter.freeText = "24535";
		parameter.includeClosed = true;

		Services.quoteRequestService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {}, 5, callback);
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

		Services.quoteRequestService.deleteRequest(stub.id, "Porque Sim!", callback);
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

		Services.quoteRequestService.closeSearch(workspaceId, callback);
	}
}
