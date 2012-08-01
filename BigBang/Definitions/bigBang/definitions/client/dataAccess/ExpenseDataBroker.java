package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.Expense.Acceptance;
import bigBang.definitions.shared.Expense.ReturnEx;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest;


public interface ExpenseDataBroker extends DataBrokerInterface<Expense>{

	SearchDataBroker<ExpenseStub> getSearchBroker();

	void getExpense(String id, ResponseHandler<Expense> handler);

	void updateExpense(Expense expense, ResponseHandler<Expense> handler);

	void removeExpense(String id, String reason, ResponseHandler<String> handler);

	void sendNotification(String id, ResponseHandler<Expense> handler);
	
	void receiveAcceptance(Acceptance acceptance, ResponseHandler<Expense> handler);
	
	void receiveReturn(ReturnEx returnEx, ResponseHandler<Expense> handler);
	
	void massSendNotification(String[] expenseIds, ResponseHandler<Void> handler);

	void createExternalInfoRequest(ExternalInfoRequest toSend,
			ResponseHandler<ExternalInfoRequest> responseHandler);

	void createInfoOrDocumentRequest(InfoOrDocumentRequest request,
			ResponseHandler<InfoOrDocumentRequest> responseHandler);

	void notifyClient(String expenseId, ResponseHandler<Expense> responseHandler);

	void returnToClient(String expenseId,
			ResponseHandler<Expense> responseHandler);

	void massReturnToClient(String[] expenseIds, ResponseHandler<Void> handler);

	void massNotifyClient(String[] toNotify,
			ResponseHandler<Void> responseHandler);

	void getExpensesForOwner(String ownerId, ResponseHandler<Collection<ExpenseStub>> handler);

	void serialCreateExpense(Expense expense, DocuShareHandle handle,
			ResponseHandler<Expense> responseHandler);
	
}
