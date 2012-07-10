package bigbang.tests.client;

import bigBang.definitions.shared.Expense;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExpenseEdit
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		AsyncCallback<Expense> callback = new AsyncCallback<Expense>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Expense result)
			{
				DoStep2(result);
			}
		};

		Services.expenseService.getExpense("9F99D844-C525-4605-9955-A03F00E5F4AE", callback);
	}

	private static void DoStep2(Expense expense)
	{
		AsyncCallback<Expense> callback = new AsyncCallback<Expense>()
		{
			public void onFailure(Throwable caught)
			{
				return;
			}

			public void onSuccess(Expense result)
			{
				return;
			}
		};

		expense.settlement = 120.0;
		expense.isManual = true;

		Services.expenseService.editExpense(expense, callback);
	}
}
