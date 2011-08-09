package bigBang.module.receiptModule.client.userInterface;

import bigBang.definitions.shared.SearchResult;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.receiptModule.interfaces.ReceiptService;

public class ReceiptSearchPanel extends SearchPanel {

	public ReceiptSearchPanel() {
		super(ReceiptService.Util.getInstance());
		// TODO Auto-generated constructor stub
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
