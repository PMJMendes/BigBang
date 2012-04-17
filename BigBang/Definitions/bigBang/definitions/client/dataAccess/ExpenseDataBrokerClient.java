package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.HealthExpense;

public interface ExpenseDataBrokerClient extends DataBrokerClient<HealthExpense>{

	public void addExpense(HealthExpense expense);
	
	public void updateExpense(HealthExpense expense);
	
	public void deleteExpense(String id);
	
}
