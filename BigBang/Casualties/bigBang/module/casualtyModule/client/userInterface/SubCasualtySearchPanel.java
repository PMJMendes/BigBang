package bigBang.module.casualtyModule.client.userInterface;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBroker;
import bigBang.definitions.client.dataAccess.SubCasualtyDataBrokerClient;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.definitions.shared.SubCasualty;
import bigBang.definitions.shared.SubCasualtyStub;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.casualtyModule.client.userInterface.SubCasualtyList.Entry;
import bigBang.module.casualtyModule.shared.SubCasualtySearchParameter;
import bigBang.module.casualtyModule.shared.SubCasualtySortParameter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

public class SubCasualtySearchPanel extends SearchPanel<SubCasualtyStub> implements SubCasualtyDataBrokerClient{

	public static enum Filters {
		CASUALTY_DATE,
		CASUALTY_CLIENT
	}
	protected FiltersPanel filtersPanel;

	private HashMap<String, SubCasualtyStub> subCasualtiesToUpdate;
	private HashMap<String, Void> subCasualtiesToRemove;
	private int subCasualtyDataVersion;

	public SubCasualtySearchPanel() {
		super(((SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY)).getSearchBroker());
		subCasualtiesToUpdate = new HashMap<String, SubCasualtyStub>();
		subCasualtiesToRemove = new HashMap<String, Void>();

		Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
		sortOptions.put(SubCasualtySortParameter.SortableField.RELEVANCE, "Relevância");
		sortOptions.put(SubCasualtySortParameter.SortableField.NUMBER, "Número");

		filtersPanel = new FiltersPanel(sortOptions);
		filtersPanel.addDateField(Filters.CASUALTY_DATE, "Data do Sinistro");
		filtersPanel.addMutableListField(Filters.CASUALTY_CLIENT, BigBangConstants.EntityIds.CLIENT, "Segurado");

		SubCasualtyDataBroker broker = (SubCasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.SUB_CASUALTY);
		broker.registerClient(this);

		filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doSearch(false);
			}
		});

		filtersContainer.clear();
		filtersContainer.add(filtersPanel);
	}

	public SubCasualtySearchPanel(SearchDataBroker<SubCasualtyStub> broker) {
		super(broker);
	}

	@Override
	public void doSearch(boolean keepState) {
		if(this.workspaceId != null){
			this.broker.disposeSearch(this.workspaceId);
			this.workspaceId = null;
		}

		subCasualtiesToRemove.clear();
		subCasualtiesToUpdate.clear();

		SubCasualtySearchParameter parameter = new SubCasualtySearchParameter();
		parameter.freeText = this.textBoxFilter.getValue();
		Date casDate = (Date) filtersPanel.getFilterValue(Filters.CASUALTY_DATE);
		parameter.casualtyDate = casDate == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(casDate);
		parameter.clientId = (String) filtersPanel.getFilterValue(Filters.CASUALTY_CLIENT);

		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		SubCasualtySortParameter sort = new SubCasualtySortParameter((SubCasualtySortParameter.SortableField) filtersPanel.getSelectedSortableField(),
				filtersPanel.getSortingOrder());

		SortParameter[] sorts = new SortParameter[]{
				sort
		};

		doSearch(parameters, sorts, keepState);		
	}

	@Override
	public void onResults(Collection<SubCasualtyStub> results) {
		for(SubCasualtyStub s : results){
			if(!subCasualtiesToRemove.containsKey(s.id)){
				if(subCasualtiesToUpdate.containsKey(s.id)){
					s = subCasualtiesToUpdate.get(s.id);
				}
				addSearchResult(s);
			}
		}		
	}

	protected Entry addSearchResult(SearchResult r){
		Entry entry = null;
		if(r instanceof SubCasualtyStub){
			entry = new Entry((SubCasualtyStub)r);
			add(entry);
		}
		return entry;
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
			this.subCasualtyDataVersion = number;
		}		
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.SUB_CASUALTY)){
			return subCasualtyDataVersion;
		}
		return -1;	
	}

	@Override
	public void addSubCasualty(SubCasualty subCasualty) {
		this.add(0, new Entry(subCasualty));
		
	}

	@Override
	public void updateSubCasualty(SubCasualty subCasualty) {
		for(ValueSelectable<SubCasualtyStub> s : this){
			SubCasualtyStub stub = s.getValue();
			if(subCasualty.id.equalsIgnoreCase(stub.id)){
				s.setValue(subCasualty);
				return;
			}
		}
		this.subCasualtiesToUpdate.put(subCasualty.id, subCasualty);		
	}

	@Override
	public void removeSubCasualty(String id) {
		for(ValueSelectable<SubCasualtyStub> s : this){
			SubCasualtyStub stub = s.getValue();
			if(id.equalsIgnoreCase(stub.id)){
				remove(s);
				return;
			}
		}
		this.subCasualtiesToRemove.put(id, null);		
	}

	public void setOwner(String ownerId) {
		SubCasualtySearchParameter parameter = new SubCasualtySearchParameter();
		parameter.ownerId = ownerId;
		
		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};
		
		doSearch(parameters, null, false);
	}



}
