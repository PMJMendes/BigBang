package bigBang.module.clientModule.client.userInterface.view;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ClientStub;
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
import bigBang.module.clientModule.client.dataAccess.ClientSearchDataBroker;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanelListEntry;
import bigBang.module.clientModule.client.userInterface.ClientSearchPanel.Filters;
import bigBang.module.clientModule.client.userInterface.form.ClientForm;
import bigBang.module.clientModule.shared.ClientSearchParameter;
import bigBang.module.clientModule.shared.ClientSortParameter;
import bigBang.module.clientModule.shared.ModuleConstants;
import bigBang.module.clientModule.shared.ClientSortParameter.SortableField;

public class ClientMassManagerTransferView extends MassManagerTransferView<ClientStub, Client> {

	protected static class CheckableClientsSearchPanel extends CheckableSearchPanel<ClientStub> {

		public FiltersPanel filtersPanel;
		
		public CheckableClientsSearchPanel() {
			super((ClientSearchDataBroker) ((ClientProcessBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT)).getSearchBroker());
			
			Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
			sortOptions.put(ClientSortParameter.SortableField.RELEVANCE, "Relevância");
			sortOptions.put(ClientSortParameter.SortableField.NAME, "Nome");
			sortOptions.put(ClientSortParameter.SortableField.GROUP, "Grupo");
			sortOptions.put(ClientSortParameter.SortableField.NUMBER, "Número");

			filtersPanel = new FiltersPanel(sortOptions);
			filtersPanel.addTypifiedListField(Filters.MANAGER, BigBangConstants.EntityIds.USER, "Gestor de Cliente");
			filtersPanel.addTypifiedListField(Filters.COST_CENTER, BigBangConstants.EntityIds.COST_CENTER, "Centro de Custo");
			filtersPanel.addTypifiedListField(Filters.MEDIATOR, BigBangConstants.EntityIds.MEDIATOR, "Mediador");

			filtersPanel.addTypifiedListField(Filters.PROFESSION, ModuleConstants.ListIDs.Professions, "Profissão");
			filtersPanel.addTypifiedListField(Filters.OPERATIONAL_PROFILE, ModuleConstants.ListIDs.OperationalProfiles, "Perfil Op.");
			filtersPanel.addTypifiedListField(Filters.COMPANY_SIZE, ModuleConstants.ListIDs.CompanySizes, "Num. de Trab.");
			filtersPanel.addTypifiedListField(Filters.SALES_VOLUME, BigBangConstants.TypifiedListIds.SALES_VOLUMES, "Vol. de Vendas");

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
			
			showSearchField(true);
			setOperationId(BigBangConstants.OperationIds.ClientProcess.CREATE_MANAGER_TRANSFER);
		}

		@Override
		public ListEntry<ClientStub> addEntry(ClientStub value) {
			ListEntry<ClientStub> entry = new ClientSearchPanelListEntry(value);
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

	}

	protected static class SelectedClientsList extends SelectedProcessesList<ClientStub> {

		@Override
		public ListEntry<ClientStub> addEntry(ClientStub value) {
			ListEntry<ClientStub> entry = new ClientSearchPanelListEntry(value);
			entry.setChecked(true);
			this.add(entry);
			return entry;
		}
	}

	public ClientMassManagerTransferView() {
		super(new ClientForm(), 
				new CheckableClientsSearchPanel(), 
				new SelectedClientsList());
	}

	@Override
	public void addProcessToTransfer(ClientStub value) {
		this.selectedList.addEntry(value);
		searchPanel.markForCheck(value.id);
	}

	@Override
	public void removeProcessFromTransfer(String id) {
		for(ValueSelectable<ClientStub> entry : this.selectedList){
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
