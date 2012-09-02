package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest.Cancellation;
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
import bigBang.library.interfaces.InfoOrDocumentRequestService;
import bigBang.library.interfaces.InfoOrDocumentRequestServiceAsync;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class CancelInfoOrDocumentRequestViewPresenter implements ViewPresenter {

	public static enum Action {
		CONFIRM,
		CANCEL
	}

	public static interface Display {
		HasEditableValue<InfoOrDocumentRequest.Cancellation> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	private boolean bound = false;
	private InfoOrDocumentRequestServiceAsync service;
	private Display view;

	public CancelInfoOrDocumentRequestViewPresenter(Display view){
		setView((UIObject)view);
		service = InfoOrDocumentRequestService.Util.getInstance();
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
		String requestId = parameterHolder.getParameter("requestid");
		if(requestId == null || requestId.isEmpty()) {
			onFailure();
		}else{
			showCancellation(requestId);
		}
	}

	private void bind(){
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<CancelInfoOrDocumentRequestViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case CONFIRM:
					onConfirm();
					break;
				case CANCEL:
					onCancel();
					break;
				}
			}
		});

		bound = true;
	}

	private void showCancellation(String requestId){
		Cancellation cancellation = new Cancellation();
		cancellation.requestId = requestId;
		view.getForm().setValue(cancellation);
	}

	private void onConfirm(){
		if(view.getForm().validate()) {
			final Cancellation toCancel = view.getForm().getInfo();
			service.cancelRequest(toCancel, new BigBangAsyncCallback<Void>() {

				@Override
				public void onResponseSuccess(Void result) {
					EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InfoOrDocumentRequest.CANCEL_REQUEST, toCancel.requestId));
					onCancellationSuccess();
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					onCancellationFailed();
					super.onResponseFailure(caught);
				}
			});
		}
	}

	private void onCancel(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onCancellationFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível cancelar o pedido de informação"), TYPE.ALERT_NOTIFICATION));
	}

	private void onCancellationSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Pedido de Informação cancelado com sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		item.popFromStackParameter("display");
		item.removeParameter("requestid");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível cancelar o pedido de informação"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

}
