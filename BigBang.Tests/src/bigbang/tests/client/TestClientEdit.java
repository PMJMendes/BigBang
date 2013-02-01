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

public class TestClientEdit
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
						DoStep4(result.workspaceId);
				}
				else
					return;
			}
		};

		parameter = new ClientSearchParameter();
		parameter.freeText = "Gumbercindo";
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
				DoStep3(result);
			}
		};

		Services.clientService.getClient(stub.id, callback);
	}

	private static void DoStep3(Client client)
	{
		AsyncCallback<Client> callback = new AsyncCallback<Client>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Client result)
			{
				DoStep4(tmpWorkspace);
			}
		};

		client.name = "Gumbercindo Jonas";
		Services.clientService.editClient(client, callback);
	}

	private static void DoStep4(String workspaceId)
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
