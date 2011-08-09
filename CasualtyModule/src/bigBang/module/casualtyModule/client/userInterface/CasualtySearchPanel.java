package bigBang.module.casualtyModule.client.userInterface;

import bigBang.definitions.shared.SearchResult;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.casualtyModule.interfaces.CasualtyService;

public class CasualtySearchPanel extends SearchPanel {

	public CasualtySearchPanel() {
		super(CasualtyService.Util.getInstance());
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
