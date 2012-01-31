package bigBang.library.client.userInterface.view;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.DeleteRequestEvent;
import bigBang.library.client.event.DeleteRequestEventHandler;
import bigBang.library.client.event.SelectedStateChangedEvent;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.ContactOperationsToolBar;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.List;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.ListHeader;
import bigBang.library.client.userInterface.NavigationListEntry;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter;
import bigBang.library.client.userInterface.presenter.ContactViewPresenter.Action;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContactView extends View implements ContactViewPresenter.Display{

	private VerticalPanel wrapper;
	private ActionInvokedEventHandler<Action> actionHandler;
	private DeleteRequestEventHandler deleteHandler;
	private ListEntry<Void> childContactsButton;
	private TextBoxFormField name;
	private ExpandableListBoxFormField type;
	private AddressFormField address;
	private List<ContactInfo> contactIL;
	private Contact contact;
	private ContactOperationsToolBar toolbar;

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
			infoValue = new TextBoxFormField();

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
		wrapper = new VerticalPanel();
		initWidget(wrapper);
		wrapper.setWidth("100%");
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
		type = new ExpandableListBoxFormField(BigBangConstants.TypifiedListIds.CONTACT_TYPE, "Tipo");
		address = new AddressFormField();

		childContactsButton = new NavigationListEntry<Void>(null);
		childContactsButton.getElement().getStyle().setProperty("borderTop", "1px solid gray");
		childContactsButton.setTitle("Sub-Contactos");
		childContactsButton.addSelectedStateChangedEventHandler(new SelectedStateChangedEventHandler() {

			@Override
			public void onSelectedStateChanged(SelectedStateChangedEvent event) {
				if(event.getSelected()) {
					fireAction(Action.SHOW_CHILD_CONTACTS);
				}
			}
		});
		
		childContactsButton.setHeight("40px");
		HorizontalPanel horz = new HorizontalPanel();
		horz.add(type);
		horz.add(name);
		wrapper.add(toolbar);
		wrapper.add(horz);
		wrapper.add(address);
		ListHeader conts = new ListHeader("Detalhes");
		wrapper.add(conts);
		contactIL = new List<ContactInfo>();
		contactIL.setSelectableEntries(false);
		wrapper.add(contactIL.getListContent());
		wrapper.add(childContactsButton);

		setSize("400px", "400px");
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
			
			newContact.info[i] = new ContactInfo();
			newContact.info[i].typeId = ((ContactEntry) contactIL.get(i)).type.getValue();
			newContact.info[i].value = ((ContactEntry) contactIL.get(i)).infoValue.getValue();
			
		}
		
		return newContact;
	}

	@SuppressWarnings("unused")
	private Contact getInfo() {
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

}
