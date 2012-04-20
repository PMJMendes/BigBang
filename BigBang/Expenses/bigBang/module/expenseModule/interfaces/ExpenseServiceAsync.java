package bigBang.module.expenseModule.interfaces;

import com.google.gwt.user.client.rpc.AsyncCallback;

import bigBang.definitions.shared.Expense;
import bigBang.library.interfaces.SearchServiceAsync;

public interface ExpenseServiceAsync extends SearchServiceAsync {

	void getExpense(String expenseId, AsyncCallback<Expense> callback);
	void editExpense(Expense expense, AsyncCallback<Expense> callback);
	void deleteExpense(String expenseId, String reason, AsyncCallback<Void> callback);
}
