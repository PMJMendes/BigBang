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

		Services.expenseService.getExpense("30538EB5-5A53-4201-8706-A039010FB953", callback);
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

		expense.settlement = "120";
		expense.isManual = true;

		Services.expenseService.editExpense(expense, callback);
	}
}
