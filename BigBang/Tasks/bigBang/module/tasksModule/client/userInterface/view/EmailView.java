package bigBang.module.tasksModule.client.userInterface.view;

import java.util.List;

import bigBang.definitions.shared.Message;
import bigBang.definitions.shared.Message.AttachmentUpgrade;
import bigBang.definitions.shared.TipifiedListItem;
import bigBang.library.client.HasEditableValue;
import bigBang.library.client.HasSelectables;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.ReceiveMessageToolbar;
import bigBang.library.client.userInterface.form.ExchangeItemForm;
import bigBang.library.client.userInterface.view.ExchangeItemSelectionView;
import bigBang.library.client.userInterface.view.ExchangeItemSelectionView.AttachmentEntry;
import bigBang.library.client.userInterface.view.ExchangeItemSelectionView.EmailEntry;
import bigBang.library.client.userInterface.view.View;
import bigBang.library.shared.AttachmentStub;
import bigBang.library.shared.ExchangeItem;
import bigBang.library.shared.ExchangeItemStub;
import bigBang.module.tasksModule.client.userInterface.form.EmailReceiverForm;
import bigBang.module.tasksModule.client.userInterface.presenter.EmailViewPresenter;
import bigBang.module.tasksModule.client.userInterface.presenter.EmailViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EmailView extends View implements EmailViewPresenter.Display{


	FilterableList<ExchangeItemStub> emails;
	FilterableList<AttachmentStub> attachments;
	ExchangeItemForm centerForm;
	EmailReceiverForm form;
	ReceiveMessageToolbar toolbar;
	
	private ActionInvokedEventHandler<EmailViewPresenter.Action> actionHandler;
	private ListHeader header;

	public EmailView() {
		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);

		wrapper.setSize("100%", "100%");
		VerticalPanel leftWrapper = new VerticalPanel();
		leftWrapper.setSize("100%", "100%");

		header = new ListHeader("Lista de E-mails");
		header.showRefreshButton();
		header.showNewButton("Obter todos (lento)");
		header.getNewButton().setVisible(true);
		header.getNewButton().setEnabled(false);
		header.getNewButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<EmailViewPresenter.Action>(EmailViewPresenter.Action.GET_ALL_EMAILS));
			}
		});
		
		header.getRefreshButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				actionHandler.onActionInvoked(new ActionInvokedEvent<EmailViewPresenter.Action>(EmailViewPresenter.Action.REFRESH));
			}
		});
		emails = new FilterableList<ExchangeItemStub>();
		emails.setHeaderWidget(header);
		emails.showFilterField(false);
		leftWrapper.add(emails);
		leftWrapper.setCellHeight(emails, "100%");
		wrapper.addWest(leftWrapper, 330);


		//RIGHT
		SplitLayoutPanel insideWrapper = new SplitLayoutPanel();
		VerticalPanel rightWrapper = new VerticalPanel();
		rightWrapper.setSize("100%", "100%");
		ListHeader aHeader = new ListHeader("Lista de Anexos");
		attachments = new FilterableList<AttachmentStub>();
		attachments.setCheckable(true);
		attachments.setHeaderWidget(aHeader);
		attachments.showFilterField(false);
		attachments.setSelectableEntries(false);
		form = new EmailReceiverForm();
		toolbar = new ReceiveMessageToolbar() {
			
			@Override
			public void onReceiveRequest() {
				actionHandler.onActionInvoked(new ActionInvokedEvent<EmailViewPresenter.Action>(Action.CONFIRM));
			}
			
			@Override
			public void onCancelRequest() {
				clear();
			}
		};
		rightWrapper.add(toolbar);
		rightWrapper.add(form.getNonScrollableContent());
		form.getNonScrollableContent().setSize("100%", "100%");		
		rightWrapper.add(attachments);
		rightWrapper.setCellHeight(attachments, "100%");
		insideWrapper.addEast(rightWrapper, 600);

		//CENTER
		centerForm = new ExchangeItemForm();
		centerForm.setSize("100%", "100%");
		centerForm.setReadOnly(true);
		centerForm.setTextBoxSize("98%", "600px");
		insideWrapper.setSize("100%", "100%");
		VerticalPanel insideVerticalWrapper = new VerticalPanel();
		insideVerticalWrapper.setSize("100%", "100%");

		ListHeader centerHeader = new ListHeader("E-mail");
		insideVerticalWrapper.add(centerHeader);
		insideVerticalWrapper.add(centerForm);
		insideVerticalWrapper.setCellHeight(centerForm, "100%");
		
		insideWrapper.add(insideVerticalWrapper);
		wrapper.add(insideWrapper);


	}


	@Override
	protected void initializeView() {
		return;	
	}


	@Override
	public void setTypifiedListItems(List<TipifiedListItem> conversations) {
		form.setConversations(conversations);
	}


	@Override
	public HasSelectables<ValueSelectable<ExchangeItemStub>> getEmailList() {
		return emails;
	}


	@Override
	public HasEditableValue<ExchangeItem> getForm() {
		return centerForm;
	}


	@Override
	public void setAttachments(AttachmentStub[] attachments) {
		this.attachments.clear();
		
		for(AttachmentStub b : attachments){
			this.attachments.add(new ExchangeItemSelectionView.AttachmentEntry(b));
		}
	}


	@Override
	public void registerActionHandler(
			ActionInvokedEventHandler<Action> actionInvokedEventHandler) {
		this.actionHandler = actionInvokedEventHandler;
	} 

	@Override
	public void enableGetAll(boolean b) {
		header.getNewButton().setEnabled(b);
	}
	
	@Override
	public void addEmailEntry(ExchangeItemStub email){
		EmailEntry entry = new EmailEntry(email);
		emails.add(entry);
	}
	
	@Override
	public void clear(){
		toolbar.allowCancel(false);
		toolbar.allowReceive(false);
		centerForm.clearInfo();
		form.clearInfo();
		attachments.clear();
		form.setReadOnly(true);
	}	
	
	@Override
	public Message.AttachmentUpgrade[] getChecked() {


		int ammount = attachments.getChecked().size();
		bigBang.definitions.shared.Message.AttachmentUpgrade[] attachs = new Message.AttachmentUpgrade[ammount];
		int counter = 0;

		AttachmentUpgrade temp;

		for(int i = 0; i<attachments.size(); i++){
			if(attachments.get(i).isChecked()){
				temp = new Message.AttachmentUpgrade();
				temp.docTypeId = ((AttachmentEntry)attachments.get(i)).getDocType().getValue();
				temp.name =((AttachmentEntry)attachments.get(i)).getDocName().getValue();
				temp.attachmentId = ((AttachmentEntry)attachments.get(i)).getValue().id;
				attachs[counter] = temp;
				counter++;
			}
		}

		return attachs;
	}


	@Override
	public Integer getReplyLimit() {
		return form.getReplyLimit();
	}


	@Override
	public String getParentType() {
		return form.getParentType();
	}


	@Override
	public String getParentId() {
		return form.getParentId();
	}


	@Override
	public void clearList() {
		emails.clear();
	}


	@Override
	public HasEditableValue<Message> getMessageForm() {
		return form;
	}
	
	@Override
	public void enableAllToolbar(){
		toolbar.allowCancel(true);
		toolbar.allowReceive(true);
	}


	@Override
	public String getRequestType() {
		return form.getRequestType();
	}


	@Override
	public void enableRefresh(boolean b) {
		header.getRefreshButton().setEnabled(b);
	}


	@Override
	public void removeSelected() {
		for(ValueSelectable<ExchangeItemStub>  email: emails.getSelected()){
			emails.remove(email);
			break;
		}
	}
}
