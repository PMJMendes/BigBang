package bigBang.module.receiptModule.client.userInterface;

import bigBang.library.client.userInterface.DocumentNavigationList;

public class ReceiptNavigationList extends DocumentNavigationList{
	
	final String FILTER = "recibos-";
	
	public ReceiptNavigationList(){
		super();
		this.showFilterField(false);
		this.showSearchField(false);
		this.textBoxFilter.setValue(FILTER);
		applyFilter(FILTER);
	}
	
	public void applyFilter(String filter){
		onFilterTextChanged(FILTER);
	}
	
	@Override
	protected void onSizeChanged() {
		this.applyFilter(FILTER);
	};
}
