package bigBang.module.riskAnalisysModule.client.userInterface;

import bigBang.definitions.shared.SearchResult;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.riskAnalisysModule.interfaces.RiskAnalisysService;

public class RiskAnalisysSearchPanel extends SearchPanel {

	public RiskAnalisysSearchPanel() {
		super(RiskAnalisysService.Util.getInstance());
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
