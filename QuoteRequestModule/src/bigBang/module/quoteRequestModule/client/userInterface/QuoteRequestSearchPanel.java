package bigBang.module.quoteRequestModule.client.userInterface;

import bigBang.definitions.shared.SearchResult;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.quoteRequestModule.interfaces.QuoteRequestService;

public class QuoteRequestSearchPanel extends SearchPanel {

	public QuoteRequestSearchPanel() {
		super(QuoteRequestService.Util.getInstance());
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
