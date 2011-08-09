package bigBang.module.clientModule.client.userInterface;

import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.ValueWrapper;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SortOrder;
import bigBang.library.shared.SortParameter;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.shared.ClientSearchParameter;
import bigBang.module.clientModule.shared.ClientSortParameter;

/**
 * @author Premium-Minds (Francisco Cabrita)
 *
 * A SearchPanel for clients and client groups
 */
public class ClientSearchPanel extends SearchPanel implements ClientProcessDataBrokerClient {
	
	protected int clientDataVersionNumber;
	
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
		
		SortParameter[] sorts = new SortParameter[]{
				new ClientSortParameter(ClientSortParameter.SortableField.RELEVANCE, SortOrder.DESC)
		};
		
		doSearch(parameters, sorts);
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		this.clientDataVersionNumber = number;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return clientDataVersionNumber;
	}

	@Override
	public void addClient(Client client) {
		addSearchResult(client);
	}

	@Override
	public void updateClient(Client client) {
		for(ValueSelectable<SearchResult> vs : this) {
			if(vs.getValue().id.equals(client.id)){
				vs.setValue(client);
				break;
			}
		}
	}

	@Override
	public void removeClient(String clientId) {
		for(ValueSelectable<SearchResult> vs : this) {
			if(vs.getValue().id.equals(clientId)){
				remove(vs);
				break;
			}
		}
	}

}
