package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.InfoOrDocumentRequest;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.BigBangAsyncCallback;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.interfaces.ContactsService;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class InfoOrDocumentRequestViewPresenter<T extends ProcessBase> implements ViewPresenter {

	public static enum Action {
		SEND,
		CANCEL, 
		ON_CLICK_BACK
	}
	
	public static interface Display<T extends ProcessBase>{
		HasEditableValue<InfoOrDocumentRequest> getForm();
		HasValue<T> getOwnerForm();
		
		void setAvailableContacts(Contact[] contacts);
		
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		Widget asWidget();
	}

	protected Display<T> view;
	protected boolean bound = false;
	
	public InfoOrDocumentRequestViewPresenter(Display<T> view) {
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
		
		String ownerId = parameterHolder.getParameter("ownerid");
		ownerId = ownerId == null ? new String() : ownerId;
		String ownerTypeId = parameterHolder.getParameter("ownerTypeId");
		ownerTypeId = ownerTypeId == null ? new String() : ownerTypeId;
		
		clearView();
		
		if(ownerId.isEmpty() || ownerTypeId.isEmpty()){
			onGetOwnerFailed();
		}else {
			showCreateRequest(ownerId, ownerTypeId);
		}
	}

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
				case ON_CLICK_BACK:
					onCancel();
					break;
				}
			}
		});
		
		//APPLICATION-WIDE EVENTS
		bound = true;
	}

	protected void clearView(){
		view.getOwnerForm().setValue(null);
		view.getForm().setValue(null);
		view.getForm().setReadOnly(true);
		view.setAvailableContacts(null);
	}

	protected void showCreateRequest(final String ownerId, final String ownerTypeId){
		checkOwnerPermissions(ownerId, ownerTypeId, new ResponseHandler<Boolean>(){

			@Override
			public void onResponse(Boolean response) {
				if(response){
					showOwner(ownerId, ownerTypeId);
					setContactsForOwner(ownerId);
					InfoOrDocumentRequest request = getFormattedRequest(ownerId, ownerTypeId);
					view.getForm().setValue(request);
					view.getForm().setReadOnly(false);
				}else{
					onUserLacksPermission();
				}
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onResponse(false);
			}
		});
	}

	protected void setContactsForOwner(String ownerId) {
		ContactsService.Util.getInstance().getFlatEmails(ownerId, new BigBangAsyncCallback<Contact[]>() {

			@Override
			public void onResponseSuccess(Contact[] result) {
				view.setAvailableContacts(result);
			}
		});
	}
	
	protected abstract InfoOrDocumentRequest getFormattedRequest(String ownerId, String ownerTypeId);
	
	protected abstract void showOwner(String ownerId, String ownerTypeId);
	
	protected abstract void checkOwnerPermissions(String ownerId, String ownerTypeId, ResponseHandler<Boolean> handler);
	
	protected abstract void onSend();

	protected abstract void onCancel();

	protected void onSendRequestFailed(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível enviar o Pedido de Informação"), TYPE.ALERT_NOTIFICATION));
	}

	protected void onSendRequestSuccess(){
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Pedido de Informação enviado com Sucesso"), TYPE.TRAY_NOTIFICATION));
	}
	
	protected abstract void onGetOwnerFailed();

	protected abstract void onUserLacksPermission();
	
	protected abstract void onGenericError();

}
