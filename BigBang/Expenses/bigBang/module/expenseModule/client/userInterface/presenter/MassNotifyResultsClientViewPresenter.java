package bigBang.module.expenseModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Expense;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.library.client.Checkable;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasCheckables;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.CheckedSelectionChangedEvent;
import bigBang.library.client.event.CheckedSelectionChangedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class MassNotifyResultsClientViewPresenter implements ViewPresenter{
	private Display view;
	private boolean bound = false;
	protected ExpenseDataBroker broker;
	
	public MassNotifyResultsClientViewPresenter(Display view){
		setView((UIObject) view);
		broker = (ExpenseDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.EXPENSE);
	}
	
	public interface Display{
		void addExpenseToNotifyResults(ExpenseStub stub);
		void removeExpenseToNotifyResults(String id);
		HasCheckables getCheckableSelectedList();
		HasEditableValue<Expense> getExpenseForm();
		HasValueSelectables<ExpenseStub> getMainList();
		HasValueSelectables<ExpenseStub> getSelectedList();
		HasCheckables getCheckableMainList();
		void refreshMainList();
		void markAllForCheck();
		void markForCheck(String id);
		void markForUncheck(String id);
		void removeAllExpensesToNotifyResults();
		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void allowCreation(boolean b);
	}
	
	public enum Action{
		SELECT_ALL, NOTIFY_RESULTS_CLIENT, CLEAR
	}

	@Override
	public void setView(UIObject view) {
		this.view = (Display)view;
		
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	private void bind() {
		if(bound){return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CLEAR:
					view.removeAllExpensesToNotifyResults();
					break;
				case NOTIFY_RESULTS_CLIENT:
					notifyResults(view.getSelectedList().getAll());
					break;
				case SELECT_ALL:
					view.markAllForCheck();
					break;
				}	
			}
		});
		
		view.getCheckableMainList().addCheckedSelectionChangedEventHandler(new CheckedSelectionChangedEventHandler() {
			
			@Override
			public void onCheckedSelectionChanged(CheckedSelectionChangedEvent event) {
				Checkable checkable = event.getChangedCheckable();
				
				@SuppressWarnings("unchecked")
				ValueSelectable<ExpenseStub> entry = (ValueSelectable<ExpenseStub>) checkable;
				String id = entry.getValue().id;
				
				if(checkable.isChecked()){
					view.markForCheck(id);
					view.addExpenseToNotifyResults(entry.getValue());
				}else{
					view.markForUncheck(id);
					view.removeExpenseToNotifyResults(id);
				}
				
			}
		});
		
		view.getCheckableSelectedList().addCheckedSelectionChangedEventHandler(new CheckedSelectionChangedEventHandler() {
			
			@Override
			public void onCheckedSelectionChanged(CheckedSelectionChangedEvent event) {
				Checkable checkable = event.getChangedCheckable();
				
				@SuppressWarnings("unchecked")
				ValueSelectable<ExpenseStub> entry = (ValueSelectable<ExpenseStub>) checkable;
				String id = entry.getValue().id;
	
				if(checkable.isChecked()){
					view.markForCheck(id);
				}else{
					view.markForUncheck(id);
					view.removeExpenseToNotifyResults(id);
				}
			}
		});

		view.getMainList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				final ValueSelectable<ExpenseStub> selectable = (ValueSelectable<ExpenseStub>) event.getFirstSelected();
				
				if(selectable != null){
					broker.getExpense(selectable.getValue().id, new ResponseHandler<Expense>() {

						@Override
						public void onResponse(Expense response) {
							view.getExpenseForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Despesa de Saúde"), TYPE.ALERT_NOTIFICATION));

						}
					});
				}
			}
		});
		
		view.getSelectedList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
			
				@SuppressWarnings("unchecked")
				final ValueSelectable<ExpenseStub> selectable = (ValueSelectable<ExpenseStub>) event.getFirstSelected();
			
				if(selectable != null){
					broker.getExpense(selectable.getValue().id, new ResponseHandler<Expense>() {

						@Override
						public void onResponse(Expense response) {
							view.getExpenseForm().setValue(response);
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a Despesa de Saúde"), TYPE.ALERT_NOTIFICATION));

						}
					});
				}
			}
		});
		bound = true;
	}

	protected void notifyResults(
			Collection<ValueSelectable<ExpenseStub>> all) {

			String[] toNotify = new String[all.size()];
			int counter = 0;

			for(ValueSelectable<ExpenseStub> stub: all){
				toNotify[counter] = stub.getValue().id;
				counter++;
			}

			broker.massNotifyClient(toNotify, new ResponseHandler<Void>() {

				@Override
				public void onResponse(Void response) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Comunicações enviadas com sucesso"), TYPE.TRAY_NOTIFICATION));
					NavigationHistoryManager.getInstance().reload();
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível enviar as comunicações"), TYPE.ALERT_NOTIFICATION));
				}
			});
		
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();
		view.refreshMainList();
		showMassNotifyResultsScreen();
	}
	

	private void clearView() {
		view.removeAllExpensesToNotifyResults();
		view.getExpenseForm().setValue(null);
	}	
	

	protected void onUserLacksPermission() {
//		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não tem permissões para realizar esta operação"), TYPE.ALERT_NOTIFICATION));
	}

	private void showMassNotifyResultsScreen() {
		checkUserPermission(new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				view.allowCreation(response);
				view.getExpenseForm().setValue(null);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onUserLacksPermission();
			}		
				
		});
		
	}

	private void checkUserPermission(final ResponseHandler<Boolean> responseHandler) {
		PermissionChecker.hasGeneralPermission(BigBangConstants.EntityIds.EXPENSE, BigBangConstants.OperationIds.ExpenseProcess.NOTIFY_CLIENT, new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				responseHandler.onResponse(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(false);
			}
		});
	}
	
}