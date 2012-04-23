package bigBang.module.casualtyModule.client.userInterface.view;

import java.util.Map;
import java.util.TreeMap;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
import bigBang.definitions.shared.CasualtyStub;
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
import bigBang.module.casualtyModule.client.userInterface.CasualtyForm;
import bigBang.module.casualtyModule.client.userInterface.CasualtySearchPanel.Filters;
import bigBang.module.casualtyModule.client.userInterface.CasualtySearchPanelListEntry;
import bigBang.module.casualtyModule.shared.CasualtySearchParameter;
import bigBang.module.casualtyModule.shared.CasualtySortParameter;
import bigBang.module.casualtyModule.shared.CasualtySortParameter.SortableField;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class CasualtyMassManagerTransferView extends
		MassManagerTransferView<CasualtyStub, Casualty> {

	protected static class CheckableCasualtiesSearchPanel extends CheckableSearchPanel<CasualtyStub> {

		public FiltersPanel filtersPanel;
		
		public CheckableCasualtiesSearchPanel() {
			super(((CasualtyDataBroker)DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.CASUALTY)).getSearchBroker());
			
			Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
			sortOptions.put(CasualtySortParameter.SortableField.RELEVANCE, "Relevância");
			sortOptions.put(CasualtySortParameter.SortableField.NUMBER, "Número");
			sortOptions.put(CasualtySortParameter.SortableField.DATE, "Data");
			sortOptions.put(CasualtySortParameter.SortableField.MANAGER, "Gestor");
			sortOptions.put(CasualtySortParameter.SortableField.CLIENT_NUMBER, "Número de Cliente");
			sortOptions.put(CasualtySortParameter.SortableField.CLIENT_NAME, "Nome de Cliente");
			
			filtersPanel = new FiltersPanel(sortOptions);
			filtersPanel.addDateField(Filters.DATE_FROM, "Ocorrido de");
			filtersPanel.addDateField(Filters.DATE_TO, "Ocorrido até");
			filtersPanel.addTypifiedListField(Filters.MANAGER, BigBangConstants.EntityIds.USER, "Gestor");
			filtersPanel.addCheckBoxField(Filters.CASE_STUDY, "Apenas Case Study");
			filtersPanel.addCheckBoxField(Filters.INCLUDE_CLOSED, "Incluir Encerrados");

			filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					doSearch();
				}
			});

			filtersContainer.clear();
			filtersContainer.add(filtersPanel);

			setOperationId(BigBangConstants.OperationIds.CasualtyProcess.CREATE_MANAGER_TRANSFER);
		}

		@Override
		public ListEntry<CasualtyStub> addEntry(CasualtyStub value) {
			ListEntry<CasualtyStub> entry = new CasualtySearchPanelListEntry(value);
			add(0, entry);
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

			p.dateFrom = (String) filtersPanel.getFilterValue(Filters.DATE_FROM);
			p.dateTo = (String) filtersPanel.getFilterValue(Filters.DATE_TO);
			p.managerId = (String) filtersPanel.getFilterValue(Filters.MANAGER);
			p.caseStudy = (Boolean) filtersPanel.getFilterValue(Filters.CASE_STUDY);
			p.includeClosed = (Boolean) filtersPanel.getFilterValue(Filters.INCLUDE_CLOSED);

			parameters[0] = p;

			SortParameter[] sorts = new SortParameter[]{
					new CasualtySortParameter((SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder())
			};

			doSearch(parameters, sorts);
		}

	}

	protected static class SelectedCasualtiesList extends SelectedProcessesList<CasualtyStub> {

		@Override
		public ListEntry<CasualtyStub> addEntry(CasualtyStub value) {
			ListEntry<CasualtyStub> entry = new CasualtySearchPanelListEntry(value);
			entry.setChecked(true);
			this.add(entry);
			return entry;
		}
	}

	public CasualtyMassManagerTransferView() {
		super(new CasualtyForm(), 
				new CheckableCasualtiesSearchPanel(), 
				new SelectedCasualtiesList());
	}

	@Override
	public void addProcessToTransfer(CasualtyStub value) {
		this.selectedList.addEntry(value);
		searchPanel.markForCheck(value.id);
	}

	@Override
	public void removeProcessFromTransfer(String id) {
		for(ValueSelectable<CasualtyStub> entry : this.selectedList){
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
