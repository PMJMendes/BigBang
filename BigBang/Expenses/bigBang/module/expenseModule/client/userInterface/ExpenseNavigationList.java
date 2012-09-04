package bigBang.module.expenseModule.client.userInterface;

import bigBang.library.client.userInterface.DocumentNavigationList;

public class ExpenseNavigationList extends DocumentNavigationList{
	
	final String FILTER = "saude-";
	
	public ExpenseNavigationList(){
		super();
		this.showFilterField(false);
		this.showSearchField(false);
		this.textBoxFilter.setValue(FILTER);
		applyFilter(FILTER);
	}
	
	public void applyFilter(String filter){
		onFilterTextChanged(filter);
	}

	@Override
	protected void onSizeChanged() {
		this.applyFilter(FILTER);
	};
}
