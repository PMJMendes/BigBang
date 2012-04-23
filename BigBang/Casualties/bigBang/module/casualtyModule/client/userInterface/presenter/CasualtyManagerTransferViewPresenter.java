package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Casualty;
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

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CasualtyManagerTransferViewPresenter implements ViewPresenter {

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
	private CasualtyDataBroker broker;
	private boolean bound =  false;
	private String currentCasualtyId;
	
	public CasualtyManagerTransferViewPresenter(Display view){
		this.broker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY);
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
		this.currentCasualtyId = parameterHolder.getParameter("casualtyid");
		this.currentCasualtyId = this.currentCasualtyId == null ? new String() : this.currentCasualtyId;
		
		if(this.currentCasualtyId.isEmpty()){
			clearView();
		}else{
			showManagerTransfer(this.currentCasualtyId);
		}
	}
	
	private void bind(){
		if(bound){return;}
		
		view.registerEventHandler(new ActionInvokedEventHandler<CasualtyManagerTransferViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case TRANSFER:
					transferCasualty(CasualtyManagerTransferViewPresenter.this.currentCasualtyId, view.getForm().getInfo());
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
	
	private void showManagerTransfer(String casualtyId){
		broker.getCasualty(casualtyId, new ResponseHandler<Casualty>() {
			
			@Override
			public void onResponse(Casualty response) {
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
	
	private void transferCasualty(String casualtyId, String newManagerId){
		this.broker.createManagerTransfer(new String[]{casualtyId}, newManagerId, new ResponseHandler<ManagerTransfer>() {

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
