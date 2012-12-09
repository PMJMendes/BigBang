package bigBang.library.client.userInterface.view;

import java.util.Collection;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.Conversation;
import bigBang.definitions.shared.Document;
import bigBang.definitions.shared.ProcessBase;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.SendMessageToolbar;
import bigBang.library.client.userInterface.form.SendMessageForm;
import bigBang.library.client.userInterface.presenter.SendMessageViewPresenter;
import bigBang.library.client.userInterface.presenter.SendMessageViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class SendMessageView<T extends ProcessBase> extends View implements SendMessageViewPresenter.Display<T>{

	protected SendMessageForm messageForm;
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected FormView<T> ownerForm;

	public SendMessageView(FormView<T> ownerForm){
		SplitLayoutPanel mainWrapper = new SplitLayoutPanel();
		initWidget(mainWrapper);
		mainWrapper.setSize("100%", "100%");

		messageForm = new SendMessageForm();

		SendMessageToolbar toolbar = new SendMessageToolbar() {

			@Override
			public void onSendRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SendMessageViewPresenter.Action>(Action.SEND));
			}

			@Override
			public void onCancelRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SendMessageViewPresenter.Action>(Action.CANCEL));
			}
		};

		VerticalPanel ownerWrapper = new VerticalPanel();
		ownerWrapper.setSize("100%", "100%");
		ListHeader ownerHeader = new ListHeader("Ficha de Processo");
		ownerHeader.setLeftWidget(new Button("Voltar", new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<SendMessageViewPresenter.Action>(Action.ON_CLICK_BACK));
			}

		}));

		ownerHeader.setHeight("30px");
		ownerWrapper.add(ownerHeader);
		this.ownerForm = ownerForm;
		ownerForm.setReadOnly(true);
		ownerWrapper.add(ownerForm);
		ownerWrapper.setCellHeight(ownerForm, "100%");
		mainWrapper.addWest(ownerWrapper, 665);

		VerticalPanel requestFormWrapper = new VerticalPanel();
		requestFormWrapper.setSize("100%", "100%");
		ListHeader requestHeader = new ListHeader("Mensagem");
		requestFormWrapper.add(requestHeader);
		requestHeader.setHeight("30px");
		requestFormWrapper.add(toolbar);
		requestFormWrapper.setCellHeight(toolbar, "21px");
		requestFormWrapper.add(messageForm);
		requestFormWrapper.setCellHeight(messageForm, "100%");
		mainWrapper.add(requestFormWrapper);
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public HasEditableValue<Conversation> getForm() {
		return this.messageForm;
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;
	}

	@Override
	public HasValue<T> getOwnerForm() {
		return this.ownerForm;
	}

	@Override
	public HasValue<String> getTo(){
		return messageForm.getTo();
	}
	
	@Override
	public void addContact(String name, String id, String ownerType) {
		messageForm.addItemContactList(name, id, ownerType);
	}
		
	@Override
	public void clearEmails(){
		messageForm.clearEmails();
	}
	
	@Override
	public void setAvailableContacts(Contact[] contacts){
		messageForm.setAvailableContacts(contacts);
	}
	
	@Override
	public void lockCreateContact(boolean lock){
		messageForm.lockCreateNewButton(lock);
	}
	
	@Override
	public void setSelectedContact(String id){
		messageForm.getTo().setValue(id);
	}
	
	@Override
	public void addDocuments(Collection<Document> response) {
		messageForm.addDocuments(response);
	}
	
	@Override
	public void addDocument(Document document) {
		messageForm.addDocument(document);
	}
	
	@Override
	public void setTypeAndOwnerId(String ownerTypeId, String ownerId){
		messageForm.setTypeAndOwnerId(ownerTypeId, ownerId);
	}
	
}