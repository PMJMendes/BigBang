package bigBang.module.quoteRequestModule.client.userInterface.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import bigBang.definitions.client.dataAccess.QuoteRequestBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.QuoteRequest;
import bigBang.definitions.shared.QuoteRequestStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortParameter;
import bigBang.library.client.HasCheckables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.userInterface.CheckableSearchPanel;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.SelectedProcessesList;
import bigBang.library.client.userInterface.view.MassManagerTransferView;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestForm;
import bigBang.module.quoteRequestModule.client.userInterface.QuoteRequestSearchPanel;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSearchParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter;
import bigBang.module.quoteRequestModule.shared.QuoteRequestSortParameter.SortableField;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class QuoteRequestMassManagerTransferView extends MassManagerTransferView<QuoteRequestStub, QuoteRequest> {

	protected static class CheckableQuoteRequestsSearchPanel extends CheckableSearchPanel<QuoteRequestStub> {

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
		
		public CheckableQuoteRequestsSearchPanel() {
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
					doSearch();
				}
			});

			filtersContainer.clear();
			filtersContainer.add(filtersPanel);
			
			setOperationId(BigBangConstants.OperationIds.QuoteRequestProcess.CREATE_MANAGER_TRANSFER);
		}

		@Override
		public ListEntry<QuoteRequestStub> addEntry(QuoteRequestStub value) {
			ListEntry<QuoteRequestStub> entry = new QuoteRequestSearchPanel.Entry(value);
			add(0, entry);
			return entry;
		}

		@Override
		public void doSearch() {
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

			doSearch(parameters, sorts);
		}
	}

	protected static class SelectedQuoteRequestsList extends SelectedProcessesList<QuoteRequestStub> {

		@Override
		public ListEntry<QuoteRequestStub> addEntry(QuoteRequestStub value) {
			ListEntry<QuoteRequestStub> entry = new QuoteRequestSearchPanel.Entry(value);
			entry.setChecked(true);
			this.add(entry);
			return entry;
		}
	}

	public QuoteRequestMassManagerTransferView() {
		super(new QuoteRequestForm(), 
				new CheckableQuoteRequestsSearchPanel(), 
				new SelectedQuoteRequestsList());
	}

	@Override
	public void addProcessToTransfer(QuoteRequestStub value) {
		this.selectedList.addEntry(value);
		searchPanel.markForCheck(value.id);
	}

	@Override
	public void removeProcessFromTransfer(String id) {
		for(ValueSelectable<QuoteRequestStub> entry : this.selectedList){
			if(id.equalsIgnoreCase(entry.getValue().id)){
				this.selectedList.remove(entry);
				break;
			}
		}
		searchPanel.markForUncheck(id);
	}

	@Override
	public HasCheckables getCheckableSelectedList() {
		return this.selectedList;
	}

}
