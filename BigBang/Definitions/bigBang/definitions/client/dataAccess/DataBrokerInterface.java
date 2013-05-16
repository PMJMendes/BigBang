package bigBang.definitions.client.dataAccess;

import java.util.Collection;

public interface DataBrokerInterface <T> {

	/**
	 * Sets the current data version to the next possible and valid value.
	 */
	abstract void incrementDataVersion();
	
	/**
	 * Checks if all registered clients have the correct version number.
	 * @return true if all clients have the correct data version and false otherwise.
	 */
	abstract boolean checkClientDataVersions();
	
	/**
	 * Gets the current data version being held by the broker
	 * @return The data version number
	 */
	abstract int getCurrentDataVersion();
	
	/**
	 * Registers a client for this data broker.
	 * @param client The client to be registered
	 */
	public void registerClient(DataBrokerClient<T> client);
	
	/**
	 * Voids the client registration
	 * @param client The client to be unregistered
	 */
	public void unregisterClient(DataBrokerClient<T> client);
	
	/**
	 * Gets the clients for the data broker
	 * @return A collection with the registered clients in the data broker
	 */
	public Collection<DataBrokerClient<T>> getClients();
	
	/**
	 * Gets the id of the data element being managed by the broker
	 * @return The data element id
	 */
	public String getDataElementId();
	
	/**
	 * Requires the data broker to refresh its currently held data
	 */
	public void requireDataRefresh();
	
	/**
	 * Notifies the broker that an item was created with the given id
	 * @param itemId The id of the created item
	 */
	public void notifyItemCreation(String itemId);
	
	/**
	 * Notifies the broker that an item was deleted
	 * @param itemId The id of the deleted item
	 */
	public void notifyItemDeletion(String itemId);
	
	/**
	 * Notifies the broker that an item was altered
	 * @param itemId The id of the altered item
	 */
	public void notifyItemUpdate(String itemId);
	
}
