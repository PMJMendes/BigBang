package bigBang.module.clientModule.client.userInterface;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.i18n.client.DateTimeFormat;

import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.SearchResult;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.ValueWrapper;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.library.shared.SearchParameter;
import bigBang.library.shared.SortParameter;
import bigBang.module.clientModule.interfaces.ClientService;
import bigBang.module.clientModule.shared.ClientSearchParameter;
import bigBang.module.clientModule.shared.ClientSortParameter;
import bigBang.module.clientModule.shared.ClientSortParameter.SortableField;
import bigBang.module.clientModule.shared.ModuleConstants;

/**
 * @author Premium-Minds (Francisco Cabrita)
 *
 * A SearchPanel for clients
 */
public class ClientSearchPanel extends SearchPanel implements ClientProcessDataBrokerClient {

	protected static enum Filters {
		MANAGER,
		COST_CENTER,
		MEDIATOR,
		PROFESSION,
		OPERATIONAL_PROFILE,
		COMPANY_SIZE,
		SALES_VOLUME,
		MARITAL_STATUS,
		BORN_AFTER,
		BORN_BEFORE
	} 
	
	protected int clientDataVersionNumber;
	protected FiltersPanel filtersPanel;
	
	
	public ClientSearchPanel(){
		super(ClientService.Util.getInstance());
		
		Map<Enum<?>, String> sortOptions = new HashMap<Enum<?>, String>(); 
		sortOptions.put(ClientSortParameter.SortableField.RELEVANCE, "Relevância");
		sortOptions.put(ClientSortParameter.SortableField.NAME, "Nome");
		sortOptions.put(ClientSortParameter.SortableField.NUMBER, "Número");
		sortOptions.put(ClientSortParameter.SortableField.GROUP, "Grupo");
		
		filtersPanel = new FiltersPanel(sortOptions);
		filtersPanel.addTypifiedListField(Filters.MANAGER, BigBangConstants.EntityIds.USER, "Gestor");
		filtersPanel.addTypifiedListField(Filters.COST_CENTER, BigBangConstants.EntityIds.COST_CENTER, "Centro de Custo");
		filtersPanel.addTypifiedListField(Filters.MEDIATOR, BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		
		filtersPanel.addTypifiedListField(Filters.PROFESSION, ModuleConstants.ListIDs.Professions, "Profissão");
		filtersPanel.addTypifiedListField(Filters.OPERATIONAL_PROFILE, ModuleConstants.ListIDs.OperationalProfiles, "Perfil Op.");
		filtersPanel.addTypifiedListField(Filters.COMPANY_SIZE, ModuleConstants.ListIDs.CompanySizes, "Num. de Trab.");
		filtersPanel.addTypifiedListField(Filters.SALES_VOLUME, ModuleConstants.ListIDs.SalesVolumes, "Vol. de Vendas");
		
		filtersPanel.addTypifiedListField(Filters.MARITAL_STATUS, ModuleConstants.ListIDs.MaritalStatuses, "Estado Civil");
		filtersPanel.addDateField(Filters.BORN_AFTER, "Nascido De");
		filtersPanel.addDateField(Filters.BORN_BEFORE, "Nascido Até");
		
		filtersContainer.clear();
		filtersContainer.add(filtersPanel);
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

		p.managerId = (String) filtersPanel.getFilterValue(Filters.MANAGER);
		p.costCenterId = (String) filtersPanel.getFilterValue(Filters.COST_CENTER);
		p.mediatorId = (String) filtersPanel.getFilterValue(Filters.MEDIATOR);
		String profession = (String) filtersPanel.getFilterValue(Filters.PROFESSION);
		p.professionIds = profession == null ? new String[0] : new String[]{profession}; //TODO FILTERS FJVC
		p.opProfileId = (String) filtersPanel.getFilterValue(Filters.OPERATIONAL_PROFILE);
		p.workerSizeId = (String) filtersPanel.getFilterValue(Filters.COMPANY_SIZE);
		p.salesVolumeId = (String) filtersPanel.getFilterValue(Filters.SALES_VOLUME);
		p.maritalStatusId = (String) filtersPanel.getFilterValue(Filters.MARITAL_STATUS);
		Date bornAfter = (Date) filtersPanel.getFilterValue(Filters.BORN_AFTER);
		p.birthDateFrom = bornAfter == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(bornAfter);
		Date bornBefore = (Date) filtersPanel.getFilterValue(Filters.BORN_BEFORE);
		p.birthDateTo = bornBefore == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(bornBefore);
		
		parameters[0] = p;
		
		SortParameter[] sorts = new SortParameter[]{
			new ClientSortParameter((SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder())
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
