package bigBang.module.expenseModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.shared.HealthExpenseStub;
import bigBang.library.client.dataAccess.SearchDataBrokerImpl;
import bigBang.library.interfaces.SearchServiceAsync;
import bigBang.module.expenseModule.interfaces.ExpenseService;

public class ExpenseSearchDataBroker extends SearchDataBrokerImpl<HealthExpenseStub> implements SearchDataBroker<HealthExpenseStub>{

	public ExpenseSearchDataBroker(){
		this(ExpenseService.Util.getInstance());
	}
	
	public ExpenseSearchDataBroker(SearchServiceAsync service) {
		super(service);
	}

}
