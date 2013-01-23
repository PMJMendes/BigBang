package bigbang.tests.client;

import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.receiptModule.shared.ReceiptSearchParameter;
import bigBang.module.receiptModule.shared.ReceiptSortParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptGet
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		ReceiptSearchParameter parameter;
		ReceiptSortParameter sorts;

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

		parameter = new ReceiptSearchParameter();
		parameter.freeText = "5601";
		parameter.internalOnly = true;
//		parameter.paymentFrom = "2012-12-01";
//		parameter.paymentTo = "2013-01-03";
		sorts = new ReceiptSortParameter();
		sorts.field = ReceiptSortParameter.SortableField.RELEVANCE;
		sorts.order = SortOrder.DESC;

		Services.receiptService.openForOperation("F5F00701-69F7-4622-BB8C-9FB800DED93F", new SearchParameter[] {parameter},
				new SortParameter[] {sorts}, 50, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<Receipt> callback = new AsyncCallback<Receipt>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Receipt result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.receiptService.getReceipt(stub.id, callback);
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
