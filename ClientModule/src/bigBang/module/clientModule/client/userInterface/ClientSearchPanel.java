package bigBang.module.clientModule.client.userInterface;

import bigBang.library.client.SearchResult;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.clientModule.shared.ClientProxy;

public class ClientSearchPanel extends SearchPanel<ClientProxy> {

	public ClientSearchPanel(){
		super();
	}
	
	@Override
	public void onResults(SearchResult[] results) {
		for(int i = 0; i < results.length; i++){
			ClientSearchPanelListEntry entry = new ClientSearchPanelListEntry(null);
			entry.setTitle("Premium Minds Lda.");
			entry.setText("nº 132356497");
			add(entry);
		}
	}
	
	
}
