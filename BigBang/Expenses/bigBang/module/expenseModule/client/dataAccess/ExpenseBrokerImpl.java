package bigBang.module.expenseModule.client.dataAccess;

import bigBang.definitions.client.dataAccess.DataBroker;
import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HealthExpense;
import bigBang.module.expenseModule.interfaces.ExpenseService;
import bigBang.module.expenseModule.interfaces.ExpenseServiceAsync;

public class ExpenseBrokerImpl extends DataBroker<HealthExpense> implements ExpenseDataBroker{

	private ExpenseServiceAsync service;
	
	public ExpenseBrokerImpl() {
		this(ExpenseService.Util.getInstance());
	}
	
	
	public ExpenseBrokerImpl(ExpenseServiceAsync service) {
		this.service = service;
		this.dataElementId = BigBangConstants.EntityIds.EXPENSE;
		this.cache.setEnabled(false);
	}
	@Override
	public void requireDataRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyItemCreation(String itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyItemDeletion(String itemId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyItemUpdate(String itemId) {
		// TODO Auto-generated method stub
		
	}

}
