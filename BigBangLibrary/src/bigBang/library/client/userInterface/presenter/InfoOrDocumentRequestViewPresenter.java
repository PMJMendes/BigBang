package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class InfoOrDocumentRequestViewPresenter implements ViewPresenter {

	public static enum Action {
		SEND,
		CANCEL
	}
	
	public static interface Display{
		HasEditableValue<InfoOrDocumentRequest> getForm();
		
		//PERMISSIONS
		void clearAllowedPermissions();
		void allowSend(boolean allow);
		
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	protected Display view;
	protected boolean bound = false;
	
	public InfoOrDocumentRequestViewPresenter(Display view) {
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
		container.add(view.asWidget());
	}

	@Override
	public abstract void setParameters(HasParameters parameterHolder);

	protected void bind(){
		if(bound){return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<InfoOrDocumentRequestViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case SEND:
					onSend();
					break;
				case CANCEL:
					onCancel();
					break;
				}
			}
		});
		
		//APPLICATION-WIDE EVENTS
		bound = true;
	}
	
	protected void clearView(){
		view.clearAllowedPermissions();
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
	}
	
	protected abstract void onSend();
	
	protected abstract void onCancel();
	
	protected void onSendRequestFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível enviar o pedido de informação"), TYPE.ALERT_NOTIFICATION));
	}
	
	protected void onSendRequestSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Pedido de Informação enviado com Sucesso"), TYPE.TRAY_NOTIFICATION));
	}
	
}
