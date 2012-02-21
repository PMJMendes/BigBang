package bigbang.tests.client;

import bigBang.definitions.shared.Negotiation;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.quoteRequestModule.shared.NegotiationSearchParameter;
import bigBang.module.quoteRequestModule.shared.NegotiationSortParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestNegotiationDelete
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		NegotiationSearchParameter parameter;
		NegotiationSortParameter sorts;

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

		parameter = new NegotiationSearchParameter();
		parameter.ownerId = "026CDFCF-17EB-41B6-ABEE-9FFA00FE0E40";
		parameter.freeText = "algumas";
		sorts = new NegotiationSortParameter();
		sorts.field = NegotiationSortParameter.SortableField.RELEVANCE;
		sorts.order = SortOrder.ASC;
		
		Services.negotiationService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {sorts}, 5, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		Negotiation.Deletion deletion;

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

		deletion = new Negotiation.Deletion();
		deletion.negotiationId = stub.id;
		deletion.motive = "Aberta em duplicado.";

		Services.negotiationService.deleteNegotiation(deletion, callback);
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

		Services.negotiationService.closeSearch(workspaceId, callback);
	}
}
