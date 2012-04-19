package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.Expense;

public interface ExpenseDataBrokerClient extends DataBrokerClient<Expense>{

	public void addExpense(Expense expense);
	
	public void updateExpense(Expense expense);
	
	public void deleteExpense(String id);
	
}
