package bigBang.module.expenseModule.client.userInterface.view;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HealthExpense;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ExpenseForm;
import bigBang.library.client.userInterface.view.View;
import bigBang.module.expenseModule.client.userInterface.ExpenseChildrenPanel;
import bigBang.module.expenseModule.client.userInterface.ExpenseProcessToolBar;
import bigBang.module.expenseModule.client.userInterface.ExpenseSearchPanel;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseSearchOperationViewPresenter;
import bigBang.module.expenseModule.client.userInterface.presenter.ExpenseSearchOperationViewPresenter.Action;

public class ExpenseSearchOperationView extends View implements ExpenseSearchOperationViewPresenter.Display {

	protected static final int SEARCH_PANEL_WIDTH = 400; //PX 
	
	protected ExpenseSearchPanel searchPanel;
	protected ExpenseForm form;
	protected ExpenseProcessToolBar operationsToolbar;
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected ExpenseChildrenPanel childrenPanel;
	
	public ExpenseSearchOperationView(){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");
		
		searchPanel = new ExpenseSearchPanel();
		mainWrapper.addWest(searchPanel, SEARCH_PANEL_WIDTH);
		
		VerticalPanel formWrapper = new VerticalPanel();
		formWrapper.setSize("100%", "100%");
		
		operationsToolbar = new ExpenseProcessToolBar(){

			@Override
			protected void onDeleteRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExpenseSearchOperationViewPresenter.Action>(Action.DELETE));
			}

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExpenseSearchOperationViewPresenter.Action>(Action.EDIT));
			}

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExpenseSearchOperationViewPresenter.Action>(Action.SAVE));
				
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExpenseSearchOperationViewPresenter.Action>(Action.CANCEL));
				
			}

			@Override
			protected void onInfoFromInsurer() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExpenseSearchOperationViewPresenter.Action>(Action.INFO_FROM_INSURER));

			}

			@Override
			protected void onInfoOrDocumentRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExpenseSearchOperationViewPresenter.Action>(Action.INFO_OR_DOCUMENT_REQUEST));

			}

			@Override
			protected void onValidate() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExpenseSearchOperationViewPresenter.Action>(Action.VALIDATE));

			}

			@Override
			protected void onNotifyClient() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExpenseSearchOperationViewPresenter.Action>(Action.NOTIFY_CLIENT));

			}

			@Override
			protected void participateToInsurer() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExpenseSearchOperationViewPresenter.Action>(Action.PARTICIPATE_TO_INSURER));

			}

			@Override
			protected void onReturnToClient() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExpenseSearchOperationViewPresenter.Action>(Action.RETURN_TO_CLIENT));

			}

			@Override
			public void onCloseProcess() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ExpenseSearchOperationViewPresenter.Action>(Action.CLOSE_PROCESS));
			}
		};
		
		formWrapper.add(operationsToolbar);
		formWrapper.setCellHeight(operationsToolbar, "21px");
		
		form = new ExpenseForm();
		formWrapper.add(form);
		
		SplitLayoutPanel contentWrapper = new SplitLayoutPanel();
		contentWrapper.setSize("100%", "100%");
		
		childrenPanel = new ExpenseChildrenPanel();
		childrenPanel.setHeight("100%");
		contentWrapper.addEast(childrenPanel, 300);
		
		contentWrapper.add(formWrapper);
		mainWrapper.add(contentWrapper);
		
		if(!bigBang.definitions.client.Constants.DEBUG){
			searchPanel.doSearch();
		}
		
		form.addValueChangeHandler(new ValueChangeHandler<HealthExpense>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<HealthExpense> event) {
				HealthExpense expense = event.getValue();
				childrenPanel.setExpense(expense);
			}
		});
		
	}

	@Override
	protected void initializeView() {
		return;
	}
	
	@Override
	public HasValueSelectables<?> getList(){
		return searchPanel;
	}
	
	@Override
	public HasEditableValue<HealthExpense> getForm(){
		return form;
	}
	
	@Override
	public boolean isFormValid(){
		return this.form.validate();
	}
	
	@Override
	public void clearAllowedPermissions() {
		this.operationsToolbar.lockAll();
	}
	
	@Override
	public void registerActionInvokedHandler(
			ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}
	
	@Override
	public void setSaveModeEnabled(boolean enabled) {
		this.operationsToolbar.setSaveModeEnabled(enabled);
	}
	
	@Override
	public void allowEdit(boolean allow) {
		this.operationsToolbar.setEditionAvailable(allow);
	}

	@Override
	public void allowDelete(boolean allow) {
		this.operationsToolbar.allowDelete(allow);
	}
	
	@Override
	public HasValueSelectables<Contact> getContactsList() {
		return this.childrenPanel.contactsList;
	}

	@Override
	public HasValueSelectables<Document> getDocumentsList() {
		return this.childrenPanel.documentsList;
	}
	
	@Override
	public HasValueSelectables<BigBangProcess> getSubProcessesList() {
		return this.childrenPanel.subProcessesList;
	}
	
	@Override
	public HasValueSelectables<HistoryItemStub> getHistoryList() {
		return this.childrenPanel.historyList;
	}

	@Override
	public void clear() {
		form.clearInfo();
	}

	@Override
	public void lockOptions() {
		operationsToolbar.lockAll();
	}
}
