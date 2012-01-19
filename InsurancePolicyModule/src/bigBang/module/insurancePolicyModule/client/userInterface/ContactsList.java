package bigBang.module.insurancePolicyModule.client.userInterface;

import java.util.List;

import bigBang.definitions.shared.Contact;
import bigBang.library.client.ValueSelectable;
import bigBang.library.client.dataAccess.BigBangContactsListBroker;
import bigBang.library.client.dataAccess.ContactsBroker;
import bigBang.library.client.dataAccess.ContactsBrokerClient;
import bigBang.library.client.userInterface.FilterableList;
import bigBang.library.client.userInterface.ListEntry;

public class ContactsList extends FilterableList<Contact> implements ContactsBrokerClient {

	protected class Entry extends ListEntry<Contact> {
		public Entry(Contact contact){
			super(contact);
		}
		
		public <I extends Object> void setInfo(I info) {
			Contact contact = (Contact) info;
			setTitle(contact.name);
		};
	}
	
	protected ContactsBroker broker;

	public ContactsList(){
		this.broker = new BigBangContactsListBroker();
	}

	@Override
	public void setDataVersionNumber(String dataElementId, int number) {
		return;
	}

	@Override
	public int getDataVersion(String dataElementId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getContactsDataVersionNumber(String ownerId) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setContactsDataVersionNumber(String ownerId, int number) {
		// TODO Auto-generated method stub

	}

	protected void addEntry(Contact contact){
		add(new Entry(contact));
	}

	@Override
	public void setContacts(String ownerId, List<Contact> contacts) {
		for(Contact c : contacts) {
			addEntry(c);
		}
	}

	@Override
	public void removeContact(String ownerId, String contactId) {
		for(ValueSelectable<Contact> vs : this){
			if(vs.getValue().id.equalsIgnoreCase(contactId)){
				remove(vs);
				break;
			}
		}
	}

	@Override
	public void addContact(String ownerId, Contact contact) {
		addEntry(contact);
	}

	@Override
	public void updateContact(String ownerId, Contact contact) {
		for(ValueSelectable<Contact> vs : this){
			if(vs.getValue().id.equalsIgnoreCase(contact.id)){
				vs.setValue(contact);
				break;
			}
		}
	}

	public void setOwner(String owner){
		broker.unregisterClient(this);
		broker.registerClient(this, owner);
	}

}
