package bigBang.module.expenseModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.HealthExpense;
import bigBang.definitions.shared.HealthExpenseStub;
import bigBang.library.client.Checkable;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasCheckables;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
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

public class MassParticipateToInsurerViewPresenter implements ViewPresenter {

	private Display view;
	private boolean bound = false;
	protected ExpenseDataBroker broker;
	
	public MassParticipateToInsurerViewPresenter(Display view){
		setView((UIObject) view);
		broker = (ExpenseDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.EXPENSE);
	}
	
	public interface Display{
		void addExpenseToParticipate(HealthExpenseStub stub);
		void removeExpenseToParticipate(String id);
		HasCheckables getCheckableSelectedList();
		HasEditableValue<HealthExpense> getExpenseForm();
		HasValueSelectables<HealthExpenseStub> getMainList();
		HasValueSelectables<HealthExpenseStub> getSelectedList();
		HasCheckables getCheckableMainList();
		void refreshMainList();
		void markAllForCheck();
		void markForCheck(String id);
		void markForUncheck(String id);
		void removeAllExpensesToParticipate();
		Widget asWidget();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void allowCreation(boolean b);
	}
	
	public enum Action{
		SELECT_ALL, PARTICIPATE_TO_INSURER, CLEAR
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
		
		view.registerActionHandler(new ActionInvokedEventHandler<MassParticipateToInsurerViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CLEAR:
					view.removeAllExpensesToParticipate();
					break;
				case PARTICIPATE_TO_INSURER:
					participateToInsurer(view.getSelectedList().getAll());
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
				ValueSelectable<HealthExpenseStub> entry = (ValueSelectable<HealthExpenseStub>) checkable;
				String id = entry.getValue().id;
				
				if(checkable.isChecked()){
					view.markForCheck(id);
					view.addExpenseToParticipate(entry.getValue());
				}else{
					view.markForUncheck(id);
					view.removeExpenseToParticipate(id);
				}
				
			}
		});
		
		view.getCheckableSelectedList().addCheckedSelectionChangedEventHandler(new CheckedSelectionChangedEventHandler() {
			
			@Override
			public void onCheckedSelectionChanged(CheckedSelectionChangedEvent event) {
				Checkable checkable = event.getChangedCheckable();
				
				@SuppressWarnings("unchecked")
				ValueSelectable<HealthExpenseStub> entry = (ValueSelectable<HealthExpenseStub>) checkable;
				String id = entry.getValue().id;
	
				if(checkable.isChecked()){
					view.markForCheck(id);
				}else{
					view.markForUncheck(id);
					view.removeExpenseToParticipate(id);
				}
			}
		});

		view.getMainList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				final ValueSelectable<HealthExpenseStub> selectable = (ValueSelectable<HealthExpenseStub>) event.getFirstSelected();
				
				if(selectable != null){
					//TODO
				}
			}
		});
		
		view.getSelectedList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
			
				@SuppressWarnings("unchecked")
				final ValueSelectable<HealthExpenseStub> selectable = (ValueSelectable<HealthExpenseStub>) event.getFirstSelected();
			
				if(selectable != null){
					//TODO
				}
			}
		});
		bound = true;
	}

	protected void participateToInsurer(
			Collection<ValueSelectable<HealthExpenseStub>> all) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();
		showMassParticipateToInsurerScreen();
	}
	

	private void clearView() {
		view.removeAllExpensesToParticipate();
		view.getExpenseForm().setValue(null);
	}	
	

	protected void onUserLacksPermission() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não tem permissões para realizar esta operação"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();		
	}

	private void showMassParticipateToInsurerScreen() {
		checkUserPermission(new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				view.allowCreation(true);
				view.getExpenseForm().setValue(null);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onUserLacksPermission();
			}		
				
		});
		
	}

	private void checkUserPermission(ResponseHandler<Boolean> responseHandler) {
		responseHandler.onResponse(true);
	}
	
}
