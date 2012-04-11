package bigBang.module.casualtyModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.dataAccess.CasualtyDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.CasualtyStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.casualtyModule.shared.CasualtySearchParameter;
import bigBang.module.casualtyModule.shared.CasualtySortParameter;
import bigBang.module.casualtyModule.shared.CasualtySortParameter.SortableField;

public class CasualtySearchPanel extends SearchPanel<CasualtyStub> implements CasualtyDataBrokerClient {

	public static enum Filters {
	} 

	protected int casualtyDataVersionNumber;
	protected FiltersPanel filtersPanel;

	protected Map<String, CasualtyStub> casualtiesToUpdate;
	protected Map<String, Void> casualtiesToRemove;

	public CasualtySearchPanel(){
		super(((CasualtyDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CASUALTY)).getSearchBroker());
		
		casualtiesToUpdate = new HashMap<String, CasualtyStub>();
		casualtiesToRemove = new HashMap<String, Void>();

		Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
		sortOptions.put(CasualtySortParameter.SortableField.RELEVANCE, "Relevância");
		sortOptions.put(CasualtySortParameter.SortableField.NUMBER, "Número");
		sortOptions.put(CasualtySortParameter.SortableField.DATE, "Data");
		sortOptions.put(CasualtySortParameter.SortableField.MANAGER, "Gestor");
		sortOptions.put(CasualtySortParameter.SortableField.CLIENT_NUMBER, "Número de Cliente");
		sortOptions.put(CasualtySortParameter.SortableField.CLIENT_NAME, "Nome de Cliente");
		
		filtersPanel = new FiltersPanel(sortOptions);
//		filtersPanel.addTypifiedListField(Filters.MANAGER, BigBangConstants.EntityIds.USER, "Gestor");
//		filtersPanel.addTypifiedListField(Filters.COST_CENTER, BigBangConstants.EntityIds.COST_CENTER, "Centro de Custo");
//		filtersPanel.addTypifiedListField(Filters.MEDIATOR, BigBangConstants.EntityIds.MEDIATOR, "Mediador");
//
//		filtersPanel.addTypifiedListField(Filters.PROFESSION, ModuleConstants.ListIDs.Professions, "Profissão");
//		filtersPanel.addTypifiedListField(Filters.OPERATIONAL_PROFILE, ModuleConstants.ListIDs.OperationalProfiles, "Perfil Op.");
//		filtersPanel.addTypifiedListField(Filters.COMPANY_SIZE, ModuleConstants.ListIDs.CompanySizes, "Num. de Trab.");
//		filtersPanel.addTypifiedListField(Filters.SALES_VOLUME, BigBangConstants.TypifiedListIds.SALES_VOLUMES, "Vol. de Vendas");
//
//		filtersPanel.addTypifiedListField(Filters.MARITAL_STATUS, ModuleConstants.ListIDs.MaritalStatuses, "Estado Civil");
//		filtersPanel.addDateField(Filters.BORN_AFTER, "Nascido De");
//		filtersPanel.addDateField(Filters.BORN_BEFORE, "Nascido Até");

		filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doSearch();
			}
		});

		filtersContainer.clear();
		filtersContainer.add(filtersPanel);
		
		((CasualtyDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CASUALTY)).registerClient(this);
	}

	@Override
	public void onResults(Collection<CasualtyStub> results) {
		for(CasualtyStub s : results){
			if(!this.casualtiesToRemove.containsKey(s.id)){
				if(this.casualtiesToUpdate.containsKey(s.id)){
					s = this.casualtiesToUpdate.get(s.id);
				}
				addSearchResult(s);
			}
		}
	}

	protected CasualtySearchPanelListEntry addSearchResult(SearchResult r){
		CasualtySearchPanelListEntry entry = null;
		if(r instanceof CasualtyStub){
			entry = new CasualtySearchPanelListEntry((CasualtyStub) r);
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
		CasualtySearchParameter p = new CasualtySearchParameter();

		p.freeText = this.getFreeText();

//		p.managerId = (String) filtersPanel.getFilterValue(Filters.MANAGER);
//		p.costCenterId = (String) filtersPanel.getFilterValue(Filters.COST_CENTER);
//		p.mediatorId = (String) filtersPanel.getFilterValue(Filters.MEDIATOR);
//		String profession = (String) filtersPanel.getFilterValue(Filters.PROFESSION);
//		p.professionIds = profession == null ? new String[0] : new String[]{profession}; //TODO FILTERS FJVC
//		p.opProfileId = (String) filtersPanel.getFilterValue(Filters.OPERATIONAL_PROFILE);
//		p.workerSizeId = (String) filtersPanel.getFilterValue(Filters.COMPANY_SIZE);
//		p.salesVolumeId = (String) filtersPanel.getFilterValue(Filters.SALES_VOLUME);
//		p.maritalStatusId = (String) filtersPanel.getFilterValue(Filters.MARITAL_STATUS);
//		Date bornAfter = (Date) filtersPanel.getFilterValue(Filters.BORN_AFTER);
//		p.birthDateFrom = bornAfter == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(bornAfter);
//		Date bornBefore = (Date) filtersPanel.getFilterValue(Filters.BORN_BEFORE);
//		p.birthDateTo = bornBefore == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(bornBefore);

		parameters[0] = p;

		SortParameter[] sorts = new SortParameter[]{
				new CasualtySortParameter((SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder())
		};

		doSearch(parameters, sorts);
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		this.casualtyDataVersionNumber = number;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return casualtyDataVersionNumber;
	}

	@Override
	public void addCasualty(Casualty casualty) {
		CasualtyStub newCasualty = casualty;
		if(!this.casualtiesToRemove.containsKey(casualty.id)){
			if(this.casualtiesToUpdate.containsKey(casualty.id)){
				newCasualty = this.casualtiesToUpdate.get(casualty.id);
			}
			CasualtySearchPanelListEntry entry = new CasualtySearchPanelListEntry(newCasualty);
			add(0, entry);
		}
	}

	@Override
	public void updateCasualty(Casualty casualty) {
		for(ValueSelectable<CasualtyStub> vs : this) {
			if(vs.getValue().id.equalsIgnoreCase(casualty.id)){
				vs.setValue(casualty);
				return;
			}
		}
		this.casualtiesToUpdate.put(casualty.id, casualty);
	}

	@Override
	public void removeCasualty(String casualtyId) {
		for(ValueSelectable<CasualtyStub> vs : this) {
			if(vs.getValue().id.equals(casualtyId)){
				remove(vs);
				return;
			}
		}
		this.casualtiesToRemove.put(casualtyId, null);
	}

}
