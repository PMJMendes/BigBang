package bigBang.module.expenseModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ExpenseDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.DocuShareHandle;
import bigBang.definitions.shared.ExpenseStub;
import bigBang.library.client.Checkable;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasCheckables;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.CheckedSelectionChangedEvent;
import bigBang.library.client.event.CheckedSelectionChangedEventHandler;
import bigBang.library.client.event.NavigationStateChangedEvent;
import bigBang.library.client.event.NavigationStateChangedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.ImageHandlerPanel;
import bigBang.library.client.userInterface.NavigationPanel;
import bigBang.library.client.userInterface.presenter.ViewPresenter;
import bigBang.library.shared.DocuShareItem;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ProofReceptionViewPresenter implements ViewPresenter {

	public static enum Action {
		SELECT_ALL, RECEIVE_PROOF, CLEAR
		
	}
	
	public static interface Display {
		Widget asWidget();

		void allowConfirm(boolean b);

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		void removeAllExpensesFromSelection();

		void markForUncheck(String id);

		void markForCheck(String id);

		void markAllForCheck();

		void refreshMainList();

		HasCheckables getCheckableMainList();

		HasValueSelectables<ExpenseStub> getSelectedList();

		HasValueSelectables<ExpenseStub> getMainList();

		HasCheckables getCheckableSelectedList();

		void removeExpenseFromSelection(String id);

		void addExpenseToSelection(ExpenseStub stub);
		
		DocuShareItem getCurrentItem();

		DocuShareItem getLocationItem();

		NavigationPanel getNavigationPanel();
	}
	
	private Display view;
	private boolean bound = false;
	protected ExpenseDataBroker broker;
	
	
	public ProofReceptionViewPresenter(Display view) {
		setView((UIObject) view);
		broker = (ExpenseDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.EXPENSE);
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
					view.removeAllExpensesFromSelection();
					break;
				case RECEIVE_PROOF:
					receiveProof(view.getSelectedList().getAll(), view.getCurrentItem(), view.getLocationItem());
					break;
				case SELECT_ALL:
					view.markAllForCheck();
					break;
				}
			}
		});

		view.getNavigationPanel().registerNavigationStateChangedHandler(new NavigationStateChangedEventHandler() {

			@Override
			public void onNavigationStateChanged(NavigationStateChangedEvent event) {
				if(event.getObject() instanceof ImageHandlerPanel){
					view.allowConfirm(true);
				}
				else{
					view.allowConfirm(false);
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
					view.addExpenseToSelection(entry.getValue());
				}else{
					view.markForUncheck(id);
					view.removeExpenseFromSelection(id);
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
					view.removeExpenseFromSelection(id);
				}
			}
		});

		bound = true;
	}
	
	protected void receiveProof(
			Collection<ValueSelectable<ExpenseStub>> all, DocuShareItem item, DocuShareItem locationItem) {

		String[] toReceive = new String[all.size()];
		
		if(toReceive.length == 0){
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Nenhuma despesa seleccionada"), TYPE.ALERT_NOTIFICATION));				
			return;
		}
		
		int counter = 0;

		for(ValueSelectable<ExpenseStub> stub: all){
			toReceive[counter] = stub.getValue().id;
			counter++;
		}

		DocuShareHandle handle = new DocuShareHandle();
		handle.handle = item.handle;
		handle.locationHandle = locationItem == null ? null : locationItem.handle;
		
		broker.massReceiveProof(toReceive, handle, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Recepção marcada com sucesso"), TYPE.TRAY_NOTIFICATION));
				NavigationHistoryManager.getInstance().reload();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível marcar a recepção"), TYPE.ALERT_NOTIFICATION));
			}
		});

	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();
		view.refreshMainList();
		showMassParticipateToInsurerScreen();
	}


	private void clearView() {
		view.removeAllExpensesFromSelection();
		view.allowConfirm(false);
	}	


	protected void onUserLacksPermission() {
//		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não tem permissões para realizar esta operação"), TYPE.ALERT_NOTIFICATION));
	}

	private void showMassParticipateToInsurerScreen() {
		checkUserPermission(new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				view.allowConfirm(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onUserLacksPermission();
			}		

		});

	}

	private void checkUserPermission(final ResponseHandler<Boolean> responseHandler) {
		PermissionChecker.hasGeneralPermission(BigBangConstants.EntityIds.EXPENSE, BigBangConstants.OperationIds.ExpenseProcess.RECEIVE_PROOF, new ResponseHandler<Boolean>() {

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
