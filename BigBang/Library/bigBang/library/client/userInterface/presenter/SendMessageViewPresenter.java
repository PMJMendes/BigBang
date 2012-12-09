package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class SendMessageViewPresenter<T extends ProcessBase> implements ViewPresenter{

	public static enum Action {
		SEND,
		CANCEL, 
		ON_CLICK_BACK
	}

	public static interface Display<T extends ProcessBase>{
		HasEditableValue<Conversation> getForm();
		HasValue<T> getOwnerForm();

		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
		HasValue<String> getTo();
		void addContact(String name, String id, String ownerTypeId);
		void clearEmails();
		void setAvailableContacts(Contact[] contacts);
		void lockCreateContact(boolean b);
		void setSelectedContact(String id);
		void addDocuments(Collection<Document> response);
		void addDocument(Document document);
		void setTypeAndOwnerId(String ownerTypeId, String ownerId);
	}

	protected Display<T> view;
	protected boolean bound = false;
	private String ownerId;
	private String ownerTypeId;

	public SendMessageViewPresenter(Display<T> view) {
		setView((UIObject)view);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setView(UIObject view) {
		this.view = (Display<T>)view;
	}

	@Override
	public void go(HasWidgets container) {
		bind();
		container.clear();
		container.add(view.asWidget());
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		ownerId = parameterHolder.getParameter("ownerid");
		ownerId = ownerId == null ? new String() : ownerId;
		ownerTypeId = parameterHolder.getParameter("ownerTypeId");
		ownerTypeId = ownerTypeId == null ? new String() : ownerTypeId;

		view.setTypeAndOwnerId(ownerTypeId, ownerId);
		
		clearView();

		if(ownerId.isEmpty() || ownerTypeId.isEmpty()){
			onGetOwnerFailed();
		}else {
			showCreateRequest(ownerId, ownerTypeId);
		}
	}

	protected void bind(){
		if(bound){return;}

		view.registerActionHandler(new ActionInvokedEventHandler<SendMessageViewPresenter.Action>() {

			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case SEND:
					onSend();
					break;
				case CANCEL:
					onCancel();
					break;
				case ON_CLICK_BACK:
					onCancel();
					break;
				}
			}
		});

		bound = true;
	}


	protected void clearView(){
		view.getOwnerForm().setValue(null);
		view.getForm().setValue(null);
	}

	protected void showCreateRequest(final String ownerId, final String ownerTypeId){
		fillOwner(ownerId, new ResponseHandler<T>(){

			@Override
			public void onResponse(T response) {
					view.getOwnerForm().setValue(response);
					Conversation request = getFormattedRequest(ownerId, ownerTypeId);
					view.getForm().setValue(request);
					view.getForm().setReadOnly(false);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetOwnerFailed();
			}
		});
	}

	protected Conversation getFormattedRequest(String ownerId,
			String ownerTypeId) {
		Conversation request = new Conversation();
		request.parentDataObjectId = ownerId;
		return request;
	}
	protected abstract void fillOwner(String ownerId, ResponseHandler<T> responseHandler);

	protected void onSend() {
		if(view.getForm().validate()) {
			send();
		}else{
			onFormValidationFailed();
		}
	}
	
	protected abstract void send();
		
	protected void onCancel() {
		navigateBack();
	}

	protected void onSendRequestFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível enviar a mensagem"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onSendRequestSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mensagem enviada com sucesso"), TYPE.TRAY_NOTIFICATION));
	}

	protected void onUserLacksPermission() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não tem permissões para enviar mensagem"), TYPE.ALERT_NOTIFICATION));
		navigateBack();

	}

	protected void onGetOwnerFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o processo pai"), TYPE.ALERT_NOTIFICATION));
		navigateBack();

	}

	protected void onFormValidationFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
	}

	protected void onGenericError() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Ocorreu um erro ao apresentar a mensagem"), TYPE.ALERT_NOTIFICATION));
		navigateBack();
	}

	protected void onGetContactsFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter os contactos do processo."), TYPE.ALERT_NOTIFICATION));
		navigateBack();
	}

	public void navigateBack() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("ownerid");
		item.removeParameter("ownertypeid");
		item.removeParameter("requestid");
		NavigationHistoryManager.getInstance().go(item);		
	}
}
