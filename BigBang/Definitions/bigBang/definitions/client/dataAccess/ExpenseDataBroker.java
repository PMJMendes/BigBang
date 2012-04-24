package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
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

}
