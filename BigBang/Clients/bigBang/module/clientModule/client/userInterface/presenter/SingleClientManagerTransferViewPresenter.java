package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Client;
import bigBang.definitions.shared.ManagerTransfer;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.Session;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class SingleClientManagerTransferViewPresenter implements ViewPresenter {

	public static enum Action {
		TRANSFER,
		CANCEL
	} 
	
	public static interface Display {
		HasEditableValue<String> getForm();
		
		void allowTransfer(boolean allow);
		
		void registerEventHandler(ActionInvokedEventHandler<Action> action);
		Widget asWidget();
	}

	private Display view;
	private ClientProcessBroker broker;
	private boolean bound =  false;
	private String currentClientId;
	
	public SingleClientManagerTransferViewPresenter(Display view){
		this.broker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
		setView((UIObject)view);
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

	@Override
	public void setParameters(HasParameters parameterHolder) {
		this.currentClientId = parameterHolder.getParameter("clientid");
		this.currentClientId = this.currentClientId == null ? new String() : this.currentClientId;
		
		if(this.currentClientId.isEmpty()){
			clearView();
		}else{
			showManagerTransfer(this.currentClientId);
		}
	}
	
	private void bind(){
		if(bound){return;}
		
		view.registerEventHandler(new ActionInvokedEventHandler<SingleClientManagerTransferViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case TRANSFER:
					transferClient(SingleClientManagerTransferViewPresenter.this.currentClientId, view.getForm().getInfo());
					break;
				case CANCEL:
					onManagerTransferCancelled();
					break;
				}
			}
		});
		
		//APPLICATION-WIDE EVENTS
		
		bound = true;
	}
	
	private void clearView(){
		view.allowTransfer(false);
		view.getForm().setValue(null);
	}
	
	private void showManagerTransfer(String clientId){
		broker.getClient(clientId, new ResponseHandler<Client>() {
			
			@Override
			public void onResponse(Client response) {
				//TODO check permissions FJVC
				view.getForm().setValue(Session.getUserId());
				view.getForm().setReadOnly(false);
				view.allowTransfer(true);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetClientFailed();
			}
		});
	}
	
	private void transferClient(String clientId, String newManagerId){
		this.broker.createManagerTransfer(new String[]{clientId}, newManagerId, new ResponseHandler<ManagerTransfer>() {

			@Override
			public void onResponse(ManagerTransfer response) {
				onManagerTransferSuccess();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onManagerTransferFailed();
			}
		});
	}
	
	private void onManagerTransferSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Transferência de Gestor criada com sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onGetClientFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não é possível criar a Transferência de Gestor"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	} 
	
	private void onManagerTransferFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "De momento não é possível criar a Transferência de Gestor"), TYPE.ALERT_NOTIFICATION));
	}
	
	private void onManagerTransferCancelled(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

}
