package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.DeleteRequestEvent;
import bigBang.library.client.event.DeleteRequestEventHandler;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.ContactOperationsToolBar;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContactView extends View implements ContactViewPresenter.Display{

	private SplitLayoutPanel wrapper;
	private VerticalPanel wrapperRight;
	private VerticalPanel wrapperLeft;
	private ActionInvokedEventHandler<Action> actionHandler;
	private DeleteRequestEventHandler deleteHandler;
	private TextBoxFormField name;
	private ExpandableListBoxFormField type;
	private AddressFormField address;
	private List<ContactInfo> contactIL;
	private Contact contact;
	private ContactOperationsToolBar toolbar;
	private List<Contact> subContacts;

	public class ContactEntry extends ListEntry<ContactInfo>{

		protected ExpandableListBoxFormField type;
		protected TextBoxFormField infoValue;
		private Button remove;
		public ContactEntry(ContactInfo contactinfo) {
			super(contactinfo);
		}

		@Override
		public void setValue(ContactInfo contactinfo) {

			if(contactinfo == null){
				Button add = new Button("Adicionar Detalhe");
				add.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						fireAction(Action.ADD_NEW_DETAIL);

					}
				});
				add.setWidth("180px");
				this.setLeftWidget(add);
				super.setValue(contactinfo);
				return;	

			}

			type = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CONTACT_DETAILS_TYPE, "");
			type.setFieldWidth("100px");
			infoValue = new TextBoxFormField();
			infoValue.setFieldWidth("205px");
			infoValue.setWidth("205px");

			type.setValue(contactinfo.typeId);
			infoValue.setValue(contactinfo.value);
			
			remove = new Button("X");
			remove.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					fireEvent(new DeleteRequestEvent(getValue()));
				}
			});
			this.setLeftWidget(type);
			this.setWidget(infoValue);
			this.setRightWidget(remove);
			super.setValue(contactinfo);
		}

		public void setEditable(boolean editable){

			if(type == null){
				this.setVisible(editable);
				return;
			}
			type.setReadOnly(!editable);
			infoValue.setReadOnly(!editable);
			remove.setVisible(editable);
		}


		@Override
		public ContactInfo getValue() {

			return super.getValue();
		}
	}

	public ContactView(){
		
		
		wrapper = new SplitLayoutPanel();
		initWidget(wrapper);
		
		wrapperLeft = new VerticalPanel();
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

		name = new TextBoxFormField("Nome");
		name.setFieldWidth("200px");
		type = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CONTACT_TYPE, "Tipo");
		type.setFieldWidth("155px");
		address = new AddressFormField();

		address.setWidth("400px");
		
		HorizontalPanel horz = new HorizontalPanel();
		horz.add(type);
		horz.add(name);
		wrapperLeft.add(toolbar);
		wrapperLeft.add(horz);
		wrapperLeft.add(address);
		ListHeader conts = new ListHeader("Detalhes");
		wrapperLeft.add(conts);
		contactIL = new List<ContactInfo>();
		contactIL.setSelectableEntries(false);
		contactIL.getScrollable().setHeight("188px");
		wrapperLeft.add(contactIL.getScrollable());

		wrapper.addWest(wrapperLeft, 410);
		
		wrapperRight = new VerticalPanel();
		subContacts = new List<Contact>();
		ListHeader header = new ListHeader();
		header.setText("Sub-Contactos");
		wrapperRight.add(header);
		subContacts.getScrollable().setSize("100%","440px");
		wrapperRight.add(subContacts.getScrollable());
		wrapperRight.setSize("100%", "100%");
		wrapper.add(wrapperRight);
		wrapper.setSize("820px", "500px");
	}
	
	@Override
	protected void onAttach() {
		super.onAttach();
		if(contact != null)
			fireAction(Action.ATTACHED);
	}
	
	@Override
	protected void initializeView() {
		return;
	}

	@Override
	public void setContact(Contact contact) {

		contactIL.clear();

		if(contact == null){
			this.contact = new Contact();
			this.name.setValue("");
			this.type.setValue("");
			this.address.setValue(null);
			return;
		}
		
		this.contact = contact;
		this.name.setValue(contact.name);
		this.type.setValue(contact.typeId);
		this.address.setValue(contact.address);


	}

	@Override
	public void addContactInfo(ContactInfo contactinfo){


		ContactEntry temp = new ContactEntry(contactinfo);
		temp.setHeight("40px");
		temp.addHandler(deleteHandler, DeleteRequestEvent.TYPE);
		contactIL.add(temp);
		contactIL.getScrollable().scrollToBottom();

	}

	
	protected void fireAction(Action action){
		if(this.actionHandler != null) {
			actionHandler.onActionInvoked(new ActionInvokedEvent<Action>(action));
		}
	}

	@Override
	public void registerActionHandler(ActionInvokedEventHandler<Action> handler) {
		this.actionHandler = handler;

	}

	@Override
	public void setEditable(boolean editable) {

		this.name.setReadOnly(!editable);
		this.type.setReadOnly(!editable);
		this.type.setEditable(editable);
		this.address.setEditable(editable);
		int i;
		ContactEntry temp;
		for(i=0; i<contactIL.size(); i++){

			temp = (ContactEntry) contactIL.get(i);
			temp.setEditable(editable);
			
		}
	}

	@Override
	public List<ContactInfo> getContactInfoList() {
		
		return contactIL;
		
	}

	@Override
	public ContactEntry initializeContactEntry() {
		return new ContactEntry(new ContactInfo());
	}

	@Override
	public Contact getContact() {
		
		Contact newContact = new Contact();
		if(this.contact != null){
			newContact = this.contact;
		}
		
		newContact.address = address.getValue();
		newContact.name = name.getValue();
		newContact.typeId = type.getValue();
		
		newContact.info = new ContactInfo[contactIL.size()-1];
		for(int i = 0; i<contactIL.size()-1; i++){
			
			if(((ContactEntry) contactIL.get(i)).type.getValue() != null &&  ((ContactEntry) contactIL.get(i)).infoValue.getValue() != null){
				newContact.info[i] = new ContactInfo();
				newContact.info[i].typeId = ((ContactEntry) contactIL.get(i)).type.getValue();
				newContact.info[i].value = ((ContactEntry) contactIL.get(i)).infoValue.getValue();
			}
			else{
				contactIL.remove(i);
				i--;
			}
			
		}
		
		return newContact;
	}

	public Contact getInfo() {
		return this.contact;
	}
 
	@Override
	public void registerDeleteHandler(
			DeleteRequestEventHandler deleteRequestEventHandler) {
		this.deleteHandler = deleteRequestEventHandler;
		
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
	public void setSubContacts(Contact[] contacts) {
	
		subContacts.clear();
		ListEntry<Contact> temp;
		Label tempLabel;
		for(int i = 0; i<contacts.length; i++){
			temp = new ListEntry<Contact>(contacts[i]);
			tempLabel = new Label(temp.getValue().name);
			temp.setWidget(tempLabel);
			subContacts.add(temp);
		}
	}
	
	@Override
	public List<Contact> getSubContactList(){
		
		return subContacts;
	}

	@Override
	public void addSubContact(Contact contact) {
		
		ListEntry<Contact> temp;
		temp = new ListEntry<Contact>(contact);
		Label tempLabel;
		tempLabel = new Label(temp.getValue().name);
		temp.setWidget(tempLabel);
		subContacts.add(temp);
		for(int i = 0; i<subContacts.size(); i++){
			System.out.println(subContacts.get(i).getValue().name);
		}
		 
	}
	


}
