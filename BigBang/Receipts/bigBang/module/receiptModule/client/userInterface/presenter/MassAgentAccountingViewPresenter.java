package bigBang.module.receiptModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ReceiptDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Receipt;
import bigBang.definitions.shared.ReceiptStub;
import bigBang.library.client.Checkable;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasCheckables;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.ValueSelectable;
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


public class MassAgentAccountingViewPresenter implements ViewPresenter{

	private Display view;
	private boolean bound = false;
	protected ReceiptDataBroker broker;

	public MassAgentAccountingViewPresenter(Display view){
		setView((UIObject) view);
		broker = (ReceiptDataBroker) DataBrokerManager.Util.getInstance().getBroker(BigBangConstants.EntityIds.RECEIPT);
	}


	public interface Display{

		void addReceiptToAccountingList(ReceiptStub value);
		void removeReceiptFromAccountingList(String id);
		HasCheckables getCheckableSelectedList();
		HasEditableValue<Receipt> getReceiptForm();

		HasValueSelectables<ReceiptStub> getMainList();
		HasValueSelectables<ReceiptStub> getSelectedList();
		HasCheckables getCheckableMainList();

		void refreshMainList();

		void markAllForCheck();
		void markForCheck(String id);
		void markForUncheck(String id);

		void removeAllReceiptsFromAccountingList();
		Widget asWidget();
		void registerActionHandler(
				ActionInvokedEventHandler<Action> actionInvokedEventHandler);
		void allowSend(boolean b);

	}

	public enum Action{
		SELECT_ALL, SEND_AGENT_ACCOUNTING, CLEAR

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
					view.removeAllReceiptsFromAccountingList();
					break;
				case SEND_AGENT_ACCOUNTING:
					sendAgentAccounting(view.getSelectedList().getAll());
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
				ValueSelectable<ReceiptStub> entry = (ValueSelectable<ReceiptStub>) checkable;
				String id = entry.getValue().id;

				if(checkable.isChecked()){
					view.markForCheck(id);
					view.addReceiptToAccountingList(entry.getValue());
				}else{
					view.markForUncheck(id);
					view.removeReceiptFromAccountingList(id);
				}

			}
		});

		view.getCheckableSelectedList().addCheckedSelectionChangedEventHandler(new CheckedSelectionChangedEventHandler() {

			@Override
			public void onCheckedSelectionChanged(CheckedSelectionChangedEvent event) {
				Checkable checkable = event.getChangedCheckable();

				@SuppressWarnings("unchecked")
				ValueSelectable<ReceiptStub> entry = (ValueSelectable<ReceiptStub>) checkable;
				String id = entry.getValue().id;

				if(checkable.isChecked()){
					view.markForCheck(id);
				}else{
					view.markForUncheck(id);
					view.removeReceiptFromAccountingList(id);
				}

			}
		});
		
		view.getMainList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				
				@SuppressWarnings("unchecked")
				ValueSelectable<ReceiptStub> selectable = (ValueSelectable<ReceiptStub>) event.getFirstSelected();
				
				if(selectable!= null){
					view.getSelectedList().clearSelection();
					broker.getReceipt(selectable.getValue().id, new ResponseHandler<Receipt>() {
						
						@Override
						public void onResponse(Receipt response) {
							view.getReceiptForm().setValue(response);
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Recibo seleccionado"), TYPE.ALERT_NOTIFICATION));
						}
					});
				}
				
			}
		});
		
		
		view.getSelectedList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				
				@SuppressWarnings("unchecked")
				ValueSelectable<ReceiptStub> selectable = (ValueSelectable<ReceiptStub>) event.getFirstSelected();
				
				if(selectable!= null){
					view.getMainList().clearSelection();
					broker.getReceipt(selectable.getValue().id, new ResponseHandler<Receipt>() {
						
						@Override
						public void onResponse(Receipt response) {
							view.getReceiptForm().setValue(response);
						}
						
						@Override
						public void onError(Collection<ResponseError> errors) {
							EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o Recibo seleccionado"), TYPE.ALERT_NOTIFICATION));
						}
					});
				}
				
			}
		});
		
		bound = true;
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();
		view.refreshMainList();
		showMassAgentAccountingScreen();
	}

	private void showMassAgentAccountingScreen() {
		checkUserPermission(new ResponseHandler<Boolean>() {

			@Override
			public void onResponse(Boolean response) {
				if(response){
					view.allowSend(true);
					view.getReceiptForm().setValue(null);
				}else{
					onUserLacksPermission();
				}

			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onUserLacksPermission();
			}
		});
	}

	protected void onUserLacksPermission() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não tem permissões para realizar esta operação"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}

	private void clearView() {
		view.getMainList().clearSelection();
		view.removeAllReceiptsFromAccountingList();
		view.getReceiptForm().setValue(null);
	}

	public void sendAgentAccounting(Collection<ValueSelectable<ReceiptStub>> collection){
		String[] receiptIds = new String[collection.size()];

		int i = 0;
		for(ValueSelectable<ReceiptStub> r : collection){
			receiptIds[i] = r.getValue().id;
			i++;
		}

		broker.agentAccounting(receiptIds, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				onAgentAccountingSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onAgentAccountingFailed();
			}
		});
	}

	protected void checkUserPermission(ResponseHandler<Boolean> handler) {
		handler.onResponse(true); //TODO
	}

	protected void onAgentAccountingSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "As Prestações de Contas foram Enviadas com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryManager.getInstance().reload();
	}
	
	protected void onAgentAccountingFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Enviar as Prestações de Contas"), TYPE.ALERT_NOTIFICATION));
	}

}
