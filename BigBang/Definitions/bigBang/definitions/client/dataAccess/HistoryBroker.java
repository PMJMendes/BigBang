package bigBang.definitions.client.dataAccess;

import bigBang.definitions.shared.HistoryItem;
import bigBang.definitions.shared.HistoryItemStub;
import bigBang.definitions.client.response.ResponseHandler;

public interface HistoryBroker extends DataBrokerInterface<HistoryItem> {

	/**
	 * Returns whether or not the history data for a process needs to be refreshed
	 * @param objectId The id of the process for which to require the data refresh
	 */
	public void requireDataRefresh(String objectId);
	
	/**
	 * Registers a client for this data broker for a given process id
	 * @param client The client to be registered
	 * @param objectId The id of the process to watch
	 */
	public void registerClient(DataBrokerClient<HistoryItem> client, String objectId);
	
	/**
	 * Voids the client registration for a given process
	 * @param client The client to be unregistered
	 * @param objectId The watched process id
	 */
	public void unregisterClient(DataBrokerClient<HistoryItem> client, String objectId);
	
	/**
	 * Gets the history items for a given process
	 * @param objectId The id of the process to which the history items belong
	 * @param handler The handler to be notified on response
	 */
	public void getItems(String objectId, ResponseHandler<HistoryItem[]> handler);
	
	/**
	 * Gets a specific history item
	 * @param itemId The id of the item
	 * @param The id of the process to which the item belongs
	 * @param handler The handler to be notified on response 
	 */
	public void getItem(String itemId, String objectId, ResponseHandler<HistoryItem> handler);
	
	/**
	 * Reverts a given history item
	 * @param undoItemId The history item
	 * @param handler The handler to be notified on response
	 */
	public void undo(String undoItemId, ResponseHandler<HistoryItem> handler);
	
	/**
	 * Gets the search broker for the burrent broker type
	 * @return the search broker for history items
	 */
	public SearchDataBroker<HistoryItemStub> getSearchBroker();
	
}
