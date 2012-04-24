package bigbang.tests.client;

import bigBang.definitions.shared.Expense;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExpenseReceiveAcceptance
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Expense.Acceptance acceptance;

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

		acceptance = new Expense.Acceptance();
		acceptance.expenseId = "27F3557D-C25D-4FC3-BFD3-A03C00B9811A";
		acceptance.settlement = "100.52";

		Services.expenseService.receiveAcceptance(acceptance, callback);
	}
}
