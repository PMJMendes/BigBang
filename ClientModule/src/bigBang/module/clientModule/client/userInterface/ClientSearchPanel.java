package bigBang.module.clientModule.client.userInterface;

import bigBang.library.client.SearchResult;
import bigBang.library.client.userInterface.view.SearchPanel;

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
