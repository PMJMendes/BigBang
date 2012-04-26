package bigBang.module.expenseModule.interfaces;

import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;
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

	public Expense sendNotification(String expenseId) throws SessionExpiredException, BigBangException;
	public Expense receiveAcceptance(Expense.Acceptance acceptance) throws SessionExpiredException, BigBangException;
	public Expense receiveReturn(Expense.ReturnEx returnEx) throws SessionExpiredException, BigBangException;

	public Expense notifyClient(String expenseId) throws SessionExpiredException, BigBangException;
	public Expense returnToClient(String expenseId) throws SessionExpiredException, BigBangException;

	public InfoOrDocumentRequest createInfoRequest(InfoOrDocumentRequest request) throws SessionExpiredException, BigBangException;
	public ExternalInfoRequest createExternalRequest(ExternalInfoRequest request) throws SessionExpiredException, BigBangException;

	public void deleteExpense(String expenseId, String reason) throws SessionExpiredException, BigBangException;

	public void massSendNotification(String[] expenseIds) throws SessionExpiredException, BigBangException;
	public void massNotifyClient(String[] expenseIds) throws SessionExpiredException, BigBangException;
	public void massReturnToClient(String[] expenseIds) throws SessionExpiredException, BigBangException;
}
