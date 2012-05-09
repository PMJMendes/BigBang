package bigBang.module.clientModule.client.userInterface;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.dataAccess.ClientProcessDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.clientModule.shared.ClientSearchParameter;
import bigBang.module.clientModule.shared.ClientSortParameter;
import bigBang.module.clientModule.shared.ClientSortParameter.SortableField;
import bigBang.module.clientModule.shared.ModuleConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * @author Premium-Minds (Francisco Cabrita)
 *
 * A SearchPanel for clients
 */
public class ClientSearchPanel extends SearchPanel<ClientStub> implements ClientProcessDataBrokerClient {

	public static enum Filters {
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

	protected Map<String, ClientStub> clientsToUpdate;
	protected Map<String, Void> clientsToRemove;

	public ClientSearchPanel(){
		super(((ClientProcessBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT)).getSearchBroker());
		
		clientsToUpdate = new HashMap<String, ClientStub>();
		clientsToRemove = new HashMap<String, Void>();

		Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
		sortOptions.put(ClientSortParameter.SortableField.RELEVANCE, "Relevância");
		sortOptions.put(ClientSortParameter.SortableField.NAME, "Nome");
		sortOptions.put(ClientSortParameter.SortableField.GROUP, "Grupo");
		sortOptions.put(ClientSortParameter.SortableField.NUMBER, "Número");

		filtersPanel = new FiltersPanel(sortOptions);

		filtersPanel.addTypifiedListField(Filters.COST_CENTER, BigBangConstants.EntityIds.COST_CENTER, "Centro de Custo");
		filtersPanel.addTypifiedListField(Filters.MANAGER, BigBangConstants.EntityIds.USER, "Gestor");
		filtersPanel.addTypifiedListField(Filters.MEDIATOR, BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		filtersPanel.addTypifiedListField(Filters.OPERATIONAL_PROFILE, ModuleConstants.ListIDs.OperationalProfiles, "Perfil Op.");

		filtersPanel.addTypifiedListField(Filters.COMPANY_SIZE, ModuleConstants.ListIDs.CompanySizes, "Num. de Trab.");
		filtersPanel.addTypifiedListField(Filters.SALES_VOLUME, BigBangConstants.TypifiedListIds.SALES_VOLUMES, "Vol. de Vendas");

		filtersPanel.addTypifiedListField(Filters.PROFESSION, ModuleConstants.ListIDs.Professions, "Profissão");
		filtersPanel.addTypifiedListField(Filters.MARITAL_STATUS, ModuleConstants.ListIDs.MaritalStatuses, "Estado Civil");
		filtersPanel.addDateField(Filters.BORN_AFTER, "Nascido De");
		filtersPanel.addDateField(Filters.BORN_BEFORE, "Nascido Até");

		filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doSearch();
			}
		});

		filtersContainer.clear();
		filtersContainer.add(filtersPanel);
		
		((ClientProcessBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CLIENT)).registerClient(this);
	}

	@Override
	public void onResults(Collection<ClientStub> results) {
		for(ClientStub s : results){
			if(!this.clientsToRemove.containsKey(s.id)){
				if(this.clientsToUpdate.containsKey(s.id)){
					s = this.clientsToUpdate.get(s.id);
				}
				addSearchResult(s);
			}
		}
	}

	/**
	 * Adds an entry in the list for a given search result.
	 * The entries' presentation is different for clients or client groups
	 * @param r The search result to be added to the list
	 */
	protected ClientSearchPanelListEntry addSearchResult(SearchResult r){
		ClientSearchPanelListEntry entry = null;
		if(r instanceof ClientStub){
			entry = new ClientSearchPanelListEntry((ClientStub) r);
			add(entry);
		}
		return entry;
	}

	@Override
	public void doSearch() {
		if(this.workspaceId != null){
			this.broker.disposeSearch(this.workspaceId);
			this.workspaceId = null;
		}
		
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
		ClientStub newClient = client;
		if(!this.clientsToRemove.containsKey(client.id)){
			if(this.clientsToUpdate.containsKey(client.id)){
				newClient = this.clientsToUpdate.get(client.id);
			}
			ClientSearchPanelListEntry entry = new ClientSearchPanelListEntry(newClient);
			add(0, entry);
		}
	}

	@Override
	public void updateClient(Client client) {
		for(ValueSelectable<ClientStub> vs : this) {
			if(vs.getValue().id.equalsIgnoreCase(client.id)){
				vs.setValue(client);
				return;
			}
		}
		this.clientsToUpdate.put(client.id, client);
	}

	@Override
	public void removeClient(String clientId) {
		for(ValueSelectable<ClientStub> vs : this) {
			if(vs.getValue().id.equals(clientId)){
				remove(vs);
				return;
			}
		}
		this.clientsToRemove.put(clientId, null);
	}

}
