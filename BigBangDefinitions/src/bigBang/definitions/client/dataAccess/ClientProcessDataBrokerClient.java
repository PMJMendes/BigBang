package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.Client;

public interface ClientProcessDataBrokerClient extends DataBrokerClient<Client> {

	/**
	 * Adds a client to the BrokerClient cache
	 * @param client The client to add
	 */
	public void addClient(Client client);
	
	/**
	 * Updates the current client info in the BrokerClient
	 * @param client The client information
	 */
	public void updateClient(Client client);
	
	/**
	 * Removes a client from the BrokerClient cache
	 * @param clientId The client id
	 */
	public void removeClient(String clientId);

}
