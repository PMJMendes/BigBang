package bigBang.library.client.dataAccess;

import java.util.List;

import bigBang.definitions.shared.Contact;

public interface ContactsBrokerClient {
	/**
	 * @param ownerId The id of the owner of the contacts
	 * @return the version of the data held by the client
	 */
	public int getContactsDataVersionNumber(String ownerId);
	
	/**
	 * @param ownerId The id of the owner of the contacts
	 * @param number the data version number
	 */
	public void setContactsDataVersionNumber(String ownerId, int number);
	
	/**
	 * @param The id of the owner of the contacts
	 * @param contacts The contacts to be set
	 */
	public void setContacts(String ownerId, List<Contact> contacts);
	
	/**
	 * Indicates that the client should remove the contact if it is cached.
	 * @param ownerId The id of the owner of the contacts
	 * @param contact the item to remove
	 */
	public void removeContact(String ownerId, Contact contact);
	
	/**
	 * Indicates that the client should take notice of a new contact
	 * @param ownerId The id of the owner of the contacts
	 * @param contact the contact to add
	 */
	public void addContact(String ownerId, Contact contact);
	
	/**
	 * Indicates that the client should update its cached representation of the contact
	 * @param ownerId The id of the owner of the contacts
	 * @param contact the contact to update
	 */
	public void updateContact(String ownerId, Contact contact);
}
