package bigbang.tests.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Expense;

public class TestExpenseReceiveReturn
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Expense.ReturnEx returnEx;

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

		returnEx = new Expense.ReturnEx();
		returnEx.expenseId = "0D273CB9-122A-4DF5-B786-A03C00BB8E20";
		returnEx.reason = "Pessoa segura mal identificada.";

		Services.expenseService.receiveReturn(returnEx, callback);
	}
}
