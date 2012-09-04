package bigbang.tests.client;

import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.Rectangle;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.receiptModule.shared.ReceiptSearchParameter;
import bigBang.module.receiptModule.shared.ReceiptSortParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestReceiptEditAndValidate
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
				if ( (result != null) && (result.results != null) && (result.results.length > 0) )
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

		search = new ReceiptSearchParameter();
		search.ownerId = "F4D6391A-CBB3-4555-BCB1-9FA900BA4838";
		sort = new ReceiptSortParameter();
		sort.field = ReceiptSortParameter.SortableField.NUMBER;
		sort.order = SortOrder.ASC;

		Services.receiptService.openSearch(new SearchParameter[] {search}, new SortParameter[] {sort}, 5, callback);
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
				DoStep3(result);
			}
		};

		Services.receiptService.getReceipt(stub.id, callback);
	}

	private static void DoStep3(Receipt receipt)
	{
		Rectangle rect;

		AsyncCallback<Receipt> callback = new AsyncCallback<Receipt>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Receipt result)
			{
				DoStep4(tmpWorkspace);
			}
		};

		receipt.maturityDate = "2012-01-01";
		receipt.endDate = "2012-12-31";
		receipt.description = "Teste";

		rect = new Rectangle();
		rect.x1 = 0;
		rect.y1 = 1500;
		rect.x2 = 1654;
		rect.y2 = 2000;

		Services.receiptService.editAndValidateReceipt(receipt, rect, callback);
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

		Services.receiptService.closeSearch(workspaceId, callback);
	}
}
