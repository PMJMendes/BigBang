package bigBang.definitions.client.dataAccess;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ScanHandle;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.Expense.Acceptance;
import bigBang.definitions.shared.Expense.ReturnEx;
import bigBang.definitions.shared.ExpenseStub;


public interface ExpenseDataBroker extends DataBrokerInterface<Expense>{

	SearchDataBroker<ExpenseStub> getSearchBroker();

	void getExpense(String id, ResponseHandler<Expense> handler);

	void updateExpense(Expense expense, ResponseHandler<Expense> handler);

	void removeExpense(String id, String reason, ResponseHandler<String> handler);

	void sendNotification(String id, ResponseHandler<Expense> handler);
	
	void receiveAcceptance(Acceptance acceptance, ResponseHandler<Expense> handler);
	
	void receiveReturn(ReturnEx returnEx, ResponseHandler<Expense> handler);
	
	void massSendNotification(String[] expenseIds, ResponseHandler<Void> handler);

	void notifyClient(String expenseId, ResponseHandler<Expense> responseHandler);

	void returnToClient(String expenseId,
			ResponseHandler<Expense> responseHandler);

	void massReturnToClient(String[] expenseIds, ResponseHandler<Void> handler);

	void massNotifyClient(String[] toNotify,
			ResponseHandler<Void> responseHandler);

	void getExpensesForOwner(String ownerId, ResponseHandler<Collection<ExpenseStub>> handler);

	void serialCreateExpense(Expense expense, ScanHandle handle,
			ResponseHandler<Expense> responseHandler);

	void massReceiveProof(String[] toReceive,
			ScanHandle handle, ResponseHandler<Void> responseHandler);

	void sendMessage(Conversation info,
			ResponseHandler<Conversation> responseHandler);

	void receiveMessage(Conversation info,
			ResponseHandler<Conversation> responseHandler);
	
}
