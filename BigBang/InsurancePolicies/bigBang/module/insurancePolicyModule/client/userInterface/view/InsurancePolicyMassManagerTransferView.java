package bigBang.module.insurancePolicyModule.client.userInterface.view;

import java.util.Map;
import java.util.TreeMap;

import bigBang.definitions.client.dataAccess.InsurancePolicyBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Policy2;
import bigBang.definitions.shared.Policy2Stub;
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
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicyForm;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel;
import bigBang.module.insurancePolicyModule.client.userInterface.InsurancePolicySearchPanel.Filters;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySearchParameter;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySortParameter;
import bigBang.module.insurancePolicyModule.shared.InsurancePolicySortParameter.SortableField;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class InsurancePolicyMassManagerTransferView extends
		MassManagerTransferView<Policy2Stub, Policy2> {

	protected static class CheckablePoliciesSearchPanel extends CheckableSearchPanel<Policy2Stub> {

		public FiltersPanel filtersPanel;
		
		public CheckablePoliciesSearchPanel() {
			super(((InsurancePolicyBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.INSURANCE_POLICY)).getSearchBroker());

			Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
			sortOptions.put(InsurancePolicySortParameter.SortableField.RELEVANCE, "Relevância");
			sortOptions.put(InsurancePolicySortParameter.SortableField.NUMBER, "Número");
			sortOptions.put(InsurancePolicySortParameter.SortableField.CATEGORY_LINE_SUBLINE, "Categoria/Ramo/Modalidade");
			sortOptions.put(InsurancePolicySortParameter.SortableField.CLIENT_NUMBER, "Número de Cliente");
			sortOptions.put(InsurancePolicySortParameter.SortableField.CLIENT_NAME, "Nome de Cliente");

			filtersPanel = new FiltersPanel(sortOptions);
			filtersPanel.addTypifiedListField(Filters.MANAGER, BigBangConstants.EntityIds.USER, "Gestor de Apólice");
			filtersPanel.addTypifiedListField(Filters.INSURANCE_AGENCY, BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
			filtersPanel.addTypifiedListField(Filters.MEDIATOR, BigBangConstants.EntityIds.MEDIATOR, "Mediador");
			filtersPanel.addTypifiedListField(Filters.CATEGORY, BigBangConstants.EntityIds.CATEGORY, "Categoria");
			filtersPanel.addTypifiedListField(Filters.LINE, BigBangConstants.EntityIds.LINE, "Ramo", Filters.CATEGORY);
			filtersPanel.addTypifiedListField(Filters.SUBLINE, BigBangConstants.EntityIds.SUB_LINE, "Modalidade", Filters.LINE);
			filtersPanel.addTextField(Filters.INSURED_OBJECT, "Unidade de Risco");
			filtersPanel.addCheckBoxField(Filters.CASE_STUDY, "Apenas Case Study");

			filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					doSearch();
				}
			});

			filtersContainer.clear();
			filtersContainer.add(filtersPanel);
		}

		@Override
		public ListEntry<Policy2Stub> addEntry(Policy2Stub value) {
			ListEntry<Policy2Stub> entry = new InsurancePolicySearchPanel.Entry(value);
			add(0, entry);
			return entry;
		}

		@Override
		public void doSearch() {
			if(this.workspaceId != null){
				this.broker.disposeSearch(this.workspaceId);
				this.workspaceId = null;
			}
			
			InsurancePolicySearchParameter parameter = new InsurancePolicySearchParameter();
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

			InsurancePolicySortParameter sort = new InsurancePolicySortParameter((SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder());

			SortParameter[] sorts = new SortParameter[]{
					sort
			};

			doSearch(parameters, sorts);
		}
		
	}
	
	protected static class SelectedPoliciesList extends SelectedProcessesList<Policy2Stub> {

		@Override
		public ListEntry<Policy2Stub> addEntry(Policy2Stub value) {
			ListEntry<Policy2Stub> entry = new InsurancePolicySearchPanel.Entry(value);
			entry.setChecked(true);
			this.add(entry);
			return entry;
		}
	}
	
	public InsurancePolicyMassManagerTransferView() {
		super(new InsurancePolicyForm() {

			@Override
			public void onSubLineChanged(String subLineId) {
				return;
			}
		},	new CheckablePoliciesSearchPanel(), 
			new SelectedPoliciesList());
	}

	@Override
	public void addProcessToTransfer(Policy2Stub value) {
		this.selectedList.addEntry(value);
		searchPanel.markForCheck(value.id);
	}

	@Override
	public void removeProcessFromTransfer(String id) {
		for(ValueSelectable<Policy2Stub> entry : this.selectedList){
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
