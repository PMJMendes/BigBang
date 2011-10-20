package bigBang.library.client.userInterface;

import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.BigBangContactsListBroker;
import bigBang.library.client.dataAccess.ContactsBroker;
import bigBang.library.client.dataAccess.ContactsBrokerClient;
import bigBang.library.client.event.SelectionChangedEvent;
import bigBang.library.client.event.SelectionChangedEventHandler;
import bigBang.library.client.resources.Resources;
import bigBang.library.client.userInterface.view.ContactView;
import bigBang.library.client.userInterface.view.ContactsNavigationPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class ContactsPreviewList extends List<Contact> implements ContactsBrokerClient {

	public static class Entry extends ListEntry<Contact> {

		public Entry(Contact value) {
			super(value);
			setToggleable(true);
			this.setHeight("25px");
			this.titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		}

		public <I extends Object> void setInfo(I info) {
			Contact c = (Contact) info;
			setTitle(c.name);
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

		contactPopupPanel = new PopupPanel();
		contactPopupPanel.setAutoHideEnabled(true);
		contactPopupPanel.setSize("370px", "400px");
		contactPopupPanel.getElement().getStyle().setOverflow(Overflow.HIDDEN);

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
		addSelectionChangedEventHandler(selectionChangedHandler);

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
				this.contactsDataVersion = 0;
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
		ContactView contactView = new ContactView();
		contactView.setEditable(true);
		contactPopupPanel.clear();
		ContactsNavigationPanel navPanel = new ContactsNavigationPanel();
		navPanel.setMainContact(contactView);
		contactPopupPanel.add(navPanel);

		contactPopupPanel.setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				for(ValueSelectable<Contact> s : ContactsPreviewList.this.getSelected()){
					ListEntry<?> entry = (ListEntry<?>) s;
					contactPopupPanel.setPopupPosition(entry.getAbsoluteLeft()-offsetWidth, entry.getAbsoluteTop() - 20);
					break;
				}
			}
		});
	}

	protected void showContactForm(Contact contact) {
		ContactView contactView = new ContactView();
		contactView.setContact(contact);
		contactPopupPanel.clear();
		ContactsNavigationPanel navPanel = new ContactsNavigationPanel();
		navPanel.setMainContact(contactView);
		contactPopupPanel.add(navPanel);

		contactPopupPanel.setPopupPositionAndShow(new PositionCallback() {

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
				for(ValueSelectable<Contact> s : ContactsPreviewList.this.getSelected()){
					ListEntry<?> entry = (ListEntry<?>) s;
					int left = entry.getAbsoluteLeft()-offsetWidth;
					int top = entry.getAbsoluteTop() - 20;

					if(left < 0)
						left = 0;
					if((top + offsetHeight) > RootPanel.get().getOffsetHeight()){
						top = RootPanel.get().getOffsetHeight() - offsetHeight;
					}
					if(top < 0)
						top = 0;

					contactPopupPanel.setPopupPosition(left, top);
					break;
				}
			}
		});
	}

	public void clearAll() {
		this.clear();
		this.contactOwner = null;
		this.processId = null;
		this.operationId = null;
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		return;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CONTACT)){
			return contactsDataVersion;
		}
		return -1;
	}
}
