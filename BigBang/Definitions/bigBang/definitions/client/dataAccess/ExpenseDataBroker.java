package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.HealthExpense;
import bigBang.definitions.shared.HealthExpenseStub;


public interface ExpenseDataBroker extends DataBrokerInterface<HealthExpense>{

	SearchDataBroker<HealthExpenseStub> getSearchBroker();

}
