package bigBang.library.client.userInterface;

import java.util.Collection;
import java.util.List;

import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.util.ButtonHelper;
import org.gwt.mosaic.ui.client.util.ButtonHelper.ButtonLabelType;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.BigBangContactsListBroker;
import bigBang.library.client.dataAccess.ContactsBroker;
import bigBang.library.client.dataAccess.ContactsBrokerClient;
import bigBang.library.client.history.NavigationHistoryItem;
import bigBang.library.client.history.NavigationHistoryManager;
import bigBang.library.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContactsList extends FilterableList<Contact> implements ContactsBrokerClient {

	public static class Entry extends ListEntry<Contact> {

		public Entry(Contact value) {
			super(value);
			setHeight("35px");
			titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
			textLabel.getElement().getStyle().setFontSize(9, Unit.PX);
			textLabel.getElement().getStyle().setFontStyle(FontStyle.ITALIC);
		}

		public <I extends Object> void setInfo(I info) {
			Contact c = (Contact) info;
			this.setTitle(c.name);
			this.setText(c.typeLabel);
		};
	}

	protected int contactsDataVersion;
	protected String ownerId;
	protected ContactsBroker broker;
	protected ToolButton createNew;
	private String ownerTypeId;
	private VerticalPanel headerPanel;

	public ContactsList(){
		
		headerPanel = new VerticalPanel();
		setHeaderWidget(headerPanel);
		headerPanel.setSize("100%", "100%");
		
		this.showFilterField(false);
		this.showSearchField(true);
		
		showNewButton("Novo");
		
		this.broker = BigBangContactsListBroker.Util.getInstance();

		this.addAttachHandler(new AttachEvent.Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if(event.isAttached()) {
					setOwner(ownerId);
				}else{
					discardOwner();
				}
			}
		});
	}

	protected void createNewContact() {
		NavigationHistoryItem navItem = NavigationHistoryManager.getInstance().getCurrentState();
		navItem.setParameter("show", "contactmanagement");
		navItem.setParameter("ownerid", ownerId);
		navItem.setParameter("ownertypeid", ownerTypeId);
		navItem.setParameter("contactid", "new");
		navItem.setParameter("editpermission", createNew.isEnabled() ? "1" : "0");
		NavigationHistoryManager.getInstance().go(navItem);
	}

	public void setOwner(String ownerId){
		discardOwner();
		if(ownerId != null && !ownerId.isEmpty()){
			this.broker.registerClient(this, ownerId);
			this.broker.refreshContactsForOwner(ownerId, new ResponseHandler<Void>() {

						@Override
						public void onResponse(Void response) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onError(Collection<ResponseError> errors) {
							// TODO Auto-generated method stub
							
						}
			});
		}
		this.ownerId = ownerId;
	}

	public void discardOwner(){
		this.clear();
		if(ownerId != null){
			this.broker.unregisterClient(this, this.ownerId);
			this.ownerId = null;
		}
	}

	public void addEntry(Contact c){
		this.add(new Entry(c));
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CONTACT)) {
			this.contactsDataVersion = number;
		}
	}

	@Override
	public int getDataVersion(String dataElementId) {
		if(dataElementId.equalsIgnoreCase(BigBangConstants.EntityIds.CONTACT)){
			return contactsDataVersion;
		}
		return -1;
	}

	@Override
	public int getContactsDataVersionNumber(String ownerId) {
		return contactsDataVersion;
	}

	@Override
	public void setContactsDataVersionNumber(String ownerId, int number) {
		contactsDataVersion = number;
	}

	@Override
	public void setContacts(String ownerId, List<Contact> contacts) {
		this.clear();
		for(Contact c : contacts) {
			addEntry(c);
		}
	}

	@Override
	public void removeContact(String ownerId, String contactId) {
		if(ownerId == null){
			for(ValueSelectable<Contact> s : this) {
				if(s.getValue().id.equalsIgnoreCase(contactId)){
					remove(s);
				}
			}
		}
	}

	@Override
	public void addContact(String ownerId, Contact contact) {
		if(ownerId == null) {
			addEntry(contact);
		}
	}

	@Override
	public void updateContact(String ownerId, Contact contact) {
		for(ValueSelectable<Contact> s : this){
			if(s.getValue().id.equalsIgnoreCase(contact.id)){
				s.setValue(contact);
			}
		}
	}

	public void setOwnerType(String client) {
		ownerTypeId = client;
	}

	public void allowCreation(boolean hasPermission) {
		createNew.setEnabled(hasPermission);
	}
	
	public void showNewButton(String text){
		Resources r = GWT.create(Resources.class);
		createNew = new ToolButton(ButtonHelper.createButtonLabel(
				AbstractImagePrototype.create(r.listNewIcon()), text,
				ButtonLabelType.TEXT_ON_LEFT));
		
		createNew.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				createNewContact();
			}
		});
		
		createNew.setEnabled(false);
		headerPanel.setWidth("100%");
		headerPanel.add(createNew);
		headerPanel.setCellHorizontalAlignment(createNew, HasHorizontalAlignment.ALIGN_RIGHT);
		createNew.getElement().getStyle().setTop(4, Unit.PX);
		createNew.getElement().getStyle().setRight(3, Unit.PX);
		setHeaderWidget(headerPanel);
	}
}