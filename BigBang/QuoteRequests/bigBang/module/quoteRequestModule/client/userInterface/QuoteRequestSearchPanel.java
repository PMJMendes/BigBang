package bigBang.module.quoteRequestModule.client.userInterface;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.client.dataAccess.QuoteRequestDataBrokerClient;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SearchResult;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.view.SearchPanel;
import bigBang.module.quoteRequestModule.client.resources.Resources;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSearchParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter.SortableField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class QuoteRequestSearchPanel extends SearchPanel<QuoteRequestStub> implements QuoteRequestDataBrokerClient {

	/**
	 * An entry in the search panel
	 */
	public static class Entry extends ListEntry<QuoteRequestStub>{
		protected Label numberLabel;
		protected Label clientLabel;
		protected Label lineLabel;
		protected Image statusIcon;
		
		public Entry(QuoteRequestStub quoteRequest){
			super(quoteRequest);
			setHeight("50px");
			this.titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		}

		public <I extends Object> void setInfo(I info) {
			QuoteRequestStub value = (QuoteRequestStub)info;
			if(value.id != null){
				if(numberLabel == null) {
					numberLabel = getFormatedLabel();
					numberLabel.getElement().getStyle().setFontSize(14, Unit.PX);
					numberLabel.setWordWrap(false);
					clientLabel = getFormatedLabel();
					clientLabel.getElement().getStyle().setFontSize(11, Unit.PX);
					clientLabel.getElement().getStyle().setProperty("whiteSpace", "");
					clientLabel.setHeight("1.2em");
					lineLabel = getFormatedLabel();
					lineLabel.getElement().getStyle().setFontSize(11, Unit.PX);
					VerticalPanel container = new VerticalPanel();
					container.setSize("100%", "100%");
					
					container.add(numberLabel);
					container.add(lineLabel);
					container.add(clientLabel);
					
					setWidget(container);
					
					statusIcon = new Image();
					setRightWidget(statusIcon);
				}
				
				numberLabel.setText("#" + value.processNumber);
				clientLabel.setText("#" + value.clientNumber + " - " + value.clientName);

				Resources resources = GWT.create(Resources.class);
				statusIcon.setResource(value.isOpen ? resources.activePolicyIcon() : resources.inactivePolicyIcon());
				
				return;
			}
		};

		@Override
		public void setSelected(boolean selected, boolean b) {
			super.setSelected(selected, b);
			if(this.clientLabel == null) {return;}
			if(selected){
				this.clientLabel.getElement().getStyle().setColor("white");
			}else{
				this.clientLabel.getElement().getStyle().setColor("gray");
			}
		}
	}
	
	protected static enum Filters {
		CATEGORY,
		LINE,
		SUBLINE,
		INSURANCE_AGENCY,
		MEDIATOR,
		MANAGER,
		CASE_STUDY
	}
	protected FiltersPanel filtersPanel;
	
	protected int dataVersion = 0;
	protected Map<String, QuoteRequestStub> quoteRequestsToUpdate;
	protected Set<String> quoteRequestsToRemove;
	
	public QuoteRequestSearchPanel() {
		super(((QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST)).getSearchBroker());
		quoteRequestsToUpdate = new HashMap<String, QuoteRequestStub>();
		quoteRequestsToRemove = new HashSet<String>();
		
		Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
		sortOptions.put(QuoteRequestSortParameter.SortableField.RELEVANCE, "Relevância");
		sortOptions.put(QuoteRequestSortParameter.SortableField.NUMBER, "Número");
		sortOptions.put(QuoteRequestSortParameter.SortableField.CLIENT_NUMBER, "Número de Cliente");
		sortOptions.put(QuoteRequestSortParameter.SortableField.CLIENT_NAME, "Nome de Cliente");

		filtersPanel = new FiltersPanel(sortOptions);
		filtersPanel.addTypifiedListField(Filters.MANAGER, BigBangConstants.EntityIds.USER, "Gestor de Consulta");
		filtersPanel.addTypifiedListField(Filters.INSURANCE_AGENCY, BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
		filtersPanel.addTypifiedListField(Filters.MEDIATOR, BigBangConstants.EntityIds.MEDIATOR, "Mediador");
		filtersPanel.addTypifiedListField(Filters.CATEGORY, BigBangConstants.EntityIds.CATEGORY, "Categoria");
		filtersPanel.addTypifiedListField(Filters.LINE, BigBangConstants.EntityIds.LINE, "Ramo", Filters.CATEGORY);
		filtersPanel.addTypifiedListField(Filters.SUBLINE, BigBangConstants.EntityIds.SUB_LINE, "Modalidade", Filters.LINE);
		filtersPanel.addCheckBoxField(Filters.CASE_STUDY, "Apenas Case Study");

		filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doSearch(false);
			}
		});

		filtersContainer.clear();
		filtersContainer.add(filtersPanel);

		QuoteRequestBroker broker = (QuoteRequestBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.QUOTE_REQUEST);
		broker.registerClient(this);
	}

	@Override
	public void doSearch(boolean keepState) {
		if(this.workspaceId != null){
			this.broker.disposeSearch(this.workspaceId);
			this.workspaceId = null;
		}
		
		this.quoteRequestsToRemove.clear();
		this.quoteRequestsToUpdate.clear();

		QuoteRequestSearchParameter parameter = new QuoteRequestSearchParameter();
		parameter.freeText = this.textBoxFilter.getValue();
		parameter.managerId = (String) filtersPanel.getFilterValue(Filters.MANAGER);
		parameter.mediatorId = (String) filtersPanel.getFilterValue(Filters.MEDIATOR);
		parameter.insuranceAgencyId = (String) filtersPanel.getFilterValue(Filters.INSURANCE_AGENCY);
		parameter.categoryId = (String) filtersPanel.getFilterValue(Filters.CATEGORY);
		parameter.lineId = (String) filtersPanel.getFilterValue(Filters.LINE);
		parameter.subLineId = (String) filtersPanel.getFilterValue(Filters.SUBLINE);
		boolean caseStudy = (Boolean) filtersPanel.getFilterValue(Filters.CASE_STUDY);
		parameter.caseStudy = caseStudy ? true : null;

		SearchParameter[] parameters = new SearchParameter[]{
				parameter
		};

		QuoteRequestSortParameter sort = new QuoteRequestSortParameter((SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder());

		SortParameter[] sorts = new SortParameter[]{
				sort
		};

		doSearch(parameters, sorts, keepState);
	}

	@Override
	public void onResults(Collection<QuoteRequestStub> results) {
		for(QuoteRequestStub s : results){
			if(!quoteRequestsToRemove.contains(s.id)){
				if(quoteRequestsToUpdate.containsKey(s.id)){
					s = quoteRequestsToUpdate.get(s.id);
				}
				addSearchResult(s);
			}
		}
	}

	protected Entry addSearchResult(SearchResult r){
		Entry entry = null;
		if(r instanceof QuoteRequestStub){
			entry = new Entry((QuoteRequestStub)r);
			add(entry);
		}
		return entry;
	}
	
	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.QUOTE_REQUEST)) {
			this.dataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.QUOTE_REQUEST)){
			return this.dataVersion;
		}
		return -1;
	}

	@Override
	public void addQuoteRequest(QuoteRequest request) {
		this.add(0, new Entry(request));
	}

	@Override
	public void updateQuoteRequest(QuoteRequest quoteRequest) {
		for(ValueSelectable<QuoteRequestStub> s : this) {
			QuoteRequestStub quoteRequestStub = s.getValue();
			if(quoteRequest.id.equalsIgnoreCase(quoteRequestStub.id)){
				s.setValue(quoteRequest);
				return;
			}
		}
		this.quoteRequestsToUpdate.put(quoteRequest.id, quoteRequest);
	}

	@Override
	public void removeQuoteRequest(String id) {
		for(ValueSelectable<QuoteRequestStub> s : this) {
			QuoteRequestStub quoteRequestStub = s.getValue();
			if(id.equalsIgnoreCase(quoteRequestStub.id)){
				remove(s);
				return;
			}
		}
		this.quoteRequestsToRemove.add(id);
	}
	
	@Override
	public void remapItemId(String oldId, String newId) {
		return;
	}

}
