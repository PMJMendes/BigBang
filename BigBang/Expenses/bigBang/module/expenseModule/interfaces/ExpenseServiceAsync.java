package bigBang.module.expenseModule.interfaces;

import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.Expense.Acceptance;
import bigBang.definitions.shared.Expense.ReturnEx;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.interfaces.ImageSubServiceAsync;
import bigBang.library.interfaces.SearchServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ExpenseServiceAsync
	extends SearchServiceAsync, ImageSubServiceAsync
{
	void getExpense(String expenseId, AsyncCallback<Expense> callback);
	void editExpense(Expense expense, AsyncCallback<Expense> callback);
	void sendNotification(String expenseId, AsyncCallback<Expense> callback);
	void receiveAcceptance(Acceptance acceptance, AsyncCallback<Expense> callback);
	void receiveReturn(ReturnEx returnEx, AsyncCallback<Expense> callback);
	void notifyClient(String expenseId, AsyncCallback<Expense> callback);
	void returnToClient(String expenseId, AsyncCallback<Expense> callback);
	void createInfoRequest(InfoOrDocumentRequest request, AsyncCallback<InfoOrDocumentRequest> callback);
	void createExternalRequest(ExternalInfoRequest request, AsyncCallback<ExternalInfoRequest> callback);
	void deleteExpense(String expenseId, String reason, AsyncCallback<Void> callback);
	void serialCreateExpense(Expense expense, DocuShareHandle source, AsyncCallback<Expense> callback);
	void massSendNotification(String[] expenseIds, AsyncCallback<Void> callback);
	void massReceiveReception(String[] expenseIds, DocuShareHandle source, AsyncCallback<Void> callback);
	void massNotifyClient(String[] expenseIds, AsyncCallback<Void> callback);
	void massReturnToClient(String[] expenseIds, AsyncCallback<Void> callback);
}
