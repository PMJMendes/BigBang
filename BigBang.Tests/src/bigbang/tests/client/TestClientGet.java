package bigbang.tests.client;

import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.clientModule.shared.ClientSearchParameter;
import bigBang.module.clientModule.shared.ClientSortParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestClientGet
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		ClientSearchParameter parameter;
		ClientSortParameter sorts;

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

		parameter = new ClientSearchParameter();
		parameter.freeText = "CREDITE";
		sorts = new ClientSortParameter();
		sorts.field = ClientSortParameter.SortableField.NAME;
		sorts.order = SortOrder.ASC;
		
		Services.clientService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {sorts}, 5, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<Client> callback = new AsyncCallback<Client>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Client result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.clientService.getClient(stub.id, callback);
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

		Services.clientService.closeSearch(workspaceId, callback);
	}
}
