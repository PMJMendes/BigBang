package bigBang.module.expenseModule.client.userInterface.view;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.dataAccess.Search;
import bigBang.definitions.client.dataAccess.SearchDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.definitions.shared.SearchParameter;
import bigBang.definitions.shared.SortParameter;
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
import bigBang.module.expenseModule.client.dataAccess.ExpenseSearchDataBroker;
import bigBang.module.expenseModule.client.userInterface.ExpenseSearchPanel;
import bigBang.module.expenseModule.client.userInterface.form.ExpenseForm;
import bigBang.module.expenseModule.client.userInterface.presenter.MassNotifyResultsClientViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.MassNotifyResultsClientViewPresenter.Action;
import bigBang.module.expenseModule.shared.ExpenseSearchParameter;
import bigBang.module.expenseModule.shared.ExpenseSortParameter;
import bigBang.module.expenseModule.shared.ExpenseSortParameter.SortableField;

public class MassNotifyResultsClientView extends View implements MassNotifyResultsClientViewPresenter.Display{

	protected static enum Filters{
		EXPENSE_DATE_FROM,
		EXPENSE_DATE_TO
	}
	
	
	protected static class SelectedExpensesList extends SelectedProcessesList<ExpenseStub>{

		@Override
		public ListEntry<ExpenseStub> addEntry(ExpenseStub value) {
			ListEntry<ExpenseStub> entry = new ExpenseSearchPanel.Entry(value);
			entry.setChecked(true, false);
			this.add(entry);
			return entry;
		}

	}
	
	

	protected static class CheckableExpensesSearchPanel extends CheckableSearchPanel<ExpenseStub>{

		protected FiltersPanel filtersPanel;

		public CheckableExpensesSearchPanel(){
			super((ExpenseSearchDataBroker) ((ExpenseDataBroker)DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.EXPENSE)).getSearchBroker());
			Map<Enum<?>, String> sortOptions = new TreeMap<Enum<?>, String>(); 
			sortOptions.put(ExpenseSortParameter.SortableField.RELEVANCE, "Relevância");
			sortOptions.put(ExpenseSortParameter.SortableField.NUMBER, "Número");
			sortOptions.put(ExpenseSortParameter.SortableField.DATE, "Data da Despesa");

			filtersPanel = new FiltersPanel(sortOptions);
			filtersPanel.addDateField(Filters.EXPENSE_DATE_FROM, "De");
			filtersPanel.addDateField(Filters.EXPENSE_DATE_TO, "Até");


			filtersPanel.getApplyButton().addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					doSearch();
				}
			});

			this.setOperationId(BigBangConstants.OperationIds.ExpenseProcess.NOTIFY_CLIENT);
			filtersContainer.clear();
			filtersContainer.add(filtersPanel);

			doSearch();
		}

		@Override
		public ListEntry<ExpenseStub> addEntry(ExpenseStub value) {
			ListEntry<ExpenseStub> entry = new ExpenseSearchPanel.Entry(value);
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
			ExpenseSearchParameter p = new ExpenseSearchParameter();

			p.freeText = this.getFreeText();

			Date dateFrom = (Date) filtersPanel.getFilterValue(Filters.EXPENSE_DATE_FROM);
			p.dateFrom = dateFrom == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(dateFrom);
			Date dateTo = (Date) filtersPanel.getFilterValue(Filters.EXPENSE_DATE_TO);
			p.dateTo = dateTo == null ? null : DateTimeFormat.getFormat("yyyy-MM-dd").format(dateTo);
		
			parameters[0] = p;
			
			SortParameter[] sorts = new SortParameter[]{
					new ExpenseSortParameter((SortableField) filtersPanel.getSelectedSortableField(), filtersPanel.getSortingOrder())
			};
			
			doSearch(parameters, sorts);
		}

	}

	protected ActionInvokedEventHandler<Action> actionHandler;
	protected CheckableExpensesSearchPanel searchPanel;
	protected SelectedExpensesList selectedExpenses;
	protected ExpenseForm expenseForm;
	protected Button notifyResultsToClient, clearButton;
	
	public MassNotifyResultsClientView(){
		
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");

		searchPanel = new CheckableExpensesSearchPanel();
		VerticalPanel searchPanelWrapper = new VerticalPanel();
		searchPanelWrapper.setSize("100%", "100%");
		searchPanelWrapper.add(new ListHeader("Lista de Despesas de Saúde"));
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

		notifyResultsToClient = new Button("Notificar Resultados ao Segurado");

		HorizontalPanel sendClearWrapper = new HorizontalPanel();
		sendClearWrapper.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		sendClearWrapper.setHeight("100%");
		sendClearWrapper.add(notifyResultsToClient);
		notifyResultsToClient.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.NOTIFY_RESULTS_CLIENT));
			}
		});

		expenseForm = new ExpenseForm();

		FormView<Void> notifyResultsToClient = new FormView<Void>(){

			@Override
			public Void getInfo() {
				return null;
			}

			@Override
			public void setInfo(Void info) {
				return;
			}

		};

		notifyResultsToClient.addSection("Notificar Resultados ao Segurado");

		VerticalPanel selectedListWrapper = new VerticalPanel();
		selectedListWrapper.add(new ListHeader("Notificar Resultados ao Segurado"));
		selectedListWrapper.setSize("100%", "100%");
		notifyResultsToClient.addWidget(sendClearWrapper);
		selectedListWrapper.add(notifyResultsToClient.getNonScrollableContent());
		notifyResultsToClient.getNonScrollableContent().setSize("100%", "40px");
		selectedListWrapper.setCellWidth(notifyResultsToClient, "100%");
		clearButton = new Button("Limpar");
		sendClearWrapper.add(clearButton);
		sendClearWrapper.setSpacing(5);
		clearButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(Action.CLEAR));
			}
		});

		selectedExpenses = new SelectedExpensesList();

		selectedListWrapper.add(selectedExpenses);
		selectedListWrapper.setCellHeight(selectedExpenses, "100%");
		wrapper.addEast(selectedListWrapper, 400);

		expenseForm.setReadOnly(true);
		VerticalPanel expenseWrapper = new VerticalPanel();
		expenseWrapper.setSize("100%", "100%");
		expenseWrapper.add(new ListHeader("Ficha da Despesa de Saúde"));
		expenseWrapper.add(expenseForm);
		expenseWrapper.setCellHeight(expenseForm, "100%");
		wrapper.add(expenseWrapper);
	}



	@Override
	public void addExpenseToNotifyResults(ExpenseStub stub) {
		selectedExpenses.addEntry(stub);
		searchPanel.markForCheck(stub.id);

	}

	@Override
	public void removeExpenseToNotifyResults(String id) {
		for(ValueSelectable<ExpenseStub> entry : selectedExpenses){
			if(id.equalsIgnoreCase(entry.getValue().id)){
				this.selectedExpenses.remove(entry);
				break;
			}
		}
		searchPanel.markForUncheck(id);
	}

	@Override
	public HasCheckables getCheckableSelectedList() {
		return this.selectedExpenses;
	}

	@Override
	public HasEditableValue<Expense> getExpenseForm() {
		return expenseForm;
	}

	@Override
	public HasValueSelectables<ExpenseStub> getMainList() {
		return searchPanel;
	}

	@Override
	public HasValueSelectables<ExpenseStub> getSelectedList() {
		return selectedExpenses;
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
			SearchDataBroker<ExpenseStub> broker = this.searchPanel.getSearchBroker();
			broker.getResults(searchPanel.getWorkspaceId(), 0, -1, new ResponseHandler<Search<ExpenseStub>>() {

				@Override
				public void onResponse(Search<ExpenseStub> response) {
					Collection <ExpenseStub> results = response.getResults();
					for(ExpenseStub result : results){
						if(!searchPanel.isMarkedForCheck(result.id)){
							searchPanel.markForCheck(result.id);
							selectedExpenses.addEntry(result);
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
		searchPanel.markForCheck(id);
	}

	@Override
	public void markForUncheck(String id) {
		searchPanel.markForUncheck(id);
	}

	@Override
	public void removeAllExpensesToNotifyResults() {
		while(!this.selectedExpenses.isEmpty()){
			this.selectedExpenses.get(0).setChecked(false, true);
		}
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;

	}

	@Override
	public void allowCreation(boolean b) {
		notifyResultsToClient.setEnabled(b);
		clearButton.setEnabled(b);
		searchPanel.setCheckable(b);
	}

	@Override
	protected void initializeView() {
		return;	
	}

}
