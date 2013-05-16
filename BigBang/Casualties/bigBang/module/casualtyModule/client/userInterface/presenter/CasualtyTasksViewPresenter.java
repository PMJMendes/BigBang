package bigBang.module.casualtyModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.CasualtyDataBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Casualty;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasOperationPermissions;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class CasualtyTasksViewPresenter implements ViewPresenter, HasOperationPermissions {

	public static enum Action {
		CLOSE,
		GO_TO_PROCESS
	}
	
	public static interface Display {
		HasValue<Casualty> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		//PERMISSIONS
		void clearAllowedPermissions();
		void allowClose(boolean allow);
		
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected CasualtyDataBroker broker;
	protected Display view;
	
	public CasualtyTasksViewPresenter(Display view){
		setView((UIObject) view);
		this.broker = (CasualtyDataBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CASUALTY); 
	}
	
	@Override
	public void setView(UIObject view) {
		this.view = (Display) view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(this.view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();
		
		String casualtyId = parameterHolder.getParameter("id");
		showCasualty(casualtyId);
	}
	
	protected void bind(){
		if(bound) {return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<CasualtyTasksViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch (action.getAction()) {
				case CLOSE:
					onClose();
					break;
				case GO_TO_PROCESS:
					NavigationHistoryManager.getInstance().NavigateToProcess(BigBangConstants.EntityIds.CASUALTY, view.getForm().getValue().id);
					break;
				default:
					break;
				}
			}
		});
		
		bound = true;
	}
	
	protected void clearView(){
		view.getForm().setValue(null);
		view.clearAllowedPermissions();
	}

	@Override
	public void setPermittedOperations(String[] operationIds) {
		view.clearAllowedPermissions();
		for(String opid : operationIds) {
			if(opid.equalsIgnoreCase(BigBangConstants.OperationIds.CasualtyProcess.CLOSE_CASUALTY)) {
				view.allowClose(true);
			}
		}
	}
	
	protected void showCasualty(String casualtyId) {
		broker.getCasualty(casualtyId, new ResponseHandler<Casualty>() {

			@Override
			public void onResponse(Casualty response) {
				view.getForm().setValue(response);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}
	
	//OPERATION ACTIONS
	
	protected void onClose(){
		broker.close(view.getForm().getValue().id, new ResponseHandler<Void>() {

			@Override
			public void onResponse(Void response) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Sinistro Encerrado com Sucesso"), TYPE.TRAY_NOTIFICATION));
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Encerrar o Sinistro"), TYPE.ALERT_NOTIFICATION));
			}
		});
	}

}
