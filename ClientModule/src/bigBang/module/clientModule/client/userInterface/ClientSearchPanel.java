package bigBang.module.clientModule.client.userInterface;

import bigBang.library.shared.SearchResult;
import bigBang.library.shared.userInterface.view.SearchPanel;

public class ClientSearchPanel extends SearchPanel {

	public ClientSearchPanel(){
		super();
		showFilters(true);
	}
	
	@Override
	protected void renderResults(SearchResult[] results) {
		for(int i = 0; i < results.length; i++){
			ClientSearchPanelListEntry entry = new ClientSearchPanelListEntry(i+"");
			entry.setTitle("Premium Minds Lda.");
			entry.setText("nº 132356497");
			addListEntry(entry);
		}
	}
	
	
}
