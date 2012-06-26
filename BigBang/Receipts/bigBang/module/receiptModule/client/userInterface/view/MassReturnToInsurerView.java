package bigBang.module.receiptModule.client.userInterface.view;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.library.client.HasCheckables;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.CheckableSearchPanel;
import bigBang.library.client.userInterface.FiltersPanel;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.SelectedProcessesList;
import bigBang.library.client.userInterface.view.FormView;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.receiptModule.client.dataAccess.ReceiptSearchDataBroker;
import bigBang.module.receiptModule.client.userInterface.ReceiptForm;
import bigBang.module.receiptModule.client.userInterface.ReceiptSearchPanel;
import bigBang.module.receiptModule.client.userInterface.presenter.MassReturnToInsurerViewPresenter;
import bigBang.module.receiptModule.client.userInterface.presenter.MassReturnToInsurerViewPresenter.Action;
import bigBang.module.receiptModule.shared.ModuleConstants;
import bigBang.module.receiptModule.shared.ReceiptSearchParameter;
import bigBang.module.receiptModule.shared.ReceiptSortParameter;
import bigBang.module.receiptModule.shared.ReceiptSortParameter.SortableField;

public class MassReturnToInsurerView extends View implements MassReturnToInsurerViewPresenter.Display{

	protected static enum Filters {
		TYPES,
		EMITED_FROM,
		EMITED_TO,
		MATURITY_FROM,
		MATURITY_TO,
		PAYMENT_FROM,
		PAYMENT_TO,
		CATEGORY,
		LINE,
		SUB_LINE, AGENCY
	}

	protected static class SelectedReceiptsList extends SelectedProcessesList<ReceiptStub>{

		@Override
		public ListEntry<ReceiptStub> addEntry(ReceiptStub value) {
			ListEntry<ReceiptStub> entry = new ReceiptSearchPanel.Entry(value);
			entry.setChecked(true, false);
			this.add(entry);
			return entry;
		}

	}

	protected static class CheckableReceiptsSearchPanel extends CheckableSearchPanel<ReceiptStub>{

		public FiltersPanel filtersPanel;


		public CheckableReceiptsSearchPanel(){
			super((ReceiptSearchDataBroker) ((ReceiptDataBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.RECEIPT)).getSearchBroker());

			Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>();
			sortOptions.put(ReceiptSortParameter.SortableField.RELEVANCE, "Relevância");
			sortOptions.put(ReceiptSortParameter.SortableField.TYPE, "Tipo");
			sortOptions.put(ReceiptSortParameter.SortableField.NUMBER, "Número");
			sortOptions.put(ReceiptSortParameter.SortableField.CLIENT, "Cliente");
			sortOptions.put(ReceiptSortParameter.SortableField.SUB_LINE, "Ramo");
			sortOptions.put(ReceiptSortParameter.SortableField.EMISSION_DATE, "Data de Emissão");
			sortOptions.put(ReceiptSortParameter.SortableField.LIMIT_DATE, "Data Limite");
			sortOptions.put(ReceiptSortParameter.SortableField.MATURITY_DATE, "Vigência");
			sortOptions.put(ReceiptSortParameter.SortableField.PAYMENT_DATE, "Data de Pagamento");

			filtersPanel = new FiltersPanel(sortOptions);
			filtersPanel.addTypifiedListField(Filters.AGENCY, BigBangConstants.EntityIds.INSURANCE_AGENCY, "Seguradora");
			filtersPanel.addTypifiedListField(Filters.TYPES, ModuleConstants.TypifiedListIds.RECEIPT_TYPE, "Tipos");
			filtersPanel.addDateField(Filters.EMITED_FROM, "Emitido de");
			filtersPanel.addDateField(Filters.EMITED_TO, "Até");
			filtersPanel.addDateField(Filters.MATURITY_FROM, "Vencimento de");
			filtersPanel.addDateField(Filters.MATURITY_TO, "Até");
			filtersPanel.addDateField(Filters.PAYMENT_FROM, "Pagamento de");
			filtersPanel.addDateField(Filters.PAYMENT_TO, "Até");
			filtersPanel.addTypifiedListField(Filters.CATEGORY, BigBangConstants.EntityIds.CATEGORY, "Categoria");
			filtersPanel.addTypifiedListField(Filters.LINE, BigBangConstants.EntityIds.LINE, "Ramo", Filters.CATEGORY);
			filtersPanel.addTypifiedListField(Filters.SUB_LINE, BigBangConstants.EntityIds.SUB_LINE, "Modalidade", Filters.LINE);

			filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					doSearch();
				}
			});

			this.setOperationId(BigBangConstants.OperationIds.ReceiptProcess.CREATE_PAYMENT_NOTICE);
			filtersContainer.clear();
			filtersContainer.add(filtersPanel);
			doSearch();

		}

		@Override
		public ListEntry<ReceiptStub> addEntry(ReceiptStub value) {
			ListEntry<ReceiptStub> entry = new ReceiptSearchPanel.Entry(value);
			add(0, entry);
			return entry;
		}

		@Override
		public void doSearch() {

			if(this.workspaceId != null){
				this.broker.disposeSearch(this.workspaceId);
				this.workspaceId = null;
			}

			ReceiptSearchParameter parameter = new ReceiptSearchParameter();
			parameter.freeText = this.getFreeText();
			String type = (String) filtersPanel.getFilterValue(Filters.TYPES);
			parameter.typeIds = type == null ? new String[0] : new String[]{type};
			parameter.companyId = (String) filtersPanel.getFilterValue(Filters.AGENCY);
			parameter.emitedFrom = (String) filtersPanel.getFilterValue(Filters.EMITED_FROM);
			parameter.emitedTo = (String) filtersPanel.getFilterValue(Filters.EMITED_TO);
			parameter.maturityFrom = (String) filtersPanel.getFilterValue(Filters.MATURITY_FROM);
			parameter.maturityTo = (String) filtersPanel.getFilterValue(Filters.EMITED_TO);
			parameter.paymentFrom = (String) filtersPanel.getFilterValue(Filters.PAYMENT_FROM);
			parameter.paymentTo = (String) filtersPanel.getFilterValue(Filters.PAYMENT_TO);
			parameter.categoryId = (String) filtersPanel.getFilterValue(Filters.CATEGORY);
			parameter.lineId = (String) filtersPanel.getFilterValue(Filters.LINE);
			parameter.subLineId = (String) filtersPanel.getFilterValue(Filters.SUB_LINE);

			SearchParameter[] parameters = new SearchParameter[] {
					parameter
			};

			ReceiptSortParameter sortParameter = new ReceiptSortParameter((SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder());

			ReceiptSortParameter[] sorts = new ReceiptSortParameter[]{
					sortParameter
			};

			doSearch(parameters, sorts);
		}

	}
	
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected CheckableReceiptsSearchPanel searchPanel;
	protected SelectedReceiptsList selectedReceipts;
	protected ReceiptForm receiptForm;
	protected Button returnReceipts, clearButton;
	
	public MassReturnToInsurerView(){
		
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		searchPanel = new CheckableReceiptsSearchPanel();
		VerticalPanel searchPanelWrapper = new VerticalPanel();
		searchPanelWrapper.setSize("100%", "100%");
		searchPanelWrapper.add(new ListHeader("Lista de Recibos"));
		searchPanelWrapper.add(searchPanel);
		searchPanelWrapper.setCellHeight(searchPanel, "100%");

		Button selectAllButton = new Button("Seleccionar Todos", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.SELECT_ALL));
			}
		});

		selectAllButton.setWidth("100%");
		searchPanelWrapper.add(selectAllButton);
		wrapper.addWest(searchPanelWrapper, 400);

		returnReceipts = new Button("Devolver recibos");

		HorizontalPanel sendClearWrapper = new HorizontalPanel();
		sendClearWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		sendClearWrapper.setHeight("100%");
		sendClearWrapper.add(returnReceipts);
		returnReceipts.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.MASS_RETURN_TO_INSURER));
			}
		});

		receiptForm = new ReceiptForm();

		FormView<Void> applyPaymentNoticeCreationForm = new FormView<Void>() {

			@Override
			public void setInfo(Void info) {
				return;
			}

			@Override
			public Void getInfo() {
				return null;
			}
		};
		
		applyPaymentNoticeCreationForm.addSection("Devolver recibos");
		
		
		VerticalPanel selectedListWrapper = new VerticalPanel();
		selectedListWrapper.add(new ListHeader("Devolver recibos"));
		selectedListWrapper.setSize("100%", "100%");
		applyPaymentNoticeCreationForm.addWidget(sendClearWrapper);
		selectedListWrapper.add(applyPaymentNoticeCreationForm.getNonScrollableContent());
		applyPaymentNoticeCreationForm.getNonScrollableContent().setSize("100%", "40px");
		selectedListWrapper.setCellWidth(applyPaymentNoticeCreationForm, "100%");
		clearButton = new Button("Limpar");
		sendClearWrapper.add(clearButton);
		sendClearWrapper.setSpacing(5);
		clearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CLEAR));
			}
		});
		selectedReceipts = new SelectedReceiptsList();
		
		selectedListWrapper.add(selectedReceipts);
		selectedListWrapper.setCellHeight(selectedReceipts, "100%");
		wrapper.addEast(selectedListWrapper, 400);

		receiptForm.setReadOnly(true);
		VerticalPanel receiptWrapper = new VerticalPanel();
		receiptWrapper.setSize("100%", "100%");
		receiptWrapper.add(new ListHeader("Ficha do Recibo"));
		receiptWrapper.add(receiptForm);
		receiptWrapper.setCellHeight(receiptForm,"100%");
		wrapper.add(receiptWrapper);
		
		
	}
	
	@Override
	public void addReceiptToReturn(ReceiptStub value) {
		this.selectedReceipts.addEntry(value);
		searchPanel.markForCheck(value.id);
	}

	@Override
	public void removeReceiptToReturn(String id) {
		for(ValueSelectable<ReceiptStub> entry : this.selectedReceipts){
			if(id.equalsIgnoreCase(entry.getValue().id)){
				this.selectedReceipts.remove(entry);
				break;
			}
		}
		searchPanel.markForUncheck(id);
	}

	@Override
	public HasCheckables getCheckableSelectedList() {
		return this.selectedReceipts;
	}

	@Override
	public HasEditableValue<Receipt> getReceiptForm() {
		return receiptForm;
	}

	@Override
	public HasValueSelectables<ReceiptStub> getMainList() {
		return searchPanel;
	}

	@Override
	public HasValueSelectables<ReceiptStub> getSelectedList() {
		return selectedReceipts;
	}

	@Override
	public HasCheckables getCheckableMainList() {
		return searchPanel;
	}

	@Override
	public void refreshMainList() {
		searchPanel.doSearch();
	}

	@Override
	public void markAllForCheck() {
		if(searchPanel.getWorkspaceId() != null){
			SearchDataBroker<ReceiptStub> broker = this.searchPanel.getSearchBroker();
			broker.getResults(searchPanel.getWorkspaceId(), 0, -1, new ResponseHandler<Search<ReceiptStub>>() {
				
				@Override
				public void onResponse(Search<ReceiptStub> response) {
					Collection <ReceiptStub> results = response.getResults();
					for(ReceiptStub result : results){
						if(!searchPanel.isMarkedForCheck(result.id)){
							searchPanel.markForCheck(result.id);
							selectedReceipts.addEntry(result);
						}
					}
				}
				
				@Override
				public void onError(Collection<ResponseError> errors) {
					return;
				}
			});
		}
	}

	@Override
	public void markForCheck(String id) {
		this.searchPanel.markForCheck(id);
	}

	@Override
	public void markForUncheck(String id) {
		this.searchPanel.markForUncheck(id);
	}

	@Override
	public void removeAllReceiptsFromReturn() {
		while(!this.selectedReceipts.isEmpty()){
			this.selectedReceipts.get(0).setChecked(false, true);
		}
		
	}

	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> actionInvockedEventHandker) {
		this.actionHandler = actionInvockedEventHandker;
		
	}

	@Override
	public void allowCreation(boolean b) {
		returnReceipts.setEnabled(b);
		clearButton.setEnabled(b);
		searchPanel.setCheckable(b);
	}

	@Override
	protected void initializeView() {
		return;
	}
	
}


