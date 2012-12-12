package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.Selectable;
import bigBang.library.client.dataAccess.ConversationBroker;
import bigBang.library.client.dataAccess.ConversationBrokerClient;
import bigBang.library.client.dataAccess.DataBrokerManager;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.NewNotificationEvent;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.MessagesList;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class ConversationViewPresenter<T extends ProcessBase> implements ViewPresenter, ConversationBrokerClient {

	public static enum Action{
		SAVE,
		SEND_MESSAGE,
		RECEIVE_MESSAGE,
		TOOLBAR_CANCEL,
		TOOLBAR_SEND,
		CLICK_BACK, TOOLBAR_EDIT,
		TOOLBAR_REPEAT,
		TOOLBAR_CLOSE, 
		TOOLBAR_RECEIVE, 
		TOOLBAR_SAVE, CONVERSATION_BUTTON_CLICKED, FORM_CANCEL
	}

	public static interface Display<T extends ProcessBase>{
		HasEditableValue<Conversation> getForm();
		HasValue<T> getOwnerForm();

		Widget asWidget();
		HasEditableValue<Conversation> getSendMessageForm();
		HasEditableValue<Conversation> getReceiveMessageForm();
		void closeSubForms();
		void clearEmails();
		void registerActionHandler(ActionInvokedEventHandler<Action> handler);
		HasValueSelectables<Message> getMessageList();
		void setMessage(Message source);
		void setFormVisible(boolean isSendMessage);
		void addContact(String string, String mediatorId, String mediator);
		void lockAllMainToolbar();
		void allowEdit(boolean hasPermission);
		void allowSendMessage(boolean hasPermission);
		void allowRepeatMessage(boolean hasPermission);
		void allowReceiveMessage(boolean hasPermission);
		void allowClose(boolean hasPermission);
		void setOwner(Conversation convers);
		void setHistoryOwner(Conversation conversation);
		List<HistoryItemStub> getHistoryList();
	}

	public ConversationViewPresenter(Display<T> view){
		setView((UIObject)view);
		broker = (ConversationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CONVERSATION);

	}

	protected Display<T> view;
	protected boolean bound = false;
	private String ownerId;
	private String ownerTypeId;
	private String conversationId;
	protected ConversationBroker broker;
	private boolean isSendMessage;
	protected Message currentMessage;
	protected Conversation conversation;
	private boolean toSend;

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

	private void bind() {
		if(bound){return;}

		view.registerActionHandler(new ActionInvokedEventHandler<ConversationViewPresenter.Action>() {
			
			@Override
			public void onActionInvoked(ActionInvokedEvent<Action> action) {
				switch(action.getAction()){
				case TOOLBAR_CLOSE:
					onClickClose();
					break;
				case TOOLBAR_RECEIVE:
					onClickReceive();
					break;
				case TOOLBAR_REPEAT:
					onClickRepeat();
					break;
				case TOOLBAR_SAVE:
					onSave();
					break;
				case TOOLBAR_SEND:
					onClickSend();
					break;
				case RECEIVE_MESSAGE:
					onReceiveMessage();
					break;
				case SAVE:
					onSave();
					break;
				case SEND_MESSAGE:
					if(toSend){
						onSendMessage();
					}
					else{
						onRepeatMessage();
					}
					break;
				case CLICK_BACK:
					navigateBack();
					break;
				case TOOLBAR_CANCEL:
					onClickCancel();
					break;
				case TOOLBAR_EDIT:
					onClickEdit();
					break;
				case CONVERSATION_BUTTON_CLICKED:
					onConversationButtonClicked();
					break;
				case FORM_CANCEL:
					resetView();
					break;
				}

			}
		});

		view.getMessageList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				resetView();
				if(event.getSelected() != null && event.getSelected().size() > 0){

					Message m = new Message();

					for(Selectable sel : event.getSelected()){
						m = ((MessagesList.Entry)sel).getValue();
					}
					currentMessage = m;
					view.setMessage(m);
					if(m.direction.equals(Conversation.Direction.INCOMING)){
						view.allowRepeatMessage(false);
					}
				}
			}
		});

		view.getHistoryList().addSelectionChangedEventHandler(new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				@SuppressWarnings("unchecked")
				ValueSelectable<HistoryItemStub> selected = (ValueSelectable<HistoryItemStub>) event.getFirstSelected();
				HistoryItemStub item = selected == null ? null : selected.getValue();
				String itemId = item == null ? null : item.id;
				itemId = itemId == null ? new String() : itemId;

				if(!itemId.isEmpty()){
					NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
					navItem.pushIntoStackParameter("display", "history");
					navItem.setParameter("historyownerid", view.getForm().getValue().id);
					navItem.setParameter("historyItemId", itemId);
					NavigationHistoryManager.getInstance().go(navItem);
				}
			}
		});

		bound = true;
	}

	protected void resetView(){
		view.closeSubForms();
		setPermissions();
	}

	protected void onConversationButtonClicked() {
		view.getMessageList().clearSelection();		
	}

	protected void onClickEdit() {
		view.getForm().setReadOnly(false);
	}

	protected void onClickCancel() {
		view.getForm().revert();
		view.getForm().setReadOnly(true);
	}

	protected void onSave() {
		if(view.getForm().validate()){
			broker.saveConversation(view.getForm().getInfo(), new ResponseHandler<Conversation>() {

				@Override
				public void onResponse(Conversation response) {
					view.setHistoryOwner(conversation);
					view.getForm().setValue(response);
					view.getForm().setReadOnly(true);
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Alterações gravadas com sucesso"), TYPE.TRAY_NOTIFICATION));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar as alterações."), TYPE.ALERT_NOTIFICATION));					
				}
			});
		}
		else{
			onFormValidationFailed();
		}
	}
	protected void onSendMessage() {
		if(view.getSendMessageForm().validate()){
			broker.sendMessage(view.getSendMessageForm().getInfo().messages[0], view.getSendMessageForm().getInfo().replylimit, new ResponseHandler<Conversation>() {

				@Override
				public void onResponse(Conversation response) {
					view.setHistoryOwner(conversation);
					resetView();
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mensagem enviada com sucesso"), TYPE.TRAY_NOTIFICATION));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível enviar a mensagem."), TYPE.ALERT_NOTIFICATION));					

				}
			});
		}
		else{
			onFormValidationFailed();
		}
	}
	protected void onRepeatMessage() {
		if(view.getSendMessageForm().validate()){
			broker.repeatMessage(view.getSendMessageForm().getInfo().messages[0], view.getSendMessageForm().getInfo().replylimit, new ResponseHandler<Conversation>() {

				@Override
				public void onResponse(Conversation response) {
					view.setHistoryOwner(conversation);
					resetView();
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mensagem enviada com sucesso"), TYPE.TRAY_NOTIFICATION));
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível repetir a mensagem."), TYPE.ALERT_NOTIFICATION));					
				}
			});
		}
		else{
			onFormValidationFailed();
		}
	}
	protected void onReceiveMessage() {
		if(view.getReceiveMessageForm().validate()){
			broker.receiveMessage(view.getReceiveMessageForm().getInfo().messages[0], view.getReceiveMessageForm().getInfo().replylimit, new ResponseHandler<Conversation>() {

				@Override
				public void onResponse(Conversation response) {
					view.setHistoryOwner(conversation);
					resetView();
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mensagem recebida com sucesso"), TYPE.TRAY_NOTIFICATION));					
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível receber a mensagem."), TYPE.ALERT_NOTIFICATION));					
				}
			});
		}
		else{
			onFormValidationFailed();
		}

	}
	protected void onClickSend() {
		isSendMessage = true;
		toSend = true;
		Conversation conv = view.getForm().getValue();
		conv.messages = new Message[1];
		view.getSendMessageForm().setValue(conv);
		view.setFormVisible(isSendMessage);
		view.lockAllMainToolbar();
	}
	protected void onSaveFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar as alterações."), TYPE.ALERT_NOTIFICATION));
	}

	protected void onClickRepeat() {
		toSend = false;
		isSendMessage = true;
		view.setFormVisible(isSendMessage);
		Conversation conv = view.getForm().getValue();
		conv.messages = new Message[1];
		conv.messages[0] = (currentMessage);
		view.getSendMessageForm().setValue(conv);
		view.lockAllMainToolbar();
	}
	protected void onClickReceive() {
		isSendMessage = false;
		Conversation conv = view.getForm().getValue();
		conv.messages = new Message[1];
		view.getReceiveMessageForm().setValue(conv);
		view.setFormVisible(isSendMessage);
		view.lockAllMainToolbar();
	}
	protected void onClickClose() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.setParameter("show", "conversationclose");
		NavigationHistoryManager.getInstance().go(item);
	}

	protected void onFormValidationFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Existem erros no preechimento do formulário"), TYPE.ERROR_TRAY_NOTIFICATION));
	}

	@Override
	public void setParameters(HasParameters parameterHolder) {

		ownerId = parameterHolder.getParameter("ownerid");
		ownerTypeId = parameterHolder.getParameter("ownertypeId");
		conversationId = parameterHolder.getParameter("conversationid");

		if(ownerId == null || ownerTypeId == null){			
			onGetOwnerFailed();
		}
		else{
			if(conversationId == null){
				onGetConversationFailed();
			}
			else{
				getConversation();
				fillOwner(ownerId, new ResponseHandler<T>() {

					@Override
					public void onResponse(T response) {
						view.getOwnerForm().setValue(response);						
					}

					@Override
					public void onError(Collection<ResponseError> errors) {
						onGetOwnerFailed();
					}

				});
			}
		}
	}

	private void getConversation() {
		broker.getConversation(conversationId, new ResponseHandler<Conversation>() {

			@Override
			public void onResponse(Conversation response) {
				view.getForm().setValue(response);
				view.setOwner(response);
				view.getForm().setReadOnly(true);
				conversation = response;
				setPermissions();
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onGetConversationFailed();
			}
		});
	}

	protected void setPermissions() {
		view.allowEdit(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.EDIT));
		view.allowSendMessage(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.SEND));
		view.allowRepeatMessage(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.REPEAT));
		view.allowReceiveMessage(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.RECEIVE));
		view.allowClose(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.CLOSE));
	}

	private void onGetConversationFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a troca de mensagens."), TYPE.ALERT_NOTIFICATION));

	}

	public void onGetOwnerFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter o processo pai."), TYPE.ALERT_NOTIFICATION));

	}

	public void updateConversation(Conversation response) {
		return;
	}



	public void navigateBack() {
		NavigationHistoryItem item = NavigationHistoryManager.getInstance().getCurrentState();
		item.popFromStackParameter("display");
		item.removeParameter("ownerid");
		item.removeParameter("ownertypeid");
		item.removeParameter("requestid");
		item.removeParameter("conversationid");
		NavigationHistoryManager.getInstance().go(item);		
	}

	protected abstract void fillOwner(String ownerId, ResponseHandler<T> handler);


	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		return;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return 0;
	}

}