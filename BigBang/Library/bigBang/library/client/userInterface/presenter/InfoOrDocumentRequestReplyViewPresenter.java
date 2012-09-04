package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.InfoOrDocumentRequest.Response;
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

public class InfoOrDocumentRequestReplyViewPresenter implements ViewPresenter {

	public static enum Action {
		SAVE,
		CANCEL
	}

	public static interface Display {
		HasEditableValue<Response> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void setSaveModeEnabled(boolean enabled);

		Widget asWidget();
	}

	private boolean bound = false;
	private InfoOrDocumentRequestServiceAsync service;
	private Display view;

	public InfoOrDocumentRequestReplyViewPresenter(Display view){
		setView((UIObject)view);
		this.service = InfoOrDocumentRequestService.Util.getInstance();
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
		view.setSaveModeEnabled(true);

		if(requestId == null || requestId.isEmpty()) {
			onFailure();
		}else{
			showReply(requestId);
		}
	}

	private void bind(){
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<InfoOrDocumentRequestReplyViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case SAVE:
					onSave();
					break;
				case CANCEL:
					onCancel();
					break;
				}
			}
		});

		bound = true;
	}

	private void showReply(String requestId){
		Response response = new Response();
		response.requestId = requestId;
		view.getForm().setValue(response);
	}

	private void onSave(){
		if(view.getForm().validate()) {
			service.receiveResponse(view.getForm().getInfo(), new BigBangAsyncCallback<InfoOrDocumentRequest>() {

				@Override
				public void onResponseSuccess(InfoOrDocumentRequest result) {
					EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InfoOrDocumentRequest.RECEIVE_REPLY,  result.id));
					onReplySuccess();
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					onReplyFailed();
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

	private void onReplySuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Resposta foi recebida com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	private void onReplyFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível receber a resposta"), TYPE.ALERT_NOTIFICATION));
	}

	private void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível receber a resposta"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}
}
