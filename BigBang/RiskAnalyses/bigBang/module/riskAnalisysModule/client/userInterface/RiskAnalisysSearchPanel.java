package bigBang.module.riskAnalisysModule.client.userInterface;

import java.util.Collection;

import bigBang.definitions.shared.RiskAnalysisStub;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;

public class RiskAnalisysSearchPanel extends SearchPanel<RiskAnalysisStub> {

	public static class Entry extends ListEntry<RiskAnalysisStub> {

		public Entry(RiskAnalysisStub value) {
			super(value);
			// TODO Auto-generated constructor stub
		}	
		
	}
	public RiskAnalisysSearchPanel() {
		super(null);
	}

	@Override
	public void doSearch(boolean keepState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResults(Collection<RiskAnalysisStub> results) {
		// TODO Auto-generated method stub
		
	}

}
