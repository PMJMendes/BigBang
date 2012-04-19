package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.ExpenseStub;


public interface ExpenseDataBroker extends DataBrokerInterface<Expense>{

	SearchDataBroker<ExpenseStub> getSearchBroker();

}
