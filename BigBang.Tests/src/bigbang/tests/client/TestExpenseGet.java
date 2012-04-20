package bigbang.tests.client;

import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortOrder;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.shared.NewSearchResult;
import bigBang.module.expenseModule.shared.ExpenseSearchParameter;
import bigBang.module.expenseModule.shared.ExpenseSortParameter;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExpenseGet
{
	private static String tmpWorkspace;

	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		ExpenseSearchParameter parameter;
		ExpenseSortParameter sorts;

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

		parameter = new ExpenseSearchParameter();
		parameter.freeText = "1";
		sorts = new ExpenseSortParameter();
		sorts.field = ExpenseSortParameter.SortableField.RELEVANCE;
		sorts.order = SortOrder.ASC;

		Services.expenseService.openSearch(new SearchParameter[] {parameter}, new SortParameter[] {sorts}, 50, callback);
	}

	private static void DoStep2(SearchResult stub)
	{
		AsyncCallback<Expense> callback = new AsyncCallback<Expense>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Expense result)
			{
				DoStep3(tmpWorkspace);
			}
		};

		Services.expenseService.getExpense(stub.id, callback);
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
