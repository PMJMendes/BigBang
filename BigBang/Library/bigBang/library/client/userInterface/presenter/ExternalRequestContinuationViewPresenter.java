package bigBang.library.client.userInterface.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.ExternalInfoRequest.Incoming;
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

public class ExternalRequestContinuationViewPresenter implements ViewPresenter {

	public static enum Action {
		CONFIRM,
		CANCEL
	}

	public static interface Display {
		HasEditableValue<Incoming> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);

		Widget asWidget();
	}

	protected boolean bound = false;
	protected Display view;
	protected ExternRequestServiceAsync service;

	public ExternalRequestContinuationViewPresenter(Display view) {
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

		if(requestId == null || requestId.isEmpty()) {
			onFailure();
		} else {
			showContinueRequest(requestId);
		}
	}

	protected void bind() {
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<ExternalRequestContinuationViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CANCEL:
					onCancel();
					break;
				case CONFIRM:
					onConfirm();
					break;
				}
			}
		});

		bound = true;
	}

	protected void showContinueRequest(String ownerId){
		Incoming incoming = new Incoming();
		incoming.requestId = ownerId;
		view.getForm().setValue(incoming);
	}

	protected void clearView() {
		view.getForm().setValue(null);
	}

	protected void onCancel(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onConfirm(){
		if(view.getForm().validate()) {
			final Incoming incoming = view.getForm().getInfo();
			service.receiveAdditional(incoming, new BigBangAsyncCallback<ExternalInfoRequest>() {

				@Override
				public void onResponseSuccess(ExternalInfoRequest result) {
					EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExternalInfoRequest.CONTINUE, incoming.requestId));
					onContinuationSuccess();
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					onContinuationFailed();
					super.onResponseFailure(caught);
				}

			});
		}
	}

	protected void onContinuationSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "O Pedido foi Continuado com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onContinuationFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível efectuar a Continuação do Pedido"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível efectuar a Continuação do Pedido"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

}
