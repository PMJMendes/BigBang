package bigbang.tests.client;

import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.casualtyModule.shared.CasualtySearchParameter;
import bigBang.module.casualtyModule.shared.CasualtySortParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestCasualtyGet
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		CasualtySearchParameter parameter;
		CasualtySortParameter sorts;

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

		parameter = new CasualtySearchParameter();
		parameter.freeText = "0002";
		sorts = new CasualtySortParameter();
		sorts.field = CasualtySortParameter.SortableField.RELEVANCE;
		sorts.order = SortOrder.ASC;

		Services.casualtyService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {sorts}, 5, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<Casualty> callback = new AsyncCallback<Casualty>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Casualty result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.casualtyService.getCasualty(stub.id, callback);
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

		Services.casualtyService.closeSearch(workspaceId, callback);
	}
}
