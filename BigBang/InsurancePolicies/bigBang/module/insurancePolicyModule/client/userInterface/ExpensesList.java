package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.module.expenseModule.client.userInterface.ExpenseSearchPanel;

public class ExpensesList extends FilterableList<ExpenseStub> {

	public class Entry extends ExpenseSearchPanel.Entry {

		public Entry(ExpenseStub value) {
			super(value);
			setLeftWidget(this.statusIcon);
		}
		
	}
	
	protected ExpenseDataBroker broker;
	protected String ownerId;
	
	public ExpensesList(){
		this.broker = (ExpenseDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.EXPENSE);
		showFilterField(false);
	}
	
	public void setOwner(String ownerId){
		this.ownerId = ownerId;
		if(ownerId == null) {
			clear();
		}else{
			broker.getExpensesForOwner(ownerId, new ResponseHandler<Collection<ExpenseStub>>(){

				@Override
				public void onResponse(Collection<ExpenseStub> response) {
					clear();
					for(ExpenseStub expense : response) {
						addEntry(expense);
					}
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}
	}
	
	public void addEntry(ExpenseStub expense){
		this.add(new Entry(expense));
	}
	
	@Override
	protected void onAttach() {
		clearSelection();
		super.onAttach();
	}
	
}
