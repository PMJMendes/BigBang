package bigBang.module.expenseModule.interfaces;

import bigBang.definitions.shared.Expense;
import bigBang.library.interfaces.SearchService;
import bigBang.library.shared.BigBangException;
import bigBang.library.shared.SessionExpiredException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ExpenseService")
public interface ExpenseService extends SearchService {
	/**
	 * Utility class for simplifying access to the instance of async service.
	 */
	public static class Util {
		private static ExpenseServiceAsync instance;
		public static ExpenseServiceAsync getInstance(){
			if (instance == null) {
				instance = GWT.create(ExpenseService.class);
			}
			return instance;
		}
	}

	public Expense getExpense(String expenseId) throws SessionExpiredException, BigBangException;

	public Expense editExpense(Expense expense) throws SessionExpiredException, BigBangException;

	public void deleteExpense(String expenseId, String reason) throws SessionExpiredException, BigBangException;
}
