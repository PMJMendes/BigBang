package bigBang.module.clientModule.client.userInterface;

import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.ValueWrapper;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SearchResult;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.shared.Client;
import bigBang.module.clientModule.shared.ClientSearchParameter;
import bigBang.module.clientModule.shared.ClientStub;

/**
 * @author Premium-Minds (Francisco Cabrita)
 *
 * A SearchPanel for clients and client groups
 */
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
	
	/**
	 * Adds an entry in the list for a given search result.
	 * The entries' presentation is different for clients or client groups
	 * @param r The search result to be added to the list
	 */
	protected void addSearchResult(SearchResult r){
		if(r instanceof ClientStub){
			ClientSearchPanelListEntry entry = new ClientSearchPanelListEntry(new ValueWrapper<ClientStub>((ClientStub) r));
			add(entry);
		}
	}

	@Override
	public void doSearch() {
		SearchParameter[] parameters = new SearchParameter[1];
		ClientSearchParameter p = new ClientSearchParameter();
		
		p.freeText = this.getFreeText();
		
		parameters[0] = p;
		doSearch(parameters);
	}

	@Override
	protected void selectionChangedEventFireBypass(final SelectionChangedEvent e) {
		//TODO FJVC IMPORTANT
		for(Selectable s : e.getSelected()){
//			@SuppressWarnings("unchecked")
//			ValueSelectable<ValueWrapper<?>> vs = (ValueSelectable<ValueWrapper<?>>)s;
//			ValueWrapper<?> result = vs.getValue();
//			if(result.getValue() instanceof ClientStub){
//				@SuppressWarnings("unchecked")
//				final ValueWrapper<ClientStub> clientWrapper = (ValueWrapper<ClientStub>) result;
//				ClientService.Util.getInstance().getClient(clientWrapper.getValue().id, new BigBangAsyncCallback<Client>() {
//
//					@Override
//					public void onSuccess(Client result) {
//						clientWrapper.setValue(result, true);
//						fireEvent(e);
//					}
//				});
//			}
		}
	}

}
