package bigbang.tests.client;

import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.receiptModule.shared.ReceiptSearchParameter;
import bigBang.module.receiptModule.shared.ReceiptSortParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptDelete
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		ReceiptSearchParameter search;
		ReceiptSortParameter sort;

		AsyncCallback<NewSearchResult> callback = new AsyncCallback<NewSearchResult>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(NewSearchResult result)
			{
				if ( result != null )
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

		search = new ReceiptSearchParameter();
		search.freeText = "A123";
		sort = new ReceiptSortParameter();
		sort.field = ReceiptSortParameter.SortableField.NUMBER;
		sort.order = SortOrder.ASC;

		Services.receiptService.openSearch(new SearchParameter[] {search}, new SortParameter[] {sort}, 5, callback);
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

		Services.receiptService.deleteReceipt(stub.id, callback);
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

		Services.receiptService.closeSearch(workspaceId, callback);
	}
}
