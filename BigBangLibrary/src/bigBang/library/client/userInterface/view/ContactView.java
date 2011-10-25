package bigBang.library.client.userInterface.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import bigBang.definitions.shared.Contact;
import bigBang.definitions.shared.ContactInfo;
import bigBang.library.client.Selectable;
import bigBang.library.client.dataAccess.ContactsBrokerClient;
import bigBang.library.client.event.ActionInvokedEvent;
import bigBang.library.client.event.ActionInvokedEventHandler;
import bigBang.library.client.event.SelectedStateChangedEvent;
import bigBang.library.client.event.SelectedStateChangedEventHandler;
import bigBang.library.client.userInterface.BigBangOperationsToolBar;
import bigBang.library.client.userInterface.BigBangOperationsToolBar.SUB_MENU;
import bigBang.library.client.userInterface.AddressFormField;
import bigBang.library.client.userInterface.ExpandableListBoxFormField;
import bigBang.library.client.userInterface.ListEntry;
import bigBang.library.client.userInterface.NavigationListEntry;
import bigBang.library.client.userInterface.TextBoxFormField;
import bigBang.library.shared.ModuleConstants;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContactView extends View implements ContactsBrokerClient {

	public static class InfoEntry extends View {

		protected ExpandableListBoxFormField typeField;
		protected TextBoxFormField valueField;
		protected ContactInfo info;

		public InfoEntry(){
			HorizontalPanel mainWrapper = new HorizontalPanel();
			mainWrapper.setWidth("100%");
			mainWrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			mainWrapper.getElement().getStyle().setBorderColor("gray");
			mainWrapper.getElement().getStyle().setBackgroundColor("#F6F6F6");

			typeField = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ContactInfoTypes, "Tipo");
			typeField.showLabel(false);
			valueField = new TextBoxFormField("Valor");
			valueField.showLabel(false);
			valueField.setFieldWidth("100%");
			valueField.setWidth("100%");

			mainWrapper.add(typeField);
			mainWrapper.add(valueField);
			mainWrapper.setCellWidth(valueField, "100%");

			initWidget(mainWrapper);
			clear();
		}

		public void setInfo(ContactInfo info){
			if(info == null){
				clear();
			}else{
				typeField.setValue(info.typeId);
				valueField.setValue(info.value);
			}
		}

		public ContactInfo getInfo() {
			this.info.typeId = typeField.getValue();
			this.info.value = valueField.getValue();
			return this.info;
		} 

		public void clear() {
			typeField.clear();
			valueField.clear();
		}

		public void setReadOnly(boolean readOnly) {
			typeField.setReadOnly(readOnly);
			valueField.setReadOnly(readOnly);
			info = new ContactInfo();
		}
	}

	public static enum Action {
		SAVE,
		EDIT,
		CANCEL,
		CREATE_CHILD_CONTACT,
		SHOW_CHILD_CONTACTS
	}

	protected TextBoxFormField nameField;
	protected ExpandableListBoxFormField typeField;
	protected VerticalPanel infoWrapper;
	protected AddressFormField addressFormField;
	protected Collection<InfoEntry> infoEntries;
	protected BigBangOperationsToolBar toolbar;
	protected Contact contact;
	protected ActionInvokedEventHandler<Action> actionHandler;
	protected Selectable childContactsButton, addInfoFieldButton;
	protected int contactsDataVersionNumber;

	@SuppressWarnings("unchecked")
	public ContactView(){
		nameField = new TextBoxFormField("Nome");
		nameField.setFieldWidth("100%");
		nameField.setLabelWidth("60px");
		typeField = new ExpandableListBoxFormField(ModuleConstants.ListIDs.ContactTypes, "Tipo");
		typeField.setLabelWidth("60px");
		addressFormField = new AddressFormField();
		addressFormField.setLabelWidth("60px");

		SimplePanel mainWrapper = new SimplePanel();
		mainWrapper.setSize("100%", "100%");

		ScrollPanel scrollWrapper = new ScrollPanel();
		scrollWrapper.setSize("100%", "100%");

		VerticalPanel wrapper = new VerticalPanel();
		wrapper.setSize("100%", "100%");

		toolbar = new BigBangOperationsToolBar() {

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
		};
		toolbar.hideAll();
		toolbar.showItem(BigBangOperationsToolBar.SUB_MENU.EDIT, true);
		toolbar.showItem(SUB_MENU.CREATE, true);
		toolbar.addItem(SUB_MENU.CREATE, new MenuItem("Sub Contacto", new Command() {

			@Override
			public void execute() {
				fireAction(Action.CREATE_CHILD_CONTACT);
			}
		}));

		toolbar.setHeight("21px");
		toolbar.setWidth("100%");

		wrapper.add(toolbar);

		VerticalPanel nameWrapper = new VerticalPanel();
		nameWrapper.setSize("100%", "100%");
		nameWrapper.setSpacing(5);
		nameWrapper.add(nameField);
		nameWrapper.add(typeField);
		nameWrapper.getElement().getStyle().setProperty("borderBottom", "1px solid gray");
		nameWrapper.add(addressFormField);
		wrapper.add(nameWrapper);

		addInfoFieldButton = new ListEntry<Void>(null);
		((ListEntry<Void>)addInfoFieldButton).setTitle("Adicionar campo");
		addInfoFieldButton.addSelectedStateChangedEventHandler(new SelectedStateChangedEventHandler() {
			
			@Override
			public void onSelectedStateChanged(SelectedStateChangedEvent event) {
				if(event.getSelected()){
					addInfoField();
					addInfoFieldButton.setSelected(false, false);
				}
			}
		});
		
		infoEntries = new ArrayList<ContactView.InfoEntry>();
		infoWrapper = new VerticalPanel();
		infoWrapper.add((Widget) addInfoFieldButton);
		infoWrapper.getElement().getStyle().setBackgroundColor("#F6F6F6");
		infoWrapper.getElement().getStyle().setProperty("borderBottom", "1px solid gray");
		infoWrapper.setSpacing(5);
		infoWrapper.setWidth("100%");
		scrollWrapper.add(infoWrapper);
		scrollWrapper.setStylePrimaryName("emptyContainer");

		wrapper.add(scrollWrapper);
		wrapper.setCellHeight(scrollWrapper, "100%");

		ListEntry<Void> childContactsButton = new NavigationListEntry<Void>(null);
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
		this.childContactsButton = childContactsButton;
		childContactsButton.setHeight("40px");
		wrapper.add(childContactsButton);

		mainWrapper.add(wrapper);

		this.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()){
					ContactView.this.childContactsButton.setSelected(false);
				}
			}
		});

		initWidget(mainWrapper);
	}

	protected void addInfoField(){
		InfoEntry entry = new InfoEntry();
		this.infoEntries.add(entry);
		this.infoEntries.remove(this.addInfoFieldButton);
		this.infoWrapper.add(entry);
		this.infoWrapper.add((Widget) this.addInfoFieldButton);
	}
	
	public void setEditable(boolean editable) {
		//TODO
	}

	@SuppressWarnings("unchecked")
	public void setContact(Contact contact){
		this.contact = contact;
		this.nameField.setValue(contact.name);
		this.infoWrapper.remove((Widget) this.addInfoFieldButton);
		for(int i = 0; contact.info != null && i < contact.info.length; i++) {
			InfoEntry entry = new InfoEntry();
			entry.setInfo(contact.info[i]);
			infoEntries.add(entry);
			infoWrapper.add(entry);
		}
		if(contact.subContacts != null){
			int subContactsCount = contact.subContacts == null ? 0 : contact.subContacts.length;
			((ListEntry<Contact>)this.childContactsButton).setTitle("Contactos Filho ("+subContactsCount+")");
		}
		this.infoWrapper.add((Widget) this.addInfoFieldButton);
	}

	protected void fireAction(Action action){
		if(this.actionHandler != null) {
			actionHandler.onActionInvoked(new ActionInvokedEvent<ContactView.Action>(action));
		}
	}

	public Contact getContact(){
		return this.contact;
	}

	public void registerActionHandler(ActionInvokedEventHandler<Action> actionHandler) {
		this.actionHandler = actionHandler;
	}

	public void clear(){
		this.nameField.clear();
		this.typeField.clear();
		this.infoWrapper.clear();
		this.contact = new Contact();
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		return;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		return -1;
	}

	@Override
	public int getContactsDataVersionNumber(String ownerId) {
		return -1;
	}

	@Override
	public void setContactsDataVersionNumber(String ownerId, int number) {
		return;
	}

	@Override
	public void setContacts(String ownerId, List<Contact> contacts) {
		if(contact == null) {
			return;
		}
		if((ownerId == null && contact.ownerId == null) || ((ownerId != null && contact.ownerId != null) && ownerId.equalsIgnoreCase(contact.ownerId))) {
			for(Contact c : contacts) {
				if(c.id.equalsIgnoreCase(contact.id)) {
					setContact(c);
					break;
				}
			}
		}
	}

	@Override
	public void removeContact(String ownerId, Contact contact) {
		if(this.contact == null || contact == null) {
			return;
		}
		
		if((ownerId == null && this.contact.ownerId == null) || ((ownerId != null && this.contact.ownerId != null) && ownerId.equalsIgnoreCase(this.contact.ownerId))) {
			if(contact.id.equalsIgnoreCase(this.contact.id)){
				clear();
			
			}else if(contact.ownerId != null && contact.ownerId.equalsIgnoreCase(this.contact.id)){
				
				ArrayList<Contact> newSubContacts = new ArrayList<Contact>();
				for(int i = 0; this.contact.subContacts != null && i < contact.subContacts.length; i++) {
					if(!this.contact.subContacts[i].id.equalsIgnoreCase(contact.id)){
						newSubContacts.add(this.contact.subContacts[i]);
					}
				}
				this.contact.subContacts = (Contact[]) newSubContacts.toArray();
			}
		}
	}

	@Override
	public void addContact(String ownerId, Contact contact) {
		if(contact.ownerId != null && contact.ownerId.equalsIgnoreCase(this.contact.id)){
			ArrayList<Contact> newSubContacts = new ArrayList<Contact>();
			for(int i = 0; this.contact.subContacts != null && i < contact.subContacts.length; i++) {
				newSubContacts.add(this.contact.subContacts[i]);
			}
			newSubContacts.add(contact);
			this.contact.subContacts = (Contact[]) newSubContacts.toArray();
		}
	}

	@Override
	public void updateContact(String ownerId, Contact contact) {
		if(this.contact == null || contact == null) {
			return;
		}
		
		if((ownerId == null && this.contact.ownerId == null) || ((ownerId != null && this.contact.ownerId != null) && ownerId.equalsIgnoreCase(this.contact.ownerId))) {
			if(contact.id.equalsIgnoreCase(this.contact.id)){
				setContact(contact);
			}else if(contact.ownerId != null && contact.ownerId.equalsIgnoreCase(this.contact.id)){
				for(int i = 0; this.contact.subContacts != null && i < contact.subContacts.length; i++) {
					if(this.contact.subContacts[i].id.equalsIgnoreCase(contact.id)){
						this.contact.subContacts[i] = contact;
						break;
					}
				}
			}
		}
	}
	
	public void setReadOnly(boolean readonly){
		this.typeField.setReadOnly(readonly);
		this.nameField.setReadOnly(readonly);
		((Widget)this.addInfoFieldButton).setVisible(!readonly);
		for(InfoEntry e : this.infoEntries) {
			e.setReadOnly(readonly);
		}
	}

}

