package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.dataAccess.UserBroker;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ExternalInfoRequest;
import bigBang.definitions.shared.ExternalInfoRequest.Outgoing;
import bigBang.definitions.shared.OutgoingMessage;
import bigBang.definitions.shared.User;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.OperationWasExecutedEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.interfaces.ContactsService;
import bigBang.library.interfaces.ContactsServiceAsync;
import bigBang.library.interfaces.ExternRequestService;
import bigBang.library.interfaces.ExternRequestServiceAsync;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class ExternalRequestReplyViewPresenter implements ViewPresenter {

	public static enum Action {
		SEND,
		CANCEL
	}
	
	public static interface Display {
		HasEditableValue<Outgoing> getForm();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		
		void setAvailableContacts(Contact[] contacts);
		void setUserNames(String[] usernames);
		
		Widget asWidget();
	}
	
	protected boolean bound = false;
	protected ExternRequestServiceAsync service;
	protected Display view;
	
	public ExternalRequestReplyViewPresenter(Display view) {
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
		String externalRequestId = parameterHolder.getParameter("externalrequestid");
		
		if(externalRequestId == null || externalRequestId.isEmpty()) {
			onFailure();
		}else{
			showReply(externalRequestId);
		}
	}
	
	protected void bind(){
		if(bound) {return;}
		
		view.registerActionHandler(new ActionInvokedEventHandler<ExternalRequestReplyViewPresenter.Action>() {
			
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
		
		bound = true;
	}

	protected void clearView(){
		this.view.getForm().setValue(null);
	}
	
	protected void showReply(String ownerId) {
		Outgoing message = new Outgoing();
		message.requestId = ownerId;
		view.getForm().setValue(message);
		
		ExternRequestService.Util.getInstance().getRequest(ownerId, new BigBangAsyncCallback<ExternalInfoRequest>() {

			@Override
			public void onResponseSuccess(final ExternalInfoRequest externalRequestResult) {
				ContactsServiceAsync contactsService = ContactsService.Util.getInstance();
				contactsService.getFlatEmails(externalRequestResult.parentDataObjectId, new BigBangAsyncCallback<Contact[]>() {

					@Override
					public void onResponseSuccess(Contact[] result) {
						view.setAvailableContacts(result);
						Outgoing request  = view.getForm().getInfo();
						OutgoingMessage message = request == null ? null : request.message;
						if(message != null) {
							message.toContactInfoId = externalRequestResult.fromInfoId;
							view.getForm().setInfo(request);
						}
					}
				});
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				ExternalRequestReplyViewPresenter.this.onFailure();
				super.onResponseFailure(caught);
			}
		});
		
		UserBroker userBroker = (UserBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.USER);
		userBroker.getUsers(new ResponseHandler<User[]>() {
			
			@Override
			public void onResponse(User[] response) {
				String[] usernames = new String[response.length];
				for(int i = 0; i < response.length; i++) {
					usernames[i] = response[i].name;
				}
				view.setUserNames(usernames);
			}
			
			@Override
			public void onError(Collection<ResponseError> errors) {
				return;
			}
		});
	}
	
	protected void onSend() {
		final Outgoing request = view.getForm().getInfo();
		service.sendInformation(request, new BigBangAsyncCallback<ExternalInfoRequest>() {

			@Override
			public void onResponseSuccess(ExternalInfoRequest result) {
				EventBus.getInstance().fireEvent(new OperationWasExecutedEvent(BigBangConstants.OperationIds.ExternalInfoRequest.REPLY, request.requestId));
				onSendSuccess();
			}
			
			@Override
			public void onResponseFailure(Throwable caught) {
				onSendFailed();
				super.onResponseFailure(caught);
			}
		});
	}
	
	protected void onCancel(){
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onSendSuccess() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "A Resposta foi enviada com Sucesso"), TYPE.TRAY_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}
	
	protected void onSendFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível enviar a Resposta"), TYPE.ALERT_NOTIFICATION));
	}
	
	protected void onFailure(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível enviar a Resposta"), TYPE.ALERT_NOTIFICATION));
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.removeParameter("show");
		NavigationHistoryManager.getInstance().go(item);
	}
	
}
