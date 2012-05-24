package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.userInterface.ContactForm;
import bigBang.library.client.userInterface.ContactForm.ContactEntry;
import bigBang.library.client.userInterface.ContactOperationsToolBar;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationListEntry;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter.Action;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContactView extends View implements ContactViewPresenter.Display{


	private ContactForm form;
	private ContactOperationsToolBar toolbar;
	private Contact contact;
	private ActionInvokedEventHandler<Action> actionHandler;
	private List<Contact> subContacts;

	public ContactView(){

		SplitLayoutPanel wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		wrapper.setSize("100%", "100%");
		form = new ContactForm();

		//TOOLBAR
		toolbar = new ContactOperationsToolBar() {

			@Override
			public void onSaveRequest() {
				fireAction(Action.SAVE);
			}

			@Override
			public void onEditRequest() {
				fireAction(Action.EDIT);
			}

			@Override
			public void onCancelRequest() {
				fireAction(Action.CANCEL);
			}

			@Override
			public void onDelete(){
				fireAction(Action.DELETE);
			}

			@Override
			public void onCreateSubContact(){
				fireAction(Action.CREATE_CHILD_CONTACT);
			}

		};
		VerticalPanel wrapperRight  = new VerticalPanel();
		subContacts = new List<Contact>();
		ListHeader header = new ListHeader();
		header.setText("Sub-Contactos");
		wrapperRight.setSize("100%", "100%");
		subContacts.setSize("100%","100%");
		subContacts.setHeaderWidget(header);
		wrapperRight.add(subContacts);
		toolbar.setWidth("100%");
		VerticalPanel innerWrapper = new VerticalPanel();
		innerWrapper.add(toolbar);
		innerWrapper.add(form.getNonScrollableContent());
		innerWrapper.setCellHeight(form.getNonScrollableContent(), "100%");
		wrapper.addEast(wrapperRight, 235);
		wrapper.setWidgetMinSize(wrapperRight, 235);
		wrapper.add(innerWrapper);

	}

	@Override
	protected void onAttach() {
		super.onAttach();
		if(contact != null)
			fireAction(Action.ATTACHED);
	}

	protected void fireAction(Action action){
		if(this.actionHandler != null) {
			actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
		}
	}

	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void setContact(Contact contact) {

		form.setValue(contact);
		this.contact = contact;

	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;

	}

	@Override
	public void setEditable(boolean editable) {

		form.setEditable(editable);
	}

	@Override
	public List<ContactInfo> getContactInfoList() {

		return form.getContactInfoList();

	}

	@Override
	public ContactEntry initializeContactEntry() {
		return form.initializeContactEntry();
	}

	@Override
	public Contact getContact() {

		Contact newContact = form.getInfo();
		newContact.subContacts = new Contact[getSubContactList().size()];

		int curr = 0;

		for(ListEntry<Contact> contact : getSubContactList()){
			newContact.subContacts[curr] = contact.getValue();
			curr++;
		}

		return newContact;
	}


	public Contact getInfo() {
		return this.contact;
	}

	@Override
	public ContactOperationsToolBar getToolbar(){
		return toolbar;
	}
	@Override
	public void setSaveMode(boolean b) {
		toolbar.setSaveModeEnabled(b);
	}

	@Override
	public List<Contact> getSubContactList(){

		return subContacts;
	}

	public void setSubContacts(Contact[] contacts) {

		subContacts.clear();
		NavigationListEntry<Contact> temp;
		Label tempLabel;
		for(int i = 0; i<contacts.length; i++){
			temp = new NavigationListEntry<Contact>(contacts[i]);
			tempLabel = new Label(temp.getValue().name);
			temp.setWidget(tempLabel);
			subContacts.add(temp);
		}
	}

	public void addSubContact(Contact contact) {

		NavigationListEntry<Contact> temp;
		temp = new NavigationListEntry<Contact>(contact);
		Label tempLabel;
		tempLabel = new Label(temp.getValue().name);
		temp.setWidget(tempLabel);
		subContacts.add(temp);
		for(int i = 0; i<subContacts.size(); i++){
			System.out.println(subContacts.get(i).getValue().name);
		}

	}
}
