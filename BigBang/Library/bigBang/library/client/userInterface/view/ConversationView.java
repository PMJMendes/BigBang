package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasValueSelectables;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ConversationChildrenPanel;
import bigBang.library.client.userInterface.ConversationOperationsToolbar;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.MessagesList;
import bigBang.library.client.userInterface.ReceiveMessageToolbar;
import bigBang.library.client.userInterface.SendMessageToolbar;
import bigBang.library.client.userInterface.form.ConversationForm;
import bigBang.library.client.userInterface.form.ReceiveMessageForm;
import bigBang.library.client.userInterface.form.SendMessageForm;
import bigBang.library.client.userInterface.presenter.ConversationViewPresenter;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ConversationView<T extends ProcessBase> extends View implements ConversationViewPresenter.Display<T>{

	private ActionInvokedEventHandler<ConversationViewPresenter.Action> actionHandler;
	private SendMessageForm sendMessageForm;
	private ReceiveMessageForm receiveMessageForm;
	private ConversationForm conversationForm;
	private ConversationOperationsToolbar toolbar;
	private MessagesList messageList;
	protected FormView<T> ownerForm;
	protected SendMessageToolbar sendMessageToolbar;
	protected ReceiveMessageToolbar receiveMessageToolbar;
	protected ConversationChildrenPanel childrenPanel;

	public ConversationView(FormView<T> ownerForm) {
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		VerticalPanel rightWrapper = new VerticalPanel();
		rightWrapper.setSize("100%", "100%");

		sendMessageForm = new SendMessageForm();
		receiveMessageForm = new ReceiveMessageForm();
		conversationForm = new ConversationForm();
		messageList = new MessagesList();

		toolbar = new ConversationOperationsToolbar() {

			@Override
			public void onSaveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ConversationViewPresenter.Action>(ConversationViewPresenter.Action.TOOLBAR_SAVE));
			}

			@Override
			public void onEditRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ConversationViewPresenter.Action>(ConversationViewPresenter.Action.TOOLBAR_EDIT));

			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ConversationViewPresenter.Action>(ConversationViewPresenter.Action.TOOLBAR_CANCEL));

			}

			@Override
			protected void onSendMessage() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ConversationViewPresenter.Action>(ConversationViewPresenter.Action.TOOLBAR_SEND));

			}

			@Override
			protected void onRepeatMessage() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ConversationViewPresenter.Action>(ConversationViewPresenter.Action.TOOLBAR_REPEAT));

			}

			@Override
			protected void onReceiveMessage() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ConversationViewPresenter.Action>(ConversationViewPresenter.Action.TOOLBAR_RECEIVE));

			}

			@Override
			protected void onCloseConversation() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ConversationViewPresenter.Action>(ConversationViewPresenter.Action.TOOLBAR_CLOSE));

			}
		};

		VerticalPanel ownerWrapper = new VerticalPanel();

		ownerWrapper.setSize("100%", "100%");
		ListHeader ownerHeader = new ListHeader("Ficha de Processo");
		ownerHeader.setLeftWidget(new Button("Voltar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ConversationViewPresenter.Action>(ConversationViewPresenter.Action.CLICK_BACK));
			}

		}));

		ownerHeader.setHeight("30px");
		ownerWrapper.add(ownerHeader);
		this.ownerForm = ownerForm;
		ownerForm.setReadOnly(true);
		ownerWrapper.add(ownerForm);
		ownerWrapper.setCellHeight(ownerForm, "100%");
		mainWrapper.addWest(ownerWrapper, 665);
		
		childrenPanel = new ConversationChildrenPanel();
		
		mainWrapper.addEast(childrenPanel, 300);

		VerticalPanel conversationWrapper = new VerticalPanel();

		ScrollPanel scrollPanel = new ScrollPanel();
		scrollPanel.getElement().getStyle().setBackgroundColor("whiteSmoke");
		scrollPanel.setSize("100%", "100%");
		scrollPanel.getElement().getStyle().setOverflowX(Overflow.HIDDEN);

		conversationWrapper.setSize("100%", "100%");
		HorizontalPanel listConversWrapper = new HorizontalPanel();
		messageList.setSize("200px", "100%");
		listConversWrapper.setSize("100%", "100%");

		listConversWrapper.add(messageList);
		rightWrapper.add(toolbar);
		rightWrapper.setCellHeight(toolbar, "21px");
		conversationForm.getNonScrollableContent().setSize("100%","100%");
		listConversWrapper.add(conversationForm.getNonScrollableContent());
		listConversWrapper.setCellWidth(conversationForm.getNonScrollableContent(), "100%");

		conversationWrapper.add(listConversWrapper);

		sendMessageToolbar = new SendMessageToolbar() {

			@Override
			public void onSendRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ConversationViewPresenter.Action>(ConversationViewPresenter.Action.SEND_MESSAGE));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ConversationViewPresenter.Action>(ConversationViewPresenter.Action.FORM_CANCEL));
			}
		};

		receiveMessageToolbar = new ReceiveMessageToolbar() {

			@Override
			public void onReceiveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ConversationViewPresenter.Action>(ConversationViewPresenter.Action.RECEIVE_MESSAGE));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<ConversationViewPresenter.Action>(ConversationViewPresenter.Action.FORM_CANCEL));
			}
		};

		conversationWrapper.add(sendMessageToolbar);
		conversationWrapper.add(receiveMessageToolbar);
		conversationWrapper.add(receiveMessageForm.getNonScrollableContent());
		conversationWrapper.add(sendMessageForm.getNonScrollableContent());

		scrollPanel.add(conversationWrapper);

		rightWrapper.add(scrollPanel);

		mainWrapper.add(rightWrapper);

		sendMessageForm.getNonScrollableContent().setVisible(false);
		receiveMessageForm.getNonScrollableContent().setVisible(false);
	}


	@Override
	public HasEditableValue<Conversation> getForm() {
		return conversationForm;
	}

	@Override
	public HasValue<T> getOwnerForm() {
		return ownerForm;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<ConversationViewPresenter.Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public HasEditableValue<Conversation> getSendMessageForm() {
		return sendMessageForm;
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
	public void clearEmails() {
		sendMessageForm.clearEmails();
	}


	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void setOwner(Conversation convers){
		messageList.setOwner(convers.id);
		childrenPanel.setOwner(convers);
	}

	@Override
	public HasValueSelectables<Message> getMessageList(){
		return messageList;
	}

	@Override
	public void setMessage(Message source) {
		conversationForm.setCurrentMessage(source);
	}


	@Override
	public void setFormVisible(boolean isSendMessage) {
		sendMessageToolbar.setVisible(isSendMessage);
		receiveMessageToolbar.setVisible(!isSendMessage);
		sendMessageForm.getNonScrollableContent().setVisible(isSendMessage);
		receiveMessageForm.getNonScrollableContent().setVisible(!isSendMessage);
		sendMessageForm.clearInfo();
		receiveMessageForm.clearInfo();
	}


	@Override
	public void addContact(String name, String nameId, String ownerTypeId) {
		sendMessageForm.addItemContactList(name, nameId, ownerTypeId);
	}

	@Override
	public void lockAllMainToolbar(){
		toolbar.lockAll();
	}


	@Override
	public void allowEdit(boolean hasPermission) {
		toolbar.allowEdit(hasPermission);
	}


	@Override
	public void allowSendMessage(boolean hasPermission) {
		toolbar.allowSendMessage(hasPermission);
	}


	@Override
	public void allowRepeatMessage(boolean hasPermission) {
		toolbar.allowRepeatMessage(hasPermission);		
	}


	@Override
	public void allowReceiveMessage(boolean hasPermission) {
		toolbar.allowReceiveMessage(hasPermission);
	}


	@Override
	public void allowClose(boolean hasPermission) {
		toolbar.allowClose(hasPermission);
	}


	@Override
	public void setHistoryOwner(Conversation conversation) {
		childrenPanel.setOwner(conversation);
	}


	@Override
	public List<HistoryItemStub> getHistoryList() {
		return childrenPanel.historyList;
	}
}
