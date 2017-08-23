package bigBang.library.client.userInterface.presenter;

import java.util.Collection;

import bigBang.definitions.client.BigBangConstants;
import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.ConversationStub;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.EventBus;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasParameters;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.Notification;
import bigBang.library.client.Notification.TYPE;
import bigBang.library.client.PermissionChecker;
import bigBang.library.client.Selectable;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.ConversationBroker;
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
import bigBang.library.interfaces.FileService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public abstract class ConversationViewPresenter<T extends ProcessBase> implements ViewPresenter{

	public static enum Action{
		SAVE,
		SEND_MESSAGE,
		RECEIVE_MESSAGE,
		TOOLBAR_CANCEL,
		TOOLBAR_NEW,
		CLICK_BACK,
		TOOLBAR_EDIT,
		TOOLBAR_REPEAT,
		TOOLBAR_CLOSE, 
		TOOLBAR_RECEIVE, 
		TOOLBAR_SAVE,
		CONVERSATION_BUTTON_CLICKED,
		FORM_CANCEL,
		TOOLBAR_REPOEN,
		TOOLBAR_REPLY,
		TOOLBAR_REPLYALL, 
		TOOLBAR_FORWARD
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
		void allowEdit(boolean hasPermission);
		void allowNewMessage(boolean hasPermission);
		void allowReplyMessage(boolean hasPermission);
		void allowReplyAllMessage(boolean hasPermission);
		void allowForwardMessage(boolean hasPermission);
		void allowRepeatMessage(boolean hasPermission);
		void allowReceiveMessage(boolean hasPermission);
		void allowClose(boolean hasPermission);
		void allowReopen(boolean hasPermission);
		void setOwner(Conversation convers);
		void setHistoryOwner(Conversation conversation);
		List<HistoryItemStub> getHistoryList();
		void setMainFormVisible(boolean b);
		void setTypeAndOwnerId(String ownerTypeId, String ownerId);
		void setSaveMode(boolean b);
		HasClickHandlers getPrintButton();
		Frame getPrintFrame();
		void clearReportSections();
	}

	public ConversationViewPresenter(Display<T> view){
		setView((UIObject)view);
		broker = (ConversationBroker) DataBrokerManager.staticGetBroker(BigBangConstants.EntityIds.CONVERSATION);

	}

	protected Display<T> view;
	protected boolean bound = false;
	protected String ownerId;
	protected String ownerTypeId;
	protected String conversationId;
	protected ConversationBroker broker;
	private boolean isSendMessage;
	protected Message currentMessage;
	protected Conversation conversation;
	private boolean toSend;
	protected String currentPrintFileId;

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
				case TOOLBAR_SAVE:
					onSave();
					break;
				case TOOLBAR_NEW:
					onClickNew();
					break;
				case TOOLBAR_REPLY:
					onClickReply();
					break;
				case TOOLBAR_REPLYALL:
					onClickReplyAll();
					break;
				case TOOLBAR_FORWARD:
					onClickForward();
					break;
				case TOOLBAR_REPEAT:
					onClickRepeat();
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
				case TOOLBAR_REPOEN:
					onReopen();
					break;
				default:
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
					setPermissions();
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

		view.getPrintButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onPrintConversation();
			}
		});

		view.getPrintFrame().addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {
				if ( currentPrintFileId != null ) {
					String str = currentPrintFileId;
					currentPrintFileId = null;
					print();
					FileService.Util.getInstance().Discard(str, new AsyncCallback<Void>() {
	
						@Override
						public void onFailure(Throwable caught) {
						}
	
						@Override
						public void onSuccess(Void result) {
						}
					});
				}
			}
		});

		bound = true;
	}

	protected void onReopen() {

		NavigationHistoryItem navigationItem = NavigationHistoryManager.getInstance().getCurrentState();
		navigationItem.setParameter("show", "reopenconversation");
		NavigationHistoryManager.getInstance().go(navigationItem);

	}

	protected void print(){
		print(view.getPrintFrame().getElement());
	}

	protected native void print(Element frame) /*-{
	 	frame = frame.contentWindow;
    	frame.focus();
    	frame.print();
	}-*/;

	protected void onPrintConversation() {
		broker.getForPrinting(conversationId, new ResponseHandler<String>(){

			@Override
			public void onResponse(String response) {
				currentPrintFileId = response;
				/*Frame frame = ConversationViewPresenter.this.view.getPrintFrame();
				frame.setUrl(GWT.getModuleBaseURL() + "bbfile?fileref=" + response);*/
				// New way to print with styling
				// https://stackoverflow.com/questions/44586986/why-is-google-chrome-not-printing-table-and-cell-borders-and-cell-background-co
				Window.open(GWT.getModuleBaseURL() + "bbfile?fileref=" + response , null, null);
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				onPrintFailure();
			}


		});
	}

	protected void onPrintFailure() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não é possível imprimir a troca de mensagens"), TYPE.ALERT_NOTIFICATION));
	}

	protected void resetView(){
		view.closeSubForms();
		view.setMainFormVisible(true);
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
					conversation = response;
					view.getForm().setValue(response);
					view.getForm().setReadOnly(true);
					view.setSaveMode(false);
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
					conversation = response;
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
					conversation = response;
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
			Conversation conv = view.getReceiveMessageForm().getInfo();
			broker.receiveMessage(conv.messages[0], conv.replylimit, new ResponseHandler<Conversation>() {

				@Override
				public void onResponse(Conversation response) {
					view.setHistoryOwner(conversation);
					conversation = response;
					resetView();
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Mensagem arquivada com sucesso"), TYPE.TRAY_NOTIFICATION));					
				}

				@Override
				public void onError(Collection<ResponseError> errors) {
					
					String error = "";
					
					for (ResponseError tmp : errors) {
						error = error + tmp.description;
					}
					
					EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a mensagem. " + error), TYPE.ALERT_NOTIFICATION));					
				}
			});
		}
		else{
			onFormValidationFailed();
		}

	}

	protected void onSaveFailed() {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível gravar as alterações."), TYPE.ALERT_NOTIFICATION));
	}

	protected void onClickNew() {
		broker.getEmpty(new ResponseHandler<Message>() {

			@Override
			public void onResponse(Message response) {
				isSendMessage = true;
				toSend = true;
				Conversation conv = view.getForm().getValue();
				conv.messages = new Message[1];
				conv.messages[0] = (response);
				view.setFormVisible(isSendMessage);
				view.getSendMessageForm().setValue(conv);
				view.setMainFormVisible(false);
				
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a mensagem original."), TYPE.ALERT_NOTIFICATION));
				
			}});
	}

	protected void onClickReply() {
		broker.getForReply(currentMessage.id, new ResponseHandler<Message>() {

			@Override
			public void onResponse(Message response) {
				isSendMessage = true;
				toSend = true;
				Conversation conv = view.getForm().getValue();
				conv.messages = new Message[1];
				conv.messages[0] = (response);
				view.setFormVisible(isSendMessage);
				view.getSendMessageForm().setValue(conv);
				view.setMainFormVisible(false);
				
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a mensagem original."), TYPE.ALERT_NOTIFICATION));
				
			}});
	}

	protected void onClickReplyAll() {
		broker.getForReplyAll(currentMessage.id, new ResponseHandler<Message>() {

			@Override
			public void onResponse(Message response) {
				isSendMessage = true;
				toSend = true;
				Conversation conv = view.getForm().getValue();
				conv.messages = new Message[1];
				conv.messages[0] = (response);
				view.setFormVisible(isSendMessage);
				view.getSendMessageForm().setValue(conv);
				view.setMainFormVisible(false);
				
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a mensagem original."), TYPE.ALERT_NOTIFICATION));
				
			}});
	}

	protected void onClickForward() {
		broker.getForForward(currentMessage.id, new ResponseHandler<Message>() {

			@Override
			public void onResponse(Message response) {
				isSendMessage = true;
				toSend = true;
				Conversation conv = view.getForm().getValue();
				conv.messages = new Message[1];
				conv.messages[0] = (response);
				view.setFormVisible(isSendMessage);
				view.getSendMessageForm().setValue(conv);
				view.setMainFormVisible(false);
				
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a mensagem original."), TYPE.ALERT_NOTIFICATION));
				
			}});
	}

	protected void onClickRepeat() {
		broker.getForRepeat(currentMessage.id, new ResponseHandler<Message>() {

			@Override
			public void onResponse(Message response) {
				isSendMessage = true;
				toSend = false;
				Conversation conv = view.getForm().getValue();
				conv.messages = new Message[1];
				conv.messages[0] = (response);
				view.setFormVisible(isSendMessage);
				view.getSendMessageForm().setValue(conv);
				view.setMainFormVisible(false);
				
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a mensagem original."), TYPE.ALERT_NOTIFICATION));
				
			}});
	}

	protected void onClickReceive() {
		isSendMessage = false;
		Conversation conv = view.getForm().getValue();
		conv.messages = new Message[1];
		view.setFormVisible(isSendMessage);
		view.getReceiveMessageForm().setValue(conv);
		view.setMainFormVisible(false);
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
		view.getPrintFrame().setUrl("");
		view.clearReportSections();
		view.setTypeAndOwnerId(ownerTypeId, ownerId);

		if(ownerId == null || ownerTypeId == null){			
			onGetOwnerFailed();
		}
		else{
			if(conversationId == null){
				onGetConversationFailed("Could not get the conversation");
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

	protected void getConversation() {
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
				String errorStr = "";
				for (ResponseError err : errors) {
					errorStr = errorStr + err.description + " ";
				}
				onGetConversationFailed(errorStr);
			}
		});
	}

	protected void setPermissions() {
		view.allowEdit(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.EDIT));
		view.allowNewMessage(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.SEND));
		view.allowReplyMessage(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.SEND) && (currentMessage != null));
		view.allowReplyAllMessage(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.SEND) && (currentMessage != null));
		view.allowForwardMessage(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.SEND) && (currentMessage != null));
		view.allowRepeatMessage(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.REPEAT) && (currentMessage == null || !ConversationStub.Direction.INCOMING.equals(currentMessage.direction)));
		view.allowReceiveMessage(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.RECEIVE));
		view.allowClose(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.CLOSE));
		view.allowReopen(PermissionChecker.hasPermission(conversation, BigBangConstants.OperationIds.ConversationProcess.REOPEN));
	}

	private void onGetConversationFailed(String err) {
		EventBus.getInstance().fireEvent(new NewNotificationEvent(new Notification("", "Não foi possível obter a troca de mensagens. " + err), TYPE.ALERT_NOTIFICATION));

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

}