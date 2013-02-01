package bigBang.library.client.userInterface;

import java.util.Collection;
import java.util.List;

import bigBang.definitions.shared.Contact;
import bigBang.library.client.ValueSelectable;

import com.google.gwt.event.dom.client.HasClickHandlers;

public class ContactManagementContactList extends ContactsList{

	public ContactManagementContactList() {
		super();
		setWidth("225px");
	}

	@Override
	protected void createNewContact() {
		return;
	}

	@Override
	public void setContacts(String ownerId, List<Contact> contacts) {
		Collection<ValueSelectable<Contact>> selected = this.getSelected();

		super.setContacts(ownerId, contacts);

		for(ValueSelectable<Contact> sel : selected){
			ListEntry<Contact> entry = (ListEntry<Contact>) sel;

			for(ListEntry<Contact> cont: this){
				if(cont.getValue().id.equals(entry.getValue().id)){
					cont.setSelected(true, false);
				}
			}
		}
	}

	public HasClickHandlers getCreateNewButton(){
		return createNew;
	}

	@Override
	public void addContact(String ownerId, Contact contact) {
		
		super.addContact(ownerId, contact);

		if(ownerId != null){

			for(ListEntry<Contact> cont : this){
				if(cont.getValue().id.equals("new")){
					remove(cont);
					break;
				}
			}
			
			for(ListEntry<Contact> cont : this){
				if(contact.id.equals(cont.getValue().id)){
					cont.setSelected(true, false);
					break;
				}
			}

		}
	}

	public boolean removeContactWithId(String string) {
		for(ListEntry<Contact> entry : this){
			if(string.equalsIgnoreCase(entry.getValue().id)){
				remove(entry);
				return true;
			}
		}
		return false;
	}
}
