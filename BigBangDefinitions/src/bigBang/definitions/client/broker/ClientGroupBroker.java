package bigBang.definitions.client.broker;

import bigBang.definitions.client.types.ClientGroup;
import bigBang.library.client.dataAccess.DataBrokerInterface;
import bigBang.library.client.response.ResponseHandler;


/**
 * The interface for a Client Group DataBroker
 */
public interface ClientGroupBroker extends DataBrokerInterface<ClientGroup> {

	/**
	 * Fetches all available client groups
	 * @param handler The handler to be notified on response
	 */
	public void getClientGroups(ResponseHandler<ClientGroup[]> handler);
	
	/**
	 * Creates a client group
	 * @param group The group to be created
	 * @param handler The handler to be notified on response
	 */
	public void addClientGroup(ClientGroup group, ResponseHandler<ClientGroup> handler);
	
	/**
	 * Fetches the client group for a given id
	 * @param groupId The client group id
	 * @param handler The handler to be notified on response
	 */
	public void getClientGroup(String groupId, ResponseHandler<ClientGroup> handler);

	/**
	 * Updates the client group for a given id
	 * @param group The client group
	 * @param handler The handler to be notified on response
	 */
	public void updateClientGroup(ClientGroup group, ResponseHandler<ClientGroup> handler);

	/**
	 * Removes the client group for a given id
	 * @param groupId The client group id
	 * @param handler The handler to be notified on response
	 */
	public void removeClientGroup(String groupId, ResponseHandler<ClientGroup> handler);

}
