package bigBang.module.insurancePolicyModule.client.userInterface;

import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.library.shared.SearchResult;
import bigBang.module.insurancePolicyModule.interfaces.InsurancePolicyService;

public class InsurancePolicySearchPanel extends SearchPanel {

	public InsurancePolicySearchPanel() {
		super(InsurancePolicyService.Util.getInstance());
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
