package bigbang.tests.client;

import bigBang.definitions.shared.QuoteRequestObject;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.quoteRequestModule.shared.QuoteRequestObjectSearchParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestQRObjectGet
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		QuoteRequestObjectSearchParameter parameter;

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

		parameter = new QuoteRequestObjectSearchParameter();
		parameter.requestId = "B567259B-90E9-4E78-83D9-A00D011DEAFF";

		Services.quoteRequestObjectService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {}, 5, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<QuoteRequestObject> callback = new AsyncCallback<QuoteRequestObject>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(QuoteRequestObject result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.quoteRequestObjectService.getObject(stub.id, callback);
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

		Services.quoteRequestObjectService.closeSearch(workspaceId, callback);
	}
}
