package bigBang.module.clientModule.client.userInterface;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.ValueWrapper;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.library.shared.SearchResult;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.shared.Client;
import bigBang.module.clientModule.shared.ClientGroupStub;
import bigBang.module.clientModule.shared.ClientStub;

public class ClientSearchPanel extends SearchPanel {

	public ClientSearchPanel(){
		super(ClientService.Util.getInstance());
	}

	@Override
	public void onResults(SearchResult[] results) {
		for(int i = 0; i < results.length; i++){
			SearchResult r = results[i];
			addSearchResult(r);
		}
	}
	
	protected void addSearchResult(SearchResult r){
		//If Client
		if(r instanceof ClientStub){
			ClientSearchPanelListEntry entry = new ClientSearchPanelListEntry(new ValueWrapper<ClientStub>((ClientStub) r));
			add(entry);
		}else{
			//If Client Group
			if(r instanceof ClientGroupStub) {
				ClientGroupSearchPanelListEntry entry = new ClientGroupSearchPanelListEntry(new ValueWrapper<ClientGroupStub>((ClientGroupStub) r));
				add(entry);
			}
		}
	}

	@Override
	public void doSearch() {
		doSearch(null); //TODO
	}

	@Override
	protected void selectionChangedEventFireBypass(SelectionChangedEvent e) {
		for(Selectable s : e.getSelected()){
			@SuppressWarnings("unchecked")
			ValueSelectable<SearchResult> vs = (ValueSelectable<SearchResult>)s;
			SearchResult result = vs.getValue();
			if(result instanceof ClientStub){
				ClientStub client = (ClientStub) result;
				ClientService.Util.getInstance().getClient(client.id, new BigBangAsyncCallback<Client>() {

					@Override
					public void onSuccess(Client result) {
						
					}
				});
			} else if(result instanceof ClientGroupStub) {
				
			}
		}
		
		
	}

}
