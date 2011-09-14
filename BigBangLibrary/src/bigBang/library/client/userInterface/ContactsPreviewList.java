package bigBang.library.client.userInterface;

import java.util.Collection;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Contact;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.BigBangContactsListBroker;
import bigBang.library.client.dataAccess.ContactsBroker;
import bigBang.library.client.dataAccess.ContactsBrokerClient;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.view.View;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class ContactsPreviewList extends List<Contact> implements ContactsBrokerClient {

	public static abstract class ContactView extends View {
		public ContactView(){
			SimplePanel wrapper = new SimplePanel();
			wrapper.setSize("400px", "500px");
			initWidget(wrapper);
		}

		public void setEditable(boolean editable) {
			//TODO
		}

		public void setContact(Contact contact){
			//TODO
		}

		public abstract void onSaveContact(Contact contact);
	}

	public static class Entry extends ListEntry<Contact> {

		public Entry(Contact value) {
			super(value);
		}

		public <I extends Object> void setInfo(I info) {
			Contact c = (Contact) info;
			setText(c.name);
		};
	}

	protected Resources resources;
	protected PopupPanel contactPopupPanel;
	protected SelectionChangedEventHandler selectionChangedHandler;
	protected HandlerRegistration managerHandlerRegistration;
	protected Button newButton;
	protected boolean readOnly = false;
	protected String contactOwner;
	protected String operationId;
	protected String processId; 
	protected ContactsBroker broker;

	protected int contactsDataVersion;

	public ContactsPreviewList(){
		super();

		this.broker = BigBangContactsListBroker.Util.getInstance();

		this.scrollPanel.getElement().getStyle().setOverflow(Overflow.AUTO);
		resources = GWT.create(Resources.class);
		ListHeader header = new ListHeader();
		header.setText("Contactos");
		header.setHeight("25px");
		header.setLeftWidget(new Image(resources.contactsIcon()));

		setHeaderWidget(header);

		setSize("100%", "145px");

		this.scrollPanel.getElement().getStyle().setOverflow(Overflow.HIDDEN);

		contactPopupPanel = new PopupPanel();
		contactPopupPanel.setAutoHideEnabled(true);
		contactPopupPanel.setSize("350px", "400px");

		selectionChangedHandler = new SelectionChangedEventHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				if(!event.getSelected().isEmpty()){
					@SuppressWarnings("unchecked")
					Contact selected = (Contact) ((ValueSelectable<Contact>)event.getFirstSelected()).getValue();
					showContactForm(selected);
				}else{
					if(contactPopupPanel.isShowing()){
						contactPopupPanel.hide();
					}
				}
			}
		};

		contactPopupPanel.addCloseHandler(new CloseHandler<PopupPanel>() {

			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				clearSelection();
			}
		});

		newButton = new Button("Novo");
		newButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				showNewContactForm();
			}
		});
		header.setRightWidget(newButton);
	}

	public void addEntry(Contact contact) {
		ListEntry<Contact> entry = new Entry(contact);
		this.add(entry);
	}

	public void setContactProcessAndOperationAndOwner(String processId, String opId, String ownerId) {
		if(this.contactOwner != ownerId || !this.contactOwner.equalsIgnoreCase(ownerId)) {
			if(this.contactOwner != null){
				this.broker.unregisterClient(this, this.contactOwner);
			}
			this.processId = processId;
			this.contactOwner = ownerId;
			this.operationId = opId;
			this.broker.registerClient(this, ownerId);
		}
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		if(this.contactPopupPanel.isShowing()){
			this.contactPopupPanel.hide();
		}
		this.newButton.setEnabled(!readOnly);
	}

	@Override
	public int getContactsDataVersionNumber(String ownerId) {
		return this.contactsDataVersion;
	}

	@Override
	public void setContactsDataVersionNumber(String ownerId, int number) {
		if(ownerId.equalsIgnoreCase(this.contactOwner)){
			this.contactsDataVersion = number;
		}
	}

	@Override
	public void setContacts(String ownerId, java.util.List<Contact> contacts) {
		if(ownerId.equalsIgnoreCase(this.contactOwner)){
			this.clear();
			for(Contact c : contacts){
				addEntry(c);
			}
		}
	}

	@Override
	public void removeContact(String ownerId, Contact contact) {
		if(ownerId.equalsIgnoreCase(this.contactOwner)){
			for(ValueSelectable<Contact> c : this) {
				if(c.getValue().id.equalsIgnoreCase(contact.id)){
					remove(c);
					break;
				}
			}
		}
	}

	@Override
	public void addContact(String ownerId, Contact contact) {
		if(ownerId.equalsIgnoreCase(this.contactOwner)){
			this.addEntry(contact);
		}
	}

	@Override
	public void updateContact(String ownerId, Contact contact) {
		if(ownerId.equalsIgnoreCase(this.contactOwner)){
			for(ValueSelectable<Contact> c : this) {
				if(c.getValue().id.equalsIgnoreCase(contact.id)){
					c.setValue(contact, true);
					break;
				}
			}
		}
	}

	protected void showNewContactForm(){
		ContactView contactView = new ContactView() {

			@Override
			public void onSaveContact(Contact contact) {
				broker.addContact(processId, operationId, contactOwner, contact, new ResponseHandler<Contact>() {

					@Override
					public void onResponse(Contact response) {
						setEditable(false);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {}
				});
			}
		};
		contactView.setEditable(true);
		contactPopupPanel.clear();
		contactPopupPanel.add(contactView);
		contactPopupPanel.center();

		//TODO APAGAR FJVC
		Contact teste = new Contact();
		teste.address = null;
		teste.isSubContact = false;
		teste.name = "Um teste";
		teste.subContacts = new Contact[0];
		teste.ownerId = contactOwner;
		teste.typeId = "04F6BC3C-0283-47F0-9670-9EEE013350D9";
		broker.addContact(processId, operationId, contactOwner, teste, new ResponseHandler<Contact>() {

			@Override
			public void onResponse(Contact response) {
				GWT.log("criado");
			}

			@Override
			public void onError(Collection<ResponseError> errors) {
				GWT.log("erro");
			}
		});
	}

	protected void showContactForm(Contact contact) {
		ContactView contactView = new ContactView() {

			@Override
			public void onSaveContact(Contact contact) {
				broker.updateContact(processId, operationId, contactOwner, contact, new ResponseHandler<Contact>() {

					@Override
					public void onResponse(Contact response) {
						setEditable(false);
					}

					@Override
					public void onError(Collection<ResponseError> errors) {}
				});
			}
		};
		contactView.setContact(contact);
		contactPopupPanel.clear();
		contactPopupPanel.add(contactView);
		contactPopupPanel.center();
	}
}
