package bigBang.module.expenseModule.client.userInterface.presenter;

import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.BigBangProcess;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.HealthExpense;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.client.userInterface.view.View;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ExpenseSearchOperationViewPresenter implements ViewPresenter {

	public enum Action{
		EDIT, SAVE, CANCEL, DELETE
	}
	
	public interface Display {
		Widget asWidget();

		HasValueSelectables<?> getList();

		HasEditableValue<HealthExpense> getForm();

		boolean isFormValid();

		void clearAllowedPermissions();

		void registerActionInvokedHandler(
				ActionInvokedEventHandler<Action> handler);

		void setSaveModeEnabled(boolean enabled);

		void allowEdit(boolean allow);

		void allowDelete(boolean allow);

		HasValueSelectables<Contact> getContactsList();

		HasValueSelectables<Document> getDocumentsList();

		HasValueSelectables<BigBangProcess> getSubProcessesList();

		HasValueSelectables<HistoryItemStub> getHistoryList();
		
		void clear();
	}

	protected boolean bound = false;
	protected Display view;
	protected ExpenseDataBroker expenseBroker;
	private String expenseId;
	
	public ExpenseSearchOperationViewPresenter(View view){
		this.setView(view);
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
		expenseBroker = (ExpenseDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.EXPENSE);
	}

	@Override
	public void go(HasWidgets container) {
		this.bind();
		container.clear();
		container.add(this.view.asWidget());
	}
	
	@Override
	public void setParameters(HasParameters parameterHolder) {
		expenseId = parameterHolder.getParameter("expenseId");
		
		if(expenseId == null || expenseId.isEmpty()){
			clearView();
		}else{
			showExpense(expenseId);
		}
		
	}

	private void clearView() {
		view.clear();
	}

	private void showExpense(String expenseId2) {
		// TODO Auto-generated method stub
		
	}

	public void bind() {
		if(bound)
			return;
		view.getList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		view.registerActionInvokedHandler(new ActionInvokedEventHandler<ExpenseSearchOperationViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				// TODO Auto-generated method stub
				
			}
		});
		
		view.getContactsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		view.getDocumentsList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		view.getHistoryList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		
		view.getSubProcessesList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
		bound = true;
	}
	
}
