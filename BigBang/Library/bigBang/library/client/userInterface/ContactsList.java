package bigBang.library.client.userInterface;

import java.util.Collection;
import java.util.List;

import bigBang.definitions.client.response.ResponseError;
import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.BigBangConstants;
import bigBang.definitions.shared.Contact;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.BigBangContactsListBroker;
import bigBang.library.client.dataAccess.ContactsBroker;
import bigBang.library.client.dataAccess.ContactsBrokerClient;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.AttachEvent;

public class ContactsList extends FilterableList<Contact> implements ContactsBrokerClient {

	public static class Entry extends ListEntry<Contact> {

		public Entry(Contact value) {
			super(value);
			setHeight("25px");
			titleLabel.getElement().getStyle().setFontSize(11, Unit.PX);
		}

		public <I extends Object> void setInfo(I info) {
			Contact c = (Contact) info;
			this.setTitle(c.name);
		};
	}

	protected int contactsDataVersion;
	protected String ownerId;
	protected ContactsBroker broker;

	public ContactsList(){
		this.showFilterField(false);
		this.showSearchField(true);

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

	public void setOwner(String ownerId){
		discardOwner();
		if(ownerId != null){
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
}