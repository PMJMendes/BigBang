package bigBang.module.expenseModule.client.userInterface;

import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.library.shared.SearchResult;
import bigBang.module.expenseModule.interfaces.ExpenseService;

public class ExpenseSearchPanel extends SearchPanel {

	public ExpenseSearchPanel() {
		super(ExpenseService.Util.getInstance());
	}
	
	@Override
	public void doSearch() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResults(SearchResult[] results) {
		// TODO Auto-generated method stub

	}

}
