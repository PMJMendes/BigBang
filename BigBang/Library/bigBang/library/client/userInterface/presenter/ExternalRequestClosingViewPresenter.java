package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ExternalInfoRequest.Closing;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.interfaces.ExternRequestService;
import bigBang.library.interfaces.ExternRequestServiceAsync;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ExternalRequestClosingViewPresenter implements ViewPresenter {

	public static enum Action {
		CLOSE,
		CANCEL
	}
	
	public static interface Display {
		HasEditableValue<Closing> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected ExternRequestServiceAsync service;
	protected Display view;
	
	public ExternalRequestClosingViewPresenter(Display view) {
		setView((UIObject) view);
		service = ExternRequestService.Util.getInstance();
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
		String requestId = parameterHolder.getParameter("externalrequestid");
		
		clearView();
		
		if(requestId == null || requestId.isEmpty()) {
			onFailure();
		}else{
			showClose(requestId);
		}
	}
	
	protected void bind(){
		if(bound) {return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<ExternalRequestClosingViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case CLOSE:
					onClose();
					break;
				case CANCEL:
					onCancel();
				}
			}
		});
		
		bound = true;
	}

	protected void clearView(){
		view.getForm().setValue(null);
	}
	
	protected void showClose(String requestId) {
		Closing closing = new Closing();
		closing.requestId = requestId;
		view.getForm().setValue(closing);
	}
	
	protected void onClose(){
		final Closing closing = view.getForm().getInfo();
		service.closeRequest(closing, new BigBangAsyncCallback<Void>() {

			@Override
			public void onResponseSuccess(Void result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExternalInfoRequest.CLOSE, closing.requestId));
				onCloseSuccess();
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				onCloseFailed();
				super.onResponseFailure(caught);
			}
		});
	}
	
	protected void onCancel(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onCloseSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Pedido foi Encerrado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		item.removeParameter("externalrequestid");
		item.popFromStackParameter("display");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onCloseFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Encerrar o Pedido"), TYPE.ALERT_NOTIFICATION));
	}
	
	protected void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível Encerrar o Pedido"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}
	
}
