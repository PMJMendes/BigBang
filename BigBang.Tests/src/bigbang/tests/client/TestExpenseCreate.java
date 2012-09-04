package bigbang.tests.client;

import bigBang.definitions.shared.Expense;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class TestExpenseCreate
{
	public static void DoTest()
	{
		DoStep1();
	}

	private static void DoStep1()
	{
		Expense expense;

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

		expense = new Expense();
		expense.referenceId = "026CDFCF-17EB-41B6-ABEE-9FFA00FE0E40";
		expense.referenceTypeId = "D0C5AE6B-D340-4171-B7A3-9F81011F5D42";
		expense.expenseDate = "2012-04-01";
		expense.insuredObjectId = null;
		expense.coverageId = "F2FFD078-C85B-4F1A-93BB-9FFA00FE0E4F";
		expense.value = 123.45;
		expense.settlement = null;
		expense.isManual = false;
		expense.notes = "Notas de rodapé...";

		Services.insurancePolicyService.createExpense(expense, callback);
	}
}
