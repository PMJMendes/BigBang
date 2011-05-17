package bigBang.module.clientModule.client.userInterface;

import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.library.shared.SearchResult;
import bigBang.module.clientModule.shared.ClientGroupStub;
import bigBang.module.clientModule.shared.ClientStub;

public class ClientSearchPanel extends SearchPanel {

	public ClientSearchPanel(){
		super();
	}

	@Override
	public void onResults(SearchResult[] results) {
		for(int i = 0; i < results.length; i++){
			SearchResult r = results[i];

			//If Client
			if(r instanceof ClientStub){
				ClientSearchPanelListEntry entry = new ClientSearchPanelListEntry((ClientStub) results[i]);
				//add(entry);
			}else{
				//If Client Group
				if(r instanceof ClientGroupStub) {
					//ClientGroupSearchPanelListEntry entry = new ClientGroupSearchPanelListEntry((ClientGroupStub) results[i]);
					//add(entry);
				}
			}


		}
	}

	@Override
	public void doSearch() {
		// TODO Auto-generated method stub
		
	}


}
