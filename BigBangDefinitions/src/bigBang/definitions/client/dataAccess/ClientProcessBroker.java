package bigBang.definitions.client.dataAccess;

import bigBang.definitions.client.response.ResponseHandler;
import bigBang.definitions.shared.Client;

/**
 * The interface for a Client processes DataBroker
 */
public interface ClientProcessBroker extends DataBrokerInterface<Client> {

	/**
	 * Fetches the client for a given id
	 * @param clientId The client id
	 * @param handler The handler to be notified on response
	 */
	public void getClient(String clientId, ResponseHandler<Client> handler);
	
	/**
	 * Creates a new Client
	 * @param client The client to be created
	 * @param handler The handler to be notified on response
	 */
	public void addClient(Client client, ResponseHandler<Client> handler);
	
	/**
	 * Updates the client for a given id
	 * @param clientId The client id
	 * @param handler The handler to be notified on response
	 */
	public void updateClient(Client client, ResponseHandler<Client> handler);
	
	/**
	 * Removes the client for a given id
	 * @param clientId The client id
	 * @param handler The handler to be notified on response
	 */
	public void removeClient(String clientId, ResponseHandler<String> handler);
	
}
