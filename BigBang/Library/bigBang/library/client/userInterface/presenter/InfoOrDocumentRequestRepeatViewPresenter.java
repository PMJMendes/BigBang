package bigBang.library.client.userInterface.presenter;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.InfoOrDocumentRequest;
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
import bigBang.library.interfaces.ContactsService;
import bigBang.library.interfaces.InfoOrDocumentRequestService;
import bigBang.library.interfaces.InfoOrDocumentRequestServiceAsync;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class InfoOrDocumentRequestRepeatViewPresenter implements ViewPresenter {

	public static enum Action {
		SEND,
		CANCEL
	}

	public static interface Display {
		HasEditableValue<InfoOrDocumentRequest> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		void setAvailableContacts(Contact[] contact);

		Widget asWidget();
	}

	private boolean bound = false;
	private InfoOrDocumentRequestServiceAsync service;
	private Display view;

	public InfoOrDocumentRequestRepeatViewPresenter(Display view) {
		setView((UIObject) view);
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
		container.add(view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {
		String requestId = parameterHolder.getParameter("requestid");

		clearView();

		if(requestId == null || requestId.isEmpty()) {
			onFailure();
		}else{
			showRepeatRequest(requestId);
		}
	}

	protected void bind(){
		if(bound) {return;}

		view.registerActionHandler(new ActionInvokedEventHandler<InfoOrDocumentRequestRepeatViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()) {
				case SEND:
					onSend();
					break;
				case CANCEL:
					onCancel();
					break;
				}
			}
		});

		bound = true;
	}

	protected void clearView(){
		view.getForm().setValue(null);
	}

	protected void showRepeatRequest(String requestId) {
		service.getRequest(requestId, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

			@Override
			public void onResponseSuccess(final InfoOrDocumentRequest request) {
				ContactsService.Util.getInstance().getFlatEmails(request.parentDataObjectId, new BigBangAsyncCallback<Contact[]>() {

					@Override
					public void onResponseSuccess(Contact[] result) {
						view.setAvailableContacts(result);
						request.replylimit = 0;
						view.getForm().setValue(request);
					}
				});
			}

			@Override
			public void onResponseFailure(Throwable caught) {
				InfoOrDocumentRequestRepeatViewPresenter.this.onFailure();
				super.onResponseFailure(caught);
			}
		});
	}

	protected void onSend(){
		if(view.getForm().validate()) {
			final InfoOrDocumentRequest request = view.getForm().getInfo();
			service.repeatRequest(request, new BigBangAsyncCallback<InfoOrDocumentRequest>() {

				@Override
				public void onResponseSuccess(InfoOrDocumentRequest result) {
					EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.InfoOrDocumentRequest.REPEAT_REQUEST, request.id));
					onSendSuccess();
				}

				@Override
				public void onResponseFailure(Throwable caught) {
					onSendFailed();
					super.onResponseFailure(caught);
				}
			});
		}else{
			EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preenchimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
		}
	}

	protected void onCancel(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onSendFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível Repetir o Pedido"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onSendSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Repetição do Pedido foi enviada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível Repetir o Pedido"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}

}
