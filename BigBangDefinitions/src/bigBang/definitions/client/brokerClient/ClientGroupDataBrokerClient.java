package bigBang.definitions.client.brokerClient;

import bigBang.definitions.client.types.ClientGroup;
import bigBang.library.client.dataAccess.DataBrokerClient;

public interface ClientGroupDataBrokerClient extends
		DataBrokerClient<ClientGroup> {

	/**
	 * Sends all the existing groups to the broker client
	 * @param groups The array with the groups
	 */
	public void setGroups(ClientGroup[] groups);
	
	/**
	 * Adds a client group to the BrokerClient cache
	 * @param group The client group to add
	 */
	public void addGroup(ClientGroup group);
	
	/**
	 * Updates the current client group info in the BrokerClient
	 * @param group The client group information
	 */
	public void updateGroup(ClientGroup group);
	
	/**
	 * Removes a client group from the BrokerClient cache
	 * @param groupId The client group id
	 */
	public void removeGroup(String groupId);
	
}
