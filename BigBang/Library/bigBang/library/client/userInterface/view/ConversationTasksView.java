package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ConversationTasksOperationsToolbar;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.MessagesList;
import bigBang.library.client.userInterface.ReceiveMessageToolbar;
import bigBang.library.client.userInterface.SendMessageToolbar;
import bigBang.library.client.userInterface.form.ConversationForm;
import bigBang.library.client.userInterface.form.ReceiveMessageForm;
import bigBang.library.client.userInterface.form.SendMessageForm;
import bigBang.library.client.userInterface.presenter.ConversationTasksViewPresenter;
import bigBang.library.client.userInterface.presenter.ConversationTasksViewPresenter.Action;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConversationTasksView<T> extends View implements ConversationTasksViewPresenter.Display{

	protected ConversationTasksOperationsToolbar toolbar;
	private SendMessageForm sendMessageForm;
	private ReceiveMessageForm receiveMessageForm;
	private ConversationForm conversationForm;
	private MessagesList messageList;
	protected View ownerForm;
	protected SendMessageToolbar sendMessageToolbar;
	protected ReceiveMessageToolbar receiveMessageToolbar;
	protected ActionInvokedEventHandler<ConversationTasksViewPresenter.Action> handler;
	private ScrollPanel ownerWrapper;
	private PopupPanel popupPanel;
	private HasWidgets overlayContainer;
	private HorizontalPanel listAndConversation;

	public ConversationTasksView() {
		
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		
		mainWrapper.setSize("100%", "100%");
		
		toolbar = new ConversationTasksOperationsToolbar() {
			
			@Override
			protected void onGoToProcess() {
				handler.onActionInvoked(new ActionInvokedEvent<ConversationTasksViewPresenter.Action>(Action.GO_TO_PROCESS));
			}

			@Override
			protected void onClose() {
				handler.onActionInvoked(new ActionInvokedEvent<ConversationTasksViewPresenter.Action>(Action.CLICK_CLOSE));
				
			}

			@Override
			protected void onRepeat() {
				handler.onActionInvoked(new ActionInvokedEvent<ConversationTasksViewPresenter.Action>(Action.CLICK_REPEAT));
				
			}

			@Override
			protected void onSend() {
				handler.onActionInvoked(new ActionInvokedEvent<ConversationTasksViewPresenter.Action>(Action.CLICK_SEND));
				
			}

			@Override
			protected void onReceive() {
				handler.onActionInvoked(new ActionInvokedEvent<ConversationTasksViewPresenter.Action>(Action.CLICK_RECEIVE));
				
			}
		};
		
		VerticalPanel leftWrapper = new VerticalPanel();
		messageList = new MessagesList();
		listAndConversation = new HorizontalPanel();
		conversationForm = new ConversationForm();
		conversationForm.setReadOnly(true);
		leftWrapper.add(toolbar);
		listAndConversation.add(messageList);
		leftWrapper.setSize("100%", "100%");
		messageList.setSize("200px", "100%");
		listAndConversation.add(conversationForm.getNonScrollableContent());
		conversationForm.getNonScrollableContent().setSize("100%", "100%");
		listAndConversation.setCellWidth(conversationForm.getNonScrollableContent(), "100%");
		listAndConversation.setSize("100%", "100%");
		leftWrapper.add(listAndConversation);
		
		sendMessageToolbar = new SendMessageToolbar() {
			
			@Override
			public void onSendRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<ConversationTasksViewPresenter.Action>(Action.SEND));
			}
			
			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<ConversationTasksViewPresenter.Action>(Action.CANCEL));
				
			}
		};
		
		receiveMessageToolbar = new ReceiveMessageToolbar() {
			
			@Override
			public void onReceiveRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<ConversationTasksViewPresenter.Action>(Action.RECEIVE));
				
			}
			
			@Override
			public void onCancelRequest() {
				handler.onActionInvoked(new ActionInvokedEvent<ConversationTasksViewPresenter.Action>(Action.CANCEL));
				
			}
		};
		
		
		
		receiveMessageForm = new ReceiveMessageForm();
		sendMessageForm = new SendMessageForm();
		
		leftWrapper.add(sendMessageToolbar);
		leftWrapper.add(receiveMessageToolbar);
		leftWrapper.add(sendMessageForm.getNonScrollableContent());
		leftWrapper.add(receiveMessageForm.getNonScrollableContent());

		ScrollPanel scroll = new ScrollPanel();
		scroll.setSize("100%", "100%");
		scroll.add(leftWrapper);
		scroll.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		scroll.getElement().getStyle().setBackgroundColor("whiteSmoke");
		ownerWrapper = new ScrollPanel();
		ownerWrapper.getElement().getStyle().setBackgroundColor("whiteSmoke");
		ownerWrapper.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		mainWrapper.addEast(ownerWrapper, 270);
		mainWrapper.getElement().getStyle().setBackgroundColor("whitesmoke");
		mainWrapper.add(scroll);
		
		this.overlayContainer = new SimplePanel();
	}
	
	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.handler = handler;
	}

	@Override
	public void clearAllowedPermissions() {
		toolbar.lockAll();
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Conversation> getForm() {
		return conversationForm;
	}

	@Override
	public void allowClose(boolean b) {
		toolbar.allowClose(b);
	}

	@Override
	public void allowReceive(boolean b) {
		toolbar.allowReceive(b);
	}

	@Override
	public void allowRepeat(boolean b) {
		toolbar.allowRepeat(b);
	}

	@Override
	public void allowSend(boolean b) {
		toolbar.allowSend(b);
	}

	@Override
	public View getParentForm() {
		return ownerForm;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setOwnerForm(View form) {
		ownerForm = form;
		ownerWrapper.add(((FormView) ownerForm).getNonScrollableContent());
		((FormView) ownerForm).getNonScrollableContent().setSize("100%", "100%");
		((FormView) ownerForm).setReadOnly(true);
		ownerWrapper.setHeight("100%");
		//ownerWrapper.setCellHeight(ownerForm, "100%");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void setOwnerFormValue(ProcessBase response) {
		((FormView)ownerForm).setValue(response);
	}
	
	@Override
	public void setOwner(String ownerId){
		messageList.setOwner(ownerId);
	}

	@Override
	public List<Message> getMessageList() {
		return messageList;
	}

	@Override
	public void setMessage(Message m) {
		conversationForm.setCurrentMessage(m);
	}

	@Override
	public HasEditableValue<Conversation> getSendMessageForm() {
		return sendMessageForm;
	}

	@Override
	public void setFormVisible(boolean isSendMessage) {
		sendMessageForm.clearInfo();
		sendMessageForm.getNonScrollableContent().setVisible(isSendMessage);
		sendMessageToolbar.setVisible(isSendMessage);
		
		receiveMessageForm.clearInfo();
		receiveMessageForm.getNonScrollableContent().setVisible(!isSendMessage);
		receiveMessageToolbar.setVisible(!isSendMessage);
	}

	@Override
	public void lockAllMainToolbar() {
		toolbar.lockAll();
	}

	@Override
	public HasEditableValue<Conversation> getReceiveMessageForm() {
		return receiveMessageForm;
	}

	@Override
	public void closeSubForms() {
		sendMessageToolbar.setVisible(false);
		receiveMessageToolbar.setVisible(false);
		sendMessageForm.getNonScrollableContent().setVisible(false);
		receiveMessageForm.getNonScrollableContent().setVisible(false);		
	}

	@Override
	public void addContact(String name, String nameId, String ownerTypeId) {
		sendMessageForm.addItemContactList(name, nameId, ownerTypeId);		
	}

	@Override
	public void showOverlayViewContainer(boolean show) {
		if(show && this.popupPanel == null){
			this.popupPanel = new PopupPanel(){
				@Override
				protected void onDetach() {
					super.onDetach();
					ConversationTasksView.this.popupPanel = null;
				}
			};
			this.popupPanel.add((Widget)this.overlayContainer);
		}

		if(this.popupPanel != null){
			if(show && !this.popupPanel.isAttached()){
				this.popupPanel.center();
			}else if(this.popupPanel.isAttached() && !show){
				this.popupPanel.hidePopup();
				this.popupPanel.remove((Widget)this.overlayContainer);
				this.popupPanel = null;
			}
		}
	}

	@Override
	public HasWidgets getOverlayViewContainer() {
		return this.overlayContainer;
	}

	@Override
	public void setMainFormVisible(boolean b) {
		listAndConversation.setVisible(b);
		toolbar.setVisible(b);
	}
	
	@Override
	public void setTypeAndOwnerId(String ownerTypeId, String ownerId) {
		sendMessageForm.setTypeAndOwnerId(ownerTypeId, ownerId);
	}

	
	
}
