package bigBang.module.clientModule.client.userInterface.presenter;

import java.util.Collection;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.dataAccess.ClientProcessBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.presenter.ViewPresenter;

public class DeleteClientViewPresenter implements ViewPresenter {

	public static enum Action{
		DELETE,
		CANCEL
	}
	
	public static interface Display{
		HasEditableValue<String> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}
	
	protected ClientProcessBroker broker;
	protected Display view;
	protected boolean bound = false;
	private String clientId;
	
	public DeleteClientViewPresenter(Display view){
		this.broker = (ClientProcessBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CLIENT);
		setView((UIObject)view);
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

	private void bind() {
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<DeleteClientViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case DELETE:
					onDelete();
					break;
				case CANCEL:
					onCancel();
					break;
				}				
			}
		});

		bound = true;
	}

	protected void onCancel() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onDelete() {
		broker.removeClient(clientId, view.getForm().getValue(), new ResponseHandler<String>() {
			
			@Override
			public void onResponse(String response) {
				onDeleteSuccess();
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				onDeleteFailed();
			}
		});
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		clearView();

		clientId = parameterHolder.getParameter("clientid");
		if(clientId == null || clientId.isEmpty()){
			NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
			item.removeParameter("show");
			NavigationHistoryManager.getInstance().go(item);
		}
	}

	private void clearView() {
		view.getForm().setValue(null);		
	}

	protected void onDeleteFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível eliminar o Cliente"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onDeleteSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Cliente eliminado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		item.removeParameter("clientid");
		NavigationHistoryManager.getInstance().go(item);
	}
	

}
