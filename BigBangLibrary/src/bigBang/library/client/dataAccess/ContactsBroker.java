package bigBang.library.client.dataAccess;

import java.util.Collection;
import java.util.List;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Contact;

public interface ContactsBroker {
	/**
	 * Sets the current data version to the next possible and valid value.
	 * @param ownerId The id of the owner of the contacts
	 */
	public int incrementDataVersion(String ownerId);
	
	/**
	 * Checks if all registered clients have the correct version number.
	 * @return true if all clients have the correct data version and false otherwise.
	 */
	public boolean checkClientDataVersions();
	
	/**
	 * Checks if all registered clients for the contacts of a given owner have the correct version number.
	 * @param ownerId The id of the owner of the contacts
	 * @return true if all clients have the correct data version and false otherwise.
	 */
	public boolean checkClientDataVersions(String ownerId);
	
	/**
	 * Gets the current data version being held by the broker for a given owner
	 * @param ownerId The is of the owner of the contacts
	 * @return The data version number
	 */
	public int getCurrentDataVersion(String ownerId);
	
	/**
	 * Registers a client for this data broker.
	 * @param client The client to be registered
	 * @param ownerId The id of the owner of the contacts
	 */
	public void registerClient(ContactsBrokerClient client, String ownerId);
	
	/**
	 * Voids the client registration
	 * @param client The client to be unregistered
	 */
	public void unregisterClient(ContactsBrokerClient client);
	
	/**
	 * Voids the client registration for the contacts of a given owner
	 * @param client The client to be unregistered
	 * @param ownerId The id of the owner of the contacts
	 */
	public void unregisterClient(ContactsBrokerClient client, String ownerId);
	
	/**
	 * Gets the clients for the data broker
	 * @return A collection with the registered clients in the data broker
	 */
	public Collection<ContactsBrokerClient> getClients();
	
	/**
	 * Gets the clients for the data broker for a given contact owner
	 * @param ownerId The id of the owner of the contacts
	 * @return A collection with the registered clients in the data broker
	 */
	public Collection<ContactsBrokerClient> getClients(String ownerId);
	
	/**
	 * Requires que data broker to refresh its currently held data
	 */
	public void requireDataRefresh();
	
	/**
	 * Requires que data broker to refresh its currently held data for a given contact owner
	 * @param ownerId The id of the owner of the contacts
	 */
	public void requireDataRefresh(String ownerId);
	
	/**
	 * Gets a contact from a specified owner
	 * @param ownerId The id of the owner of the contact
	 * @param contactId The id of the contact to fetch
	 * @param handler The handler to be notified on response
	 */
	public void getContact(String ownerId, String contactId, ResponseHandler<Contact> handler);
	
	/**
	 * Gets all contacts from a specified owner
	 * @param ownerId The id of the owner of the contacts
	 * @param handler The handler to be notified on response
	 */
	public void getContacts(String ownerId, ResponseHandler<List<Contact>> handler);
	
	/**
	 * Adds a contact to a specified owner
	 * @param processId The id of the process
	 * @param opId The id of the operation
	 * @param ownerId The id of the owner of the contact
	 * @param contact The contact to add
	 * @param handler The handler to be notified on response
	 */
	public void addContact(String processId, String opId, String ownerId, Contact contact, ResponseHandler<Contact> handler);
	
	/**
	 * Updates a contact for a given owner
	 * @param processId The id of the process
	 * @param opId The id of the operation
	 * @param ownerId The id of the owner of the contact
	 * @param contact The contact to be updated
	 * @param handler The handler to be notified on response
	 */
	public void updateContact(String processId, String opId, String ownerId, Contact contact, ResponseHandler<Contact> handler);
	
	/**
	 * Removes a contact from a specifed owner
	 * @param processId The id of the process
	 * @param opId The id of the operation
	 * @param ownerId The id of the owner of the contact
	 * @param contactId The id of the contact to be removed
	 * @param handler The handler to be notified on response
	 */
	public void removeContact(String processId, String opId, String ownerId, String contactId, ResponseHandler<Contact> handler);
}
